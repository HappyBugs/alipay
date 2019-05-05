package com.likuncheng.core.serverImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import com.likuncheng.base.BaseApiService;
import com.likuncheng.base.BaseRedisService;
import com.likuncheng.base.ResponseBase;
import com.likuncheng.core.entity.PayBean;
import com.likuncheng.core.feign.OrderFeign;
import com.likuncheng.core.mapper.CoreMapper;
import com.likuncheng.core.server.CoreServer;

import alipay.config.AlipayConfig;

@Service
public class CoreServerImpl extends BaseApiService implements CoreServer {

	// mapper层
	@Autowired
	private CoreMapper coreMapper;

	// 引入order远程调用服务
	@Autowired
	private OrderFeign orderFeign;

	// redis
	@Autowired
	private BaseRedisService baseRedisService;

	// 来自order服务
	// 分布式事务 没有分布式事务就创建分布式事务
	@TxcTransaction(propagation = DTXPropagation.REQUIRED)
	@Transactional
	@Override
	public ResponseBase createPayToken() throws Exception {
		String outTradeNo = "";
		// 调用order创建订单（创建令牌）
		ResponseBase createOrder = orderFeign.createOrder();
		outTradeNo = (String) createOrder.getData();
		// 令牌判断
		if (StringUtils.isEmpty(outTradeNo)) {
			throw new Exception("订单号为空");
		}
		return setResultSuccessData(outTradeNo, "创建令牌成功");
	}

	@Transactional
	@Override
	public ResponseBase createPayLog(PayBean payBean) throws Exception {
		Integer createPayLog = 0;
		// 创建支付信息记录
		createPayLog = coreMapper.createPayLog(payBean);
		if (createPayLog <= 0) {
			throw new Exception("创建支付信息失败");
		}
		return setResultSuccessData(createPayLog, "创建支付信息成功");
	}

	@Transactional
	@Override
	public ResponseBase updPayLogByOutTradeNo(String outTradeNo, String tradeNo, String createTime) throws Exception {
		// 修改支付信息记录 因为第一次 初始创建的时候 为了方便测试 创建的数据保存的并不完整 这就是给后面异步回调的时候进行数据库操作
		// 用于确定用户支付成功 好保存全部的完整的支付信息
		Integer updPayLogByOutTradeNo = coreMapper.updPayLogByOutTradeNo(outTradeNo, tradeNo, createTime);
		if (updPayLogByOutTradeNo <= 0) {
			throw new Exception("更新支付信息失败");
		}
		return setResultSuccessData(updPayLogByOutTradeNo, "更新支付信息成功");
	}

	@Override
	public ResponseBase getMoneyByOutTradeNo(String outTradeNo) {
		String moneyByOutTradeNo = "";
		// 得到订单的金额 （用于判断用户实际支付金额 是否合法 如果不合法就更改支付状态为2 金额异常）
		moneyByOutTradeNo = coreMapper.getMoneyByOutTradeNo(outTradeNo);
		if (StringUtils.isEmpty(moneyByOutTradeNo)) {
			throw new NullPointerException("空指针，用户金额异常");
		}
		return setResultSuccessData(moneyByOutTradeNo, "查询用户订单金额成功");
	}

	// 来自order服务
	// 分布式事务 没有分布式事务就创建分布式事务
	@TxcTransaction(propagation = DTXPropagation.REQUIRED)
	@Transactional
	@Override
	public ResponseBase updOrderStateById(String id, Integer state) throws Exception {
		// 调用order修改支付状态服务 0：未支付 1：已支付 2：金额异常
		ResponseBase updOrderStateById = orderFeign.updOrderStateById(id, state);
		Integer number = (Integer) updOrderStateById.getData();
		if (number <= 0) {
			throw new Exception("修改支付状态异常");
		}
		return setResultSuccess("修改订单支付状态成功");
	}

	@Override
	public ResponseBase payReturn(Map<String, String> params) {
		// 封装一个from表单 用于隐藏支付宝同步回调的时候的参数
		String htmlFrom = "<form name='punchout_form' method='post' action='http://127.0.0.1/aliPay/paySuccessReturn'>"
				+ "<input type='hidden' name='outTradeNo' value='" + params.get("out_trade_no") + "'/>"
				+ "<input type='hidden' name='tradeNo' value='" + params.get("trade_no") + "'/>"
				+ "<input type='hidden' name='totalAmount' value='" + params.get("total_amount") + "'/>"
				+ "<input type='submit' value='立即支付' style='display:none'/>"
				+ "</form><script>document.forms[0].submit();</script>";
		return setResultSuccessData(htmlFrom, "成功封装为form表单");
	}

