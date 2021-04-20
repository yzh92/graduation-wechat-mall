package com.shop.bean.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value= "设置用户信息")
public class UserRegisterParam {

	@ApiModelProperty(value = "密码")
	private String password;

	@ApiModelProperty(value = "邮箱")
	private String userMail;

	@ApiModelProperty(value = "昵称")
	private String nickName;

	@ApiModelProperty(value = "用户名")
	private String userName;

	@ApiModelProperty(value = "应用类型 1小程序 2微信公众号 3 PC 4 h5")
	private Integer appType;
}
