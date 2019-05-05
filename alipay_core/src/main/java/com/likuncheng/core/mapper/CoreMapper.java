package com.likuncheng.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.likuncheng.core.entity.PayBean;

@Mapper
public interface CoreMapper {

	/**
	 * 创建支付记录
	 * 
	 * @return 返回新增的支付记录id 因为数据库是int类型的 所以可以返回
	 */
	public Integer createPayLog(PayBean payBean);

	/**
	 * 根据订单号进行修改支付记录
	 * 
	 * @param payBean
	 * @returnprivate String tradeNo; // 支付宝交易号 private String createTime;
	 */
	public Integer updPayLogByOutTradeNo(@Param("outTradeNo") String outTradeNo, @Param("tradeNo") String tradeNo,
			@Param("createTime") String createTime);

	/**
	 * 根据订单号进行查询用户支付的金额 用于判断用户支付金额 是否与实际该支付金额一致
	 * 
	 * @param outTradeNo
	 * @return
	 */
	public String getMoneyByOutTradeNo(@Param("outTradeNo") String outTradeNo);

}
