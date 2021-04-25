package com.shop.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shop.bean.model.ProdComm;
import com.shop.common.annotation.SysLog;
import com.shop.common.util.PageParam;
import com.shop.service.ProdCommService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * 商品评论
 *
 * @author yzh
 */
@RestController
@AllArgsConstructor
@RequestMapping("/prod/prodComm" )
public class ProdCommController {

    private final ProdCommService prodCommService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param prodComm 商品评论
     * @return 分页数据
     */
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('prod:prodComm:page')" )
    public ResponseEntity<IPage<ProdComm>> getProdCommPage(PageParam page, ProdComm prodComm) {
        return ResponseEntity.ok(prodCommService.getProdCommPage(page,prodComm));
    }


    /**
     * 通过id查询商品评论
     * @param prodCommId id
     * @return 单个数据
     */
    @GetMapping("/info/{prodCommId}" )
    @PreAuthorize("@pms.hasPermission('prod:prodComm:info')" )
    public ResponseEntity<ProdComm> getById(@PathVariable("prodCommId" ) Long prodCommId) {
        return ResponseEntity.ok(prodCommService.getById(prodCommId));
    }

    /**
     * 新增商品评论
     * @param prodComm 商品评论
     * @return 是否新增成功
     */
    @SysLog("新增商品评论" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('prod:prodComm:save')" )
    public ResponseEntity<Boolean> save(@RequestBody @Valid ProdComm prodComm) {
        return ResponseEntity.ok(prodCommService.save(prodComm));
    }

    /**
     * 修改商品评论
     * @param prodComm 商品评论
     * @return 是否修改成功
     */
    @SysLog("修改商品评论" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('prod:prodComm:update')" )
    public ResponseEntity<Boolean> updateById(@RequestBody @Valid ProdComm prodComm) {
        prodComm.setReplyTime(new Date());
        return ResponseEntity.ok(prodCommService.updateById(prodComm));
    }

    /**
     * 通过id删除商品评论
     * @param prodCommId id
     * @return 是否删除成功
     */
    @SysLog("删除商品评论" )
    @DeleteMapping("/{prodCommId}" )
    @PreAuthorize("@pms.hasPermission('prod:prodComm:delete')" )
    public ResponseEntity<Boolean> removeById(@PathVariable Long prodCommId) {
        return ResponseEntity.ok(prodCommService.removeById(prodCommId));
    }

}
