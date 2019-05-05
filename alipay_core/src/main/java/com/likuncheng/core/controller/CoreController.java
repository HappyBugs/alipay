package com.likuncheng.core.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.likuncheng.base.BaseApiService;
import com.likuncheng.base.ResponseBase;
import com.likuncheng.core.serverImpl.CoreServerImpl;

import alipay.config.AlipayConfig;

@Controller
@RequestMapping(value = "/aliPay")
public class CoreController extends BaseApiService {

	private static final String PAY_SUCCES = "pay_success";

	@Autowired
	private CoreServerImpl coreServerImpl;

	/**
	 * 发起支付请求 这里内部进行封装了 需要发起请求的参数 为的就只是方便测试
	 * 
	 * @param outTradeNo
	 * @param response
	 */
	@RequestMapping(value = "payServer")
	public void payServer(@RequestParam("outTradeNo") String outTradeNo, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		try {
			PrintWriter writer = response.getWriter();
			ResponseBase payServer = coreServerImpl.PayServer(outTradeNo);
			String payHtml = (String) payServer.getData();
			// 4. 页面上进行渲染
			writer.println(payHtml);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 异步地址
	 * 
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "payNotify")
	public String payNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String result = "";
		Map<String, String> params = checkSign(request, response);
		ResponseBase payNotify = coreServerImpl.payNotify(params);
		result = (String) payNotify.getData();
		if (StringUtils.isEmpty(result)) {
			return "fail";
		}
		return result;
	}

	/**
	 * 同步地址 只用于给用户展示效果
	 * 
	 * @return
	 * @throws AlipayApiException
	 * @throws IOException
	 */
	@GetMapping(value = "payReturn")
	public void payReturn(HttpServletRequest request, HttpServletResponse response)
			throws AlipayApiException, IOException {
		PrintWriter writer = response.getWriter();
		try {
			Map<String, String> params = checkSign(request, response);
			// 如果验签失败返回false 如果成功才返回参数
			ResponseBase payReturn = coreServerImpl.payReturn(params);
			// 像页面写如内容
			System.err.println(payReturn.getData().toString());
			String payHtml = new String(payReturn.getData().toString());
			writer.println(payHtml);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			writer.close();

		}
	}

	/**
	 * 接收支付返回结果并验签 一起只能允许一个请求进去
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public synchronized Map<String, String> checkSign(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		// 得到请求中的所有参数
		Map<String, String[]> requestParams = request.getParameterMap();
		// 进行迭代所有的参数并且设置字体格式保存到 params中
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}

		// 验签
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset,
				AlipayConfig.sign_type);
		if (!signVerified) {
			throw new Exception("验签失败");
		}
		// 如果验签失败返回false 如果成功才返回参数
		return params;
	}

	/**
	 * 用于最后的同步回调显示地址 目的：通过from表单隐藏参数
	 * 
	 * @param request
	 * @param outTradeNo
	 * @param tradeNo
	 * @param totalAmount
	 * @return
	 */
	@PostMapping(value = "paySuccessReturn")
	public String paySuccessReturn(HttpServletRequest request, String outTradeNo, String tradeNo, String totalAmount) {
		request.setAttribute("outTradeNo", outTradeNo);
		request.setAttribute("tradeNo", tradeNo);
		request.setAttribute("totalAmount", totalAmount);
		return PAY_SUCCES;
	}

	/**
	 * 创建支付令牌
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "createPayToken")
	@ResponseBody
	public ResponseBase createPayToken() throws Exception {
		return coreServerImpl.createPayToken();
	}

}
