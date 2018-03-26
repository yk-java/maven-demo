package com.glens.controller;

import com.glens.excel.ExcelUtils;
import com.glens.model.DistributionNetworkReport;
import com.glens.model.UserInfo;
import com.glens.service.UserInfoService;
import com.glens.utils.ConvertUtils;
import com.glens.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yk on 2018/1/20.
 * @author yk
 */
@RequestMapping(value = "/excel")
@Controller
public class ExcelController {
    private static Logger logger = LoggerFactory.getLogger(ExcelController.class);
    @Autowired
    private UserInfoService userInfoService;
    /**
     * 用户详细信息导出
     */
    @RequestMapping(value = "/downUser",method = RequestMethod.GET)
    public void listUser(HttpServletResponse response){
        List<UserInfo> userList = userInfoService.findAll();
        String fileName = "用户信息_" + DateTimeUtils.dateToStr(new Date());
        ExcelUtils.writeExcel(response,fileName,userList,UserInfo.class);
       logger.info("导出成功");
    }

    /**
     * 储备报表详细信息导出
     */
    @RequestMapping(value = "/downDistributionNetworkReport",method = RequestMethod.GET)
    public void listDistribution(HttpServletRequest request,HttpServletResponse response){
        String templateFile = "template/pwreport.txt";
        String filePath = request.getSession().getServletContext().getRealPath("/")
                + templateFile;
        List<DistributionNetworkReport> list = new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            DistributionNetworkReport report = new DistributionNetworkReport();
            report.setReserveRequirements(ConvertUtils.intToBigDecimal(i));
            report.setCapital(ConvertUtils.intToBigDecimal(i));
            report.setCost(ConvertUtils.intToBigDecimal(i));
            report.setEvaluationAmount(ConvertUtils.intToBigDecimal(i));
            report.setGridReserveAmount(ConvertUtils.intToBigDecimal(i));
            report.setGridReserveQuantity(i);
            report.setNogridReserveAmount(ConvertUtils.intToBigDecimal(i));
            report.setNogridReserveQuantity(i);
            report.setPassRate(ConvertUtils.intToBigDecimal(i));
            report.setReportProportion(ConvertUtils.intToBigDecimal(i));
            report.setReserveRatio(ConvertUtils.intToBigDecimal(i));
            report.setReviewRatio(ConvertUtils.intToBigDecimal(i));
            report.setThroughAmount(ConvertUtils.intToBigDecimal(i));
            report.setTotal(ConvertUtils.intToBigDecimal(i));
            report.setReportProportion(ConvertUtils.intToBigDecimal(i));
            list.add(report);
        }
        ExcelUtils.downExcelByTemplate(filePath,response,list, DistributionNetworkReport.class);
        logger.info("导出成功");
    }
}
