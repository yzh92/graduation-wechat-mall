package com.shop.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.bean.app.dto.NoticeDto;
import com.shop.bean.model.Notice;

import java.util.List;

/**
 * 公告管理
 *
 * @author yzh
 */
public interface NoticeService extends IService<Notice> {

    List<Notice> listNotice();

    void removeNoticeList();

    Page<NoticeDto> pageNotice(Page<NoticeDto> page);

    Notice getNoticeById(Long noticeId);

    void removeNoticeById(Long noticeId);
}
