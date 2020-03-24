package com.zyh.hu.controller;

import java.io.File;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zyh.hu.service.requestxml.SHRequest;
import com.zyh.hu.service.responsexml.SHResponse;
import com.zyh.hu.utils.JaxbUtils;
import com.zyh.hu.utils.StringsUtil;

/**
 * 文件上传接口服务层
 * @author HU
 *
 */
@Controller
@RequestMapping("file")
public class FileUpController {

	
	@RequestMapping(value = "fileUpload", method = RequestMethod.POST)
    @ResponseBody
    public String fileUpload(@RequestParam("policy")MultipartFile file){
        String path = "D:\\download";
        File resultFile = new File(path + File.separator + file.getOriginalFilename());
        if (!resultFile.getParentFile().exists()){
            resultFile.getParentFile().mkdirs();
        }
        if (null != file){
            System.out.println("文件名：" + file.getName());
            System.out.println(file.getOriginalFilename());
            try {
                file.transferTo(resultFile);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "Success";
    }
	
	@RequestMapping(value = "returnRes", method = RequestMethod.POST)
    @ResponseBody
	public SHResponse returnRes(@RequestParam("requestMessage")String req){
		Object obj = null;
		if (StringsUtil.isNotBlank(req)){
			System.out.println(req);
			JaxbUtils jb = new JaxbUtils(SHResponse.class);
			obj = jb.fromXml(req);
			System.out.println(obj);
		}
		return null;
		
	}
}
