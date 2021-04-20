package com.shop.common.config;

import cn.hutool.crypto.symmetric.AES;
import com.shop.common.bean.ALiDaYu;
import com.shop.common.bean.Qiniu;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 商城bean配置
 * @author yzh
 * Date:2021/4/20
 */
@Configuration
@AllArgsConstructor
public class ShopBeanConfig {

	private final ShopBasicConfig shopBasicConfig;

    @Bean
    public Qiniu qiniu() {
    	return shopBasicConfig.getQiniu();
    }

    @Bean
    public AES tokenAes() {
    	return new AES(shopBasicConfig.getTokenAesKey().getBytes());
    }

    @Bean
    public ALiDaYu aLiDaYu () {
    	return shopBasicConfig.getALiDaYu();
    }
}
