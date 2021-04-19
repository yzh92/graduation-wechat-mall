
package com.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.bean.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {

	User getUserByBizUserId(@Param("appId")Integer appId, @Param("bizUserId")String bizUserId);

	User getUserByUserMail(@Param("userMail") String userMail);
}
