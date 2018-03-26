package com.glens.controller;

import com.glens.model.AttachmentInfo;
import com.glens.service.impl.AttachmentInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by yk on 2018/1/21.
 * @author yk
 */
@RequestMapping(value = "/zip")
@Controller
public class ZipDownController {
    private static final String MSIE = "MSIE";
    private static final String TRIDENT = "Trident";

    @Autowired
    private AttachmentInfoServiceImpl attachmentInfoServiceImpl;

    @RequestMapping(value = "/downloadFiles",method = RequestMethod.GET)
    @ResponseBody
    public void downloadFiles(HttpServletRequest request, HttpServletResponse response){

        //响应头的设置
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");

        //设置压缩包的名字
        //解决不同浏览器压缩包名字含有中文时乱码的问题
        String downloadName = "我是压缩包的名字.zip";
        String agent = request.getHeader("USER-AGENT");
        downloadName = ExportPgReportWord.getString(downloadName, agent, MSIE, TRIDENT);
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + downloadName + "\"");

        //设置压缩流：直接写入response，实现边压缩边下载
        ZipOutputStream zipos = null;
        try {
            zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
            //设置压缩方法
            zipos.setMethod(ZipOutputStream.DEFLATED);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<AttachmentInfo> list = attachmentInfoServiceImpl.selectLists(null);

        //循环将文件写入压缩流
        DataOutputStream os = null;
        for(int i = 0; i < list.size(); i++ ){
            String path = list.get(i).getAttachSavePath();
            //模拟文件，myFile.txt为需要下载的文件
            String filePath = request.getSession().getServletContext().getRealPath(path);
            File file = new File(filePath);
            try {
                //添加ZipEntry，并ZipEntry中写入文件流
                //这里，加上i是防止要下载的文件有重名的导致下载失败
                if (zipos != null) {
                    zipos.putNextEntry(new ZipEntry(i + list.get(i).getAttachSaveName()));
                }
                os = new DataOutputStream(zipos);
                InputStream is = new FileInputStream(file);
                byte[] b = new byte[100];
                int length;
                while((length = is.read(b))!= -1){
                    os.write(b, 0, length);
                }
                is.close();
                if (zipos != null) {
                    zipos.closeEntry();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //关闭流
        ExportPgReportWord.closeStream(zipos, os);
    }
}
