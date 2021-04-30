package com.shop.service;

import java.io.IOException;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.bean.model.AttachFile;

/**
 *
 * @author yzh
 */
public interface AttachFileService extends IService<AttachFile> {

	String uploadFile(byte[] bytes,String originalName) throws IOException;
	
	void deleteFile(String fileName);
}
