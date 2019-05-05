package com.likuncheng.core.entity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

@Data
public class PayBean {

	private Integer id;                 // 数据库标识
	private String outTradeNo;          // 数据库订单id
	private String totalAmount;         // 支付金额
	private String subject;             // 支付标题
	private String body;                // 支付描述
	private String tradeNo;             // 支付宝交易号
	private String createTime;          // 创建的时间

	public PayBean() {
	}
	
	public PayBean(String outTradeNo, String totalAmount, String subject, String body) throws UnsupportedEncodingException {
		//这里设置了这个 字体的格式 为的就是后面的验签 能够避免因为字体格式问题导致验签失败
		//在我没加上这个的时候 验签是失败了的
		this.outTradeNo = new String(outTradeNo.getBytes("ISO-8859-1"),"UTF-8");
		this.totalAmount = new String(totalAmount.getBytes("ISO-8859-1"),"UTF-8");
		this.subject = new String(subject.getBytes("ISO-8859-1"),"UTF-8");
		this.body = new String(body.getBytes("ISO-8859-1"),"UTF-8");
		//这样就可以避免不必要的 插入到数据的错误
		this.tradeNo = "-";
		this.createTime = "-";
	}
	
	//这个是为了方便修改支付记录信息的 因为我们之前保存数据库的时候保存了前面四个属性 这里就不用传递前面的参数了 
	//只需要在内部保存创建的时间就好 然后还需要传入被修改的条件 订单号
	public PayBean(String outTradeNo,String tradeNo) {
		this.outTradeNo = outTradeNo;
		this.tradeNo = tradeNo;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newCreateTime = sf.format(new Date());
		this.createTime = newCreateTime;
	}

	public PayBean(String outTradeNo, String totalAmount, String subject, String body, String tradeNo) {
		this.outTradeNo = outTradeNo;
		this.totalAmount = totalAmount;
		this.subject = subject;
		this.body = body;
		this.tradeNo = tradeNo;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newCreateTime = sf.format(new Date());
		this.createTime = newCreateTime;

	}

}
