package com.glens.controller;

import com.alibaba.fastjson.JSON;
import com.glens.model.Message;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * Created by yk on 2018/1/20.
 *
 * @author yk
 */
@Controller
@RequestMapping({"/activiti"})
public class ActivitiController {
    @Autowired
    ProcessEngine processEngine;
    @Autowired
    RepositoryService repositoryService;
    @RequestMapping(value = "/uploadZip", method = RequestMethod.GET)
    public String showUploadPage2() {
        //view文件夹下的上传多个文件的页面
        return "activitiProcessList";
    }

    @RequestMapping(value = "/getProcessList", method = RequestMethod.POST)
    @ResponseBody
    public void processList(HttpServletResponse response) {

        List<ProcessDefinition> processDefinitionsList = repositoryService
                .createProcessDefinitionQuery().list();

        Map<String, String> processDefinitionMap;
        List<Map<String, String>> processDefinitionListMaps = new ArrayList<>();
        for (ProcessDefinition processDefinition : processDefinitionsList) {
            processDefinitionMap = new HashMap<>();
            processDefinitionMap.put("id", processDefinition.getId());
            processDefinitionMap.put("deploymentId",
                    processDefinition.getDeploymentId());
            processDefinitionMap.put("name", processDefinition.getName());
            processDefinitionMap.put("key", processDefinition.getKey());
            processDefinitionMap.put("version",
                    String.valueOf(processDefinition.getVersion()));
            processDefinitionMap.put("resourceName",
                    processDefinition.getResourceName());
            processDefinitionMap.put("diagramResourceName",
                    processDefinition.getDiagramResourceName());
            processDefinitionListMaps.add(processDefinitionMap);
        }
        String strJson;
        strJson = JSON.toJSONString(processDefinitionListMaps);
        try {
            response.getWriter().print(strJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    @ResponseBody
    public void deploy(MultipartHttpServletRequest request, HttpServletResponse response) {
        Message result = new Message();

        MultipartFile file = request.getFile("fileInfo");

        String fileName = file.getOriginalFilename();
        try {
            InputStream filInputStream = file.getInputStream();

            String extension = FilenameUtils.getExtension(fileName);

            DeploymentBuilder deBuilder = repositoryService.createDeployment();
            if ((extension.equals("zip")) || (extension.equals("bar"))) {
                ZipInputStream zip = new ZipInputStream(filInputStream);
                deBuilder.addZipInputStream(zip);
            } else {
                deBuilder.addInputStream(fileName, filInputStream);
            }
            deBuilder.deploy();
            result.setResult(true);
        } catch (Exception e) {
            result.setResult(false);
            e.printStackTrace();
        }
        try {
            response.getWriter().print(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/delDeploy", method = RequestMethod.POST)
    @ResponseBody
    public void delDeploy(HttpServletRequest request, HttpServletResponse response) {
        Message result = new Message();

        String deploymentId = request.getParameter("deploymentId");

        try {
            repositoryService.deleteDeployment(deploymentId, true);
            result.setResult(true);
        } catch (Exception e) {
            result.setResult(false);
            e.printStackTrace();
        }
        try {
            response.getWriter().print(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