	// 来自order服务
	// 分布式事务 没有分布式事务就创建分布式事务
	@TxcTransaction(propagation = DTXPropagation.REQUIRED)
	@Transactional
	@Override
	public ResponseBase payNotify(Map<String, String> params) throws Exception {
		// 订单号
		String outTradeNo = params.get("out_trade_no");
		// 支付宝交易号
		String tradeNo = params.get("trade_no");
		// 交易金额
		String totalAmount = params.get("total_amount");
		// 返回订单应支付金额
		String moneyByOutTradeNo = coreMapper.getMoneyByOutTradeNo(outTradeNo);
		// 订单生成时间
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createTime = sf.format(new Date());
		// 如果实际支付的金额和订单的金额不相等
		if (!totalAmount.equals(moneyByOutTradeNo)) {
			// 1. 2表示异常金额 后续调用退款服务 1表示正常支付完成 0表示未支付
			ResponseBase updOrderStateById = orderFeign.updOrderStateById(outTradeNo, 2);
			Integer number = (Integer) updOrderStateById.getData();
			if (number <= 0) {
				// 这里报错 新建异常 会导致整个事务回滚 然后 支付宝发起重试
				throw new Exception("修改记录信息异常");
			}
			// 2. 这里就应该调用退款服务 因为这是异常支付信息 不能完成支付
			// 。。。。。。。
			// 3. 就应该保存到支付信息表中
			// 修改支付信息 得到受影响的行数
			Integer updPayLogByOutTradeNo = coreMapper.updPayLogByOutTradeNo(outTradeNo, tradeNo, createTime);
			if (updPayLogByOutTradeNo <= 0) {
				// 这里报错 新建异常 会导致整个事务回滚 然后 支付宝发起重试
				throw new Exception("修改记录信息异常");
			}
			// 因为这里是 用户实际支付的金额 和订单金额不匹配 为了避免支付包的重试 所以这里直接返回success
			return setResultSuccessData("success", "用户错误的支付金额，请求调用退款服务");
		}
		// 如果用户支付金额 和订单金额 相匹配 修改支付状态
		ResponseBase updOrderStateById = orderFeign.updOrderStateById(outTradeNo, 1);
		Integer uptStateNumber = (Integer) updOrderStateById.getData();
		if (uptStateNumber <= 0) {
			throw new Exception("修改记录信息异常");
		}
		// 修改支付信息
		System.out.println(outTradeNo + "----" + tradeNo + "----" + createTime);
		Integer updPayLogByOutTradeNo = coreMapper.updPayLogByOutTradeNo(outTradeNo, tradeNo, createTime);
		if (updPayLogByOutTradeNo <= 0) {
			throw new Exception("修改记录信息异常");
		}
		return setResultSuccessData("success", "支付成功");
	}

	@Transactional
	@Override
	public ResponseBase PayServer(String outTradeNo) throws Exception {
		String result = "";
		// 判断reids中时候存在该缓存
		String outTradeNoValue = (String) baseRedisService.getString(outTradeNo);
		if (StringUtils.isEmpty(outTradeNoValue)) {
			throw new Exception("支付token无效");
		}
		PayBean payBean = new PayBean(outTradeNo, "500.5", "标题：这是一条测试数据",
				"描述：这是用于测试支付宝发起请求的数据，真实情况会接收这四个参数，为了方便测试，减少URL参数，所以我们自己封装死了这四个参数");
		// 获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
				AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key,
				AlipayConfig.sign_type);

		// 设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(AlipayConfig.return_url);
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

		// 商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = payBean.getOutTradeNo();
		// 付款金额，必填
		String total_amount = payBean.getTotalAmount();
		// 订单名称，必填
		String subject = payBean.getSubject();
		// 商品描述，可空
		String body = payBean.getBody();

		alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"," + "\"total_amount\":\"" + total_amount
				+ "\"," + "\"subject\":\"" + subject + "\"," + "\"body\":\"" + body + "\","
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

		// 请求
		result = alipayClient.pageExecute(alipayRequest).getBody();
		if (StringUtils.isEmpty(result)) {
			throw new Exception("发起请求异常");
		}
		// 保存日志信息
		Integer createPayLog = coreMapper.createPayLog(payBean);
		if (createPayLog <= 0) {
			throw new Exception("保存支付信息失败");
		}
		return setResultSuccessData(result, "发起支付信息成功");
	}

}
