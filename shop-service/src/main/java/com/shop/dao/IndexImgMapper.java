package com.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.bean.model.IndexImg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yzh
 * Date:2021/4/20
 */
public interface IndexImgMapper extends BaseMapper<IndexImg> {
    void deleteIndexImgsByIds(@Param("ids") Long[] ids);

    List<IndexImg> listIndexImgs();
}
