package com.shop.common.bean;

import com.shop.common.enums.QiniuZone;
import lombok.Data;

/**
 * 七牛云存储配置信息
 * @author yzh
 */
@Data
public class Qiniu {

	private String accessKey;

	private String secretKey;

	private String bucket;

	private String resourcesUrl;

	private QiniuZone zone;
}
