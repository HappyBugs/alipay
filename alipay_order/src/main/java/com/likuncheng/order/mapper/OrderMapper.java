package com.likuncheng.order.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.likuncheng.order.entity.OrderBean;

@Mapper
public interface OrderMapper  {
	
	/**
	 * 创建一个order
	 * @param order
	 * @return orderid
	 */
	public void createOrder(OrderBean order);
	
	/**
	 * 根据订单id进行删除订单
	 * @param id
	 * @return
	 */
	public Integer delOrderById(String id);
	
	/**
	 * 根据订单id查询订单信息
	 * @param id
	 * @return
	 */
	public OrderBean getOrderById(String id);
	
	/**
	 * 根据订单id进行修改订单支付状态
	 * @param id
	 * @return
	 */
	public Integer updOrderStateById(@Param("id")String id,@Param("state")Integer state);

}
