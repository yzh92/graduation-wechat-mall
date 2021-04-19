package com.shop.security.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.security.model.AppConnect;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author yzh
 */
public interface AppConnectMapper extends BaseMapper<AppConnect> {
	/**
	 *
	 * @param bizUserId openid
	 * @param appId appid
	 * @return Result<XxxxDO>
	 */
	AppConnect getByBizUserId(@Param("bizUserId") String bizUserId, @Param("appId") Integer appId);

	AppConnect getByUserId(@Param("userId") String userId, @Param("appId") Integer appId);

    String getUserIdByUnionId(@Param("bizUnionId") String bizUnionId);
}