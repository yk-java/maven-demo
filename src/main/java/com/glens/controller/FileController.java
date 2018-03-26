package com.glens.controller;

import com.glens.model.AttachmentInfo;
import com.glens.service.impl.AttachmentInfoServiceImpl;
import com.glens.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yk on 2018/1/18.
 *
 * @author yk
 */
@RequestMapping(value = "/file")
@Controller
public class FileController {
    private static Logger logger = LoggerFactory.getLogger(FileController.class);

    private static final String UPLOADPATH = "upload/";
    @Autowired
    private AttachmentInfoServiceImpl attachmentInfoServiceImpl;

    @RequestMapping(value = "/uploadMulti", method = RequestMethod.GET)
    public String showUploadPage2() {
        //view文件夹下的上传多个文件的页面
        return "fileUpload";
    }

    @RequestMapping(value = "/downAttachment", method = RequestMethod.GET)
    public String downAttachment(Map<String, Object> map) {
        List<AttachmentInfo> list = attachmentInfoServiceImpl.selectLists(null);
        map.put("attachment", list);
        //view文件夹下的上传多个文件的页面
        return "allUser";
    }

    /**
     * 文件上传功能
     *
     * @param file MultipartFile
     * @throws IOException 异常
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(MultipartFile file, HttpServletRequest request) throws IOException {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String fileName = file.getOriginalFilename();
        File dir = new File(path, fileName);
        if (!dir.exists()) {
            Boolean b = dir.mkdirs();
        }
        //MultipartFile自带的解析方法
        file.transferTo(dir);
    }

    @RequestMapping(value = "/filesUpload", method = RequestMethod.POST)
    public void filesUpload(HttpServletRequest request, @RequestParam("files") MultipartFile[] files) {
        //判断file数组不能为空并且长度大于0
        if (files != null && files.length > 0) {
            //循环获取file数组中得文件
            for (MultipartFile file : files) {
                //保存文件
                try {
                    saveFile(file, request);
                } catch (IOException e) {
                    logger.error("文件保存异常");
                    e.printStackTrace();
                }
            }
        }
    }

    /***
     * 保存文件
     * @param file MultipartFile
     *
     */
    private void saveFile(MultipartFile file, HttpServletRequest request) throws IOException {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            AttachmentInfo attachmentInfo = new AttachmentInfo();
            // 文件保存路径
            String path = request.getSession().getServletContext().getRealPath("upload");
            String fileName = file.getOriginalFilename();
            String[] str = fileName.split("\\.");
            String fileType = str[str.length - 1];
            attachmentInfo.setAttachSavePath(UPLOADPATH + fileName);
            attachmentInfo.setAttachSaveName(fileName);
            attachmentInfo.setAttachType(fileType);
            String dateTiem = DateTimeUtils.dateToStr(new Date());
            attachmentInfo.setAttachSaveTime(DateTimeUtils.strToDateLong(dateTiem));
            attachmentInfoServiceImpl.insertSelective(attachmentInfo);
            File dir = new File(path, fileName);
            if (!dir.exists()) {
                Boolean b = dir.mkdirs();
            }
            //MultipartFile自带的解析方法
            file.transferTo(dir);
        }
    }

    /**
     * 文件下载功能
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception 异常
     */
    @RequestMapping(value = "/down/{id}", method = RequestMethod.GET)
    public void down(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Integer id) throws Exception {

        AttachmentInfo attachmentInfo = attachmentInfoServiceImpl.selectByPrimaryKey(id);
        String path = attachmentInfo.getAttachSavePath();
        //模拟文件，myFile.txt为需要下载的文件
        String filePath = request.getSession().getServletContext().getRealPath(path);
        //获取输入流
        InputStream bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
        //假如以中文名下载的话
        String[] str = path.split("/");
        String fileName = str[str.length - 1];
        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename="
                .concat(new String(fileName.getBytes("gbk"), "iso-8859-1")));
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len;
        while ((len = bis.read()) != -1) {
            out.write(len);
            out.flush();
        }
        out.close();
    }
}
