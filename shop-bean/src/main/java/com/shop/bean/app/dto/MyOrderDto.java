package com.shop.bean.app.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yzh
 */
@Data
@ApiModel("我的订单")
public class MyOrderDto {
	
	@ApiModelProperty(value = "订单项",required=true)
	private List<MyOrderItemDto> orderItemDtos;
	
	@ApiModelProperty(value = "订单号",required=true)
	private String orderNumber;
	
	@ApiModelProperty(value = "总价",required=true)
	private Double actualTotal;
	
	@ApiModelProperty(value = "订单状态",required=true)
	private Integer status;
	
}
