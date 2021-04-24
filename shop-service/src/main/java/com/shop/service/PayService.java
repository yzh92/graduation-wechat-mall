package com.shop.service;

import com.shop.bean.app.param.PayParam;
import com.shop.bean.pay.PayInfoDto;

import java.util.List;

/**
 * @author yzh
 */
public interface PayService {


    PayInfoDto pay(String userId, PayParam payParam);

    List<String> paySuccess(String payNo, String bizPayNo);

}
