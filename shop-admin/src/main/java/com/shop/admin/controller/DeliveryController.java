package com.shop.admin.controller;

import com.shop.bean.model.Delivery;
import com.shop.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author yzh
 */
@RestController
@RequestMapping("/admin/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

	/**
	 * 分页获取
	 */
    @GetMapping("/list")
	public ResponseEntity<List<Delivery>> page(){
		
		List<Delivery> list = deliveryService.list();
		return ResponseEntity.ok(list);
	}

}
