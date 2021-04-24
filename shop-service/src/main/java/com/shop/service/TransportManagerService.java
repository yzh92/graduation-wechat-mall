package com.shop.service;

import com.shop.bean.app.dto.ProductItemDto;
import com.shop.bean.model.UserAddr;

/**
 * @author yzh  运费管理service
 */
public interface TransportManagerService {

	Double calculateTransfee(ProductItemDto productItem, UserAddr userAddr);
}
