package com.likuncheng.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.likuncheng.base.ResponseBase;

@FeignClient("alipayOrder")
@Service
public interface OrderFeign {
	
	//我们只需要调用他的订单号 其他的业务操作 不需要我们关心
	@PostMapping(value = "createOrder")
	public ResponseBase createOrder();
	
	//调用修改支付状态接口
	@PostMapping(value = "updOrderStateById")
	public ResponseBase updOrderStateById(@RequestParam("id") String id,@RequestParam("state") Integer state);


}
