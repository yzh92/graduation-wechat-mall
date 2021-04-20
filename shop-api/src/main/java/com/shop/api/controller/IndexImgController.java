package com.shop.api.controller;

import com.shop.bean.app.dto.IndexImgDto;
import com.shop.bean.model.IndexImg;
import com.shop.service.IndexImgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yzh
 * Date:2021/4/20
 */
@RestController
@Api(tags = "首页轮播图接口")
public class IndexImgController {
    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private IndexImgService indexImgService;

    /**
     * 首页轮播图接口
     */
    @GetMapping("/indexImgs")
    @ApiOperation(value = "首页轮播图",notes = "获取首页轮播图列表信息")
    public ResponseEntity<List<IndexImgDto>> indexImgs(){
        List<IndexImg> indexImgList = indexImgService.listIndexImgs();
        List<IndexImgDto> indexImgDtos = mapperFacade.mapAsList(indexImgList,IndexImgDto.class);
        return ResponseEntity.ok(indexImgDtos);
    }

}
