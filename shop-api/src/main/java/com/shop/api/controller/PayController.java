package com.shop.api.controller;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.shop.api.config.ApiConfig;
import com.shop.bean.app.param.PayParam;
import com.shop.bean.pay.PayInfoDto;
import com.shop.common.util.Arith;
import com.shop.common.util.IPHelper;
import com.shop.security.service.ShopUser;
import com.shop.security.util.SecurityUtils;
import com.shop.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yzh
 */
@RestController
@RequestMapping("/p/order")
@Api(tags = "订单接口")
@AllArgsConstructor
public class PayController {

    private final PayService payService;

    private final ApiConfig apiConfig;

    private final WxPayService wxMiniPayService;

    /**
     * 支付接口
     */
    @PostMapping("/pay")
    @ApiOperation(value = "根据订单号进行支付", notes = "根据订单号进行支付")
    @SneakyThrows
    public ResponseEntity<WxPayMpOrderResult> pay(@RequestBody PayParam payParam) {
        ShopUser user = SecurityUtils.getUser();
        String userId = user.getUserId();
        String openId = user.getBizUserId();


        PayInfoDto payInfo = payService.pay(userId, payParam);

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody(payInfo.getBody());
        orderRequest.setOutTradeNo(payInfo.getPayNo());
        orderRequest.setTotalFee((int) Arith.mul(payInfo.getPayAmount(), 100));
        orderRequest.setSpbillCreateIp(IPHelper.getIpAddr());
        orderRequest.setNotifyUrl(apiConfig.getDomainName() + "/notice/pay/order");
        orderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);
        orderRequest.setOpenid(openId);

        return ResponseEntity.ok(wxMiniPayService.createOrder(orderRequest));
    }

    /**
     * 普通支付接口
     */
    @PostMapping("/normalPay")
    @ApiOperation(value = "根据订单号进行支付", notes = "根据订单号进行支付")
    @SneakyThrows
    public ResponseEntity<Boolean> normalPay(@RequestBody PayParam payParam) {

        ShopUser user = SecurityUtils.getUser();
        String userId = user.getUserId();
        PayInfoDto pay = payService.pay(userId, payParam);

        // 根据内部订单号更新order settlement
        payService.paySuccess(pay.getPayNo(), "");

        return ResponseEntity.ok(true);
    }
}
