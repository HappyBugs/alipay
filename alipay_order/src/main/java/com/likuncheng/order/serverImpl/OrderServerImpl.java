package com.likuncheng.order.serverImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import com.likuncheng.base.BaseApiService;
import com.likuncheng.base.BaseRedisService;
import com.likuncheng.base.ResponseBase;
import com.likuncheng.order.entity.OrderBean;
import com.likuncheng.order.mapper.OrderMapper;
import com.likuncheng.order.server.OrderServer;

@Service
public class OrderServerImpl extends BaseApiService implements OrderServer {

	@Autowired
	private OrderMapper orderMapper;

	// 设置支付过期时间为15分钟 封装redis的时候单位为秒
	private static final long outTime = 1 * 60 * 15;

	@Autowired
	private BaseRedisService baseRedisService;

	// 如果没有分布式事务就按照常规事务执行 如果有分布式事务 就加入分布式事务
	@TxcTransaction(propagation = DTXPropagation.SUPPORTS)
	@Transactional
	@Override
	public ResponseBase createOrder(OrderBean order) throws Exception {
		// 创建订单的 订单号
		if (order == null) {
			throw new Exception("创建订单参数订单Bena为null");
		}
		//创建订单 这里订单号也被当作支付token令牌进行使用 
		orderMapper.createOrder(order);
		System.out.println(order.getId());
		baseRedisService.setString(order.getId(), order.getId(), outTime);
		return setResultSuccessData(order.getId(), "获得订单号成功");
	}

	@Transactional
	@Override
	public ResponseBase delOrderById(String id) throws Exception {
		if (StringUtils.isEmpty(id)) {
			throw new Exception("删除订单参数id不能为空");
		}
		//删除订单信息
		Integer number = orderMapper.delOrderById(id);
		if (number <= 0) {
			throw new Exception("删除用户信息失败");
		}
		return setResultSuccess("删除用户成功");

	}

	@Transactional
	@Override
	public ResponseBase getOrderById(String id) throws Exception {
		OrderBean orderById = null;
		if (StringUtils.isEmpty(id)) {
			throw new Exception("得到订单参数id不能为空");
		}
		//得到订单信息
		orderById = orderMapper.getOrderById(id);
		if (orderById == null) {
			throw new NullPointerException("查找用户失败");
		}
		return setResultSuccessData(orderById, "成功获得订单信息");
	}

	// 没有分布式就按照普通事务进行执行
	@TxcTransaction(propagation = DTXPropagation.REQUIRED)
	@Transactional
	@Override
	public ResponseBase updOrderStateById(String id, Integer state) throws Exception {
		Integer updOrderStateById = 0;
		if (StringUtils.isEmpty(id)) {
			throw new Exception("更新支付状态参数id不能为空");
		}
		//更新订单支付状态 0：未支付  1：已支付 2：异常金额
		updOrderStateById = orderMapper.updOrderStateById(id, state);
		if (updOrderStateById <= 0) {
			throw new Exception("修改信息失败");
		}
		return setResultSuccessData(updOrderStateById, "更新支付状态成功");
	}

}
