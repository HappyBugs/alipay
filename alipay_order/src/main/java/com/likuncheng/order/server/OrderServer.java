package com.likuncheng.order.server;


import com.likuncheng.base.ResponseBase;
import com.likuncheng.order.entity.OrderBean;

/**
 * orderapi接口
 * @author Administrator
 *
 */
public interface OrderServer {
	
	
	/**
	 * 创建一个order
	 * @param order
	 * @return orderid
	 */
	public ResponseBase createOrder(OrderBean order) throws Exception ;
	
	/**
	 * 根据订单id进行删除订单
	 * @param id
	 * @return 
	 */
	public ResponseBase delOrderById(String id) throws Exception ;
	
	
	/**
	 * 根据订单id查询订单信息
	 * @param id
	 * @return
	 */
	public ResponseBase getOrderById(String id) throws Exception ;
	
	/**
	 * 根据订单id进行修改订单支付状态
	 * @param id
	 * @return
	 */
	public ResponseBase updOrderStateById(String id,Integer state) throws Exception ;

	
	

}
