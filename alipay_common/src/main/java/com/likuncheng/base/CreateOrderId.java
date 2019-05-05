package com.likuncheng.base;


public class CreateOrderId {
	
	/**
	 * 创建order的id 现时中不能这样 因为只是本地测试 模拟出效果就好 才使用synchronized保证唯一
	 * @return
	 */
	public static synchronized String createOrderId() {
		String orderId = System.currentTimeMillis()+"";
		return orderId;
	}

}
