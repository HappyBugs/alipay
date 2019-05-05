package com.likuncheng.core.server;

import java.util.Map;


import com.likuncheng.base.ResponseBase;
import com.likuncheng.core.entity.PayBean;

public interface CoreServer {

	/**
	 * 创建支付令牌
	 * 
	 * @return
	 */
	public ResponseBase createPayToken() throws Exception ;

	/**
	 * /** 创建支付记录
	 * 
	 * @return
	 */
	public ResponseBase createPayLog(PayBean payBean) throws Exception ;

	/**
	 * 根据订单号进行修改支付记录
	 * 
	 * @param payBean
	 * @return
	 */
	public ResponseBase updPayLogByOutTradeNo(String outTradeNo, String tradeNo, String createTime) throws Exception ;

	/**
	 * 根据订单号进行查询用户支付的金额 用于判断用户支付金额 是否与实际该支付金额一致
	 * 
	 * @param outTradeNo
	 * @return
	 */
	public ResponseBase getMoneyByOutTradeNo(String outTradeNo);

	/**
	 * 修改支付状态信息
	 * 
	 * @param id    就是订单号
	 * @param state
	 * @return
	 */
	public ResponseBase updOrderStateById(String id, Integer state) throws Exception ;

	/**
	 * 同步返回地址
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ResponseBase payReturn(Map<String, String> params);

	/**
	 * 异步返回地址
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ResponseBase payNotify(Map<String, String> params) throws Exception ;

	/**
	 * 发起支付请求
	 * 
	 * @param outTradeNo
	 * @param response
	 * @return
	 */
	public ResponseBase PayServer(String outTradeNo) throws Exception ;

}
