/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.shop.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * 商城配置文件
 * @author yzh
 */
@Data
@Component
@PropertySource("classpath:admin.properties")
@ConfigurationProperties(prefix = "admin")
public class AdminConfig {

	/**
	 * 数据中心ID
	 */
	private Integer datacenterId;

	/**
	 * 终端ID
	 */
	private Integer workerId;

}
