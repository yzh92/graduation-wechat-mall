package com.shop.bean.app.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yzh
 */
@ApiModel(value= "购物车参数")
@Data
public class ShopCartParam {

    @ApiModelProperty(value = "购物项id")
    private Long basketId;

    @ApiModelProperty(value = "活动id,传0则不参与该活动")
    private Long discountId;
}
