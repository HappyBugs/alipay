package com.likuncheng.order.entity;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.likuncheng.base.CreateOrderId;

import lombok.Data;

@Data
public class OrderBean {
	

	private String id;                         //订单id 也是支付token
	private Integer state;                     //支付状态
	private String createTime;                 //创建的时间
	
	
	public OrderBean() {
		//内部封装id（token）和创建时间 为的是模拟测试 这种token肯定是不合法的
		this.id = CreateOrderId.createOrderId();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createTime = sf.format(new Date());
		this.createTime = createTime;
	}
	
}
