package com.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yzh
 */
@Data
@TableName("tz_category_prop")
public class CategoryProp implements Serializable {
    @TableId

    private Long id;

    /**
     * 分类id
     */

    private Long categoryId;

    /**
     * 商品属性id即表tz_prod_prop中的prop_id
     */

    private Long propId;
}