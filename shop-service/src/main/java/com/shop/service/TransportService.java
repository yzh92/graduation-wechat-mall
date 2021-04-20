package com.shop.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.bean.model.Transport;
import org.springframework.cache.annotation.CacheEvict;

/**
 *
 * @author yzh
 */
public interface TransportService extends IService<Transport> {

	void insertTransportAndTransfee(Transport transport);

	void updateTransportAndTransfee(Transport transport);

	void deleteTransportAndTransfeeAndTranscity(Long[] ids);

	Transport getTransportAndAllItems(Long transportId);

	@CacheEvict(cacheNames = "TransportAndAllItems", key = "#transportId")
	default void removeTransportAndAllItemsCache(Long transportId){}

}
