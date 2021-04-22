package com.shop.bean.app.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author yzh
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderItemDto extends ProductItemDto implements Serializable {

}