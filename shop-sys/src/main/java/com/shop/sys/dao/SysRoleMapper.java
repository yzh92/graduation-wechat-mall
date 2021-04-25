package com.shop.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.sys.model.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色管理
 * @author yzh
 */
public interface SysRoleMapper extends BaseMapper<SysRole>{

	void deleteBatch(@Param("roleIds") Long[] roleIds);

	List<Long> listRoleIdByUserId(Long userId);

}
