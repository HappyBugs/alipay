package com.likuncheng.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likuncheng.base.BaseApiService;
import com.likuncheng.base.ResponseBase;
import com.likuncheng.order.entity.OrderBean;
import com.likuncheng.order.serverImpl.OrderServerImpl;

@RestController
public class OrderController extends BaseApiService {

	@Autowired
	private OrderServerImpl orderServerImpl;

	@PostMapping(value = "createOrder")
	public ResponseBase createOrder() throws Exception {
		// 创建订单类 因为我们在内部构造函数创建了订单id而且数据库默认状态为0(未支付) 所以我们只需要传入一个参数 那就是创建时间
		OrderBean orderBean = new OrderBean();
		return orderServerImpl.createOrder(orderBean);
	}

	@PostMapping(value = "delOrderById")
	public ResponseBase delOrderById(@RequestParam("id") String id) throws Exception {
		return orderServerImpl.delOrderById(id);

	}

	@PostMapping(value = "getOrderById")
	public ResponseBase getOrderById(@RequestParam("id") String id) throws Exception {
		return orderServerImpl.getOrderById(id);
	}
	
	@PostMapping(value = "updOrderStateById")
	public ResponseBase updOrderStateById(@RequestParam("id") String id,@RequestParam("state") Integer state) throws Exception {
		return orderServerImpl.updOrderStateById(id,state);
	}

}
