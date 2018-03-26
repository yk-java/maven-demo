package com.glens.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glens.exportword.CustomXWPFDocument;
import com.glens.exportword.WordUtil;
import com.glens.model.GPlanCbxmWithBLOBs;
import com.glens.service.GPlanCbxmService;
import com.glens.utils.DateTimeUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.glens.utils.FileUtils.delAllFile;


@Controller
@RequestMapping("/exportWord")
public class ExportPgReportWord {
    private static final String MSIE = "MSIE";
    private static final String TRIDENT = "Trident";

    @Resource
    private GPlanCbxmService gPlanCbxmService;

    @RequestMapping(value = "/exportWordDoc" , method = RequestMethod.POST)
    public void exportWordDoc(HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        String result = "";
        response.setHeader("content-disposition", "attachment;filename="
                + new SimpleDateFormat("yyyyMMddHH:mm:ss").format(new Date())
                + ".docx");
        String prjId = request.getParameter("prjId");
        String dowPatch = request.getSession().getServletContext().getRealPath("/file/cache/");
        List<GPlanCbxmWithBLOBs> lists = null;
        GPlanCbxmWithBLOBs gPlanCbxmWithBLOBs = new GPlanCbxmWithBLOBs();
        gPlanCbxmWithBLOBs.setPrjid(prjId);
        gPlanCbxmWithBLOBs.setStatus("评估通过");
        lists = gPlanCbxmService.selectByParameter(gPlanCbxmWithBLOBs);
        if (lists.size() > 0) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("${doc_prjName}", String.valueOf(lists.get(0).getPrjname()));
            param.put("${doc_prjLocation}", String.valueOf(lists.get(0).getProjectlocation()));
            param.put("${doc_prjContent}", String.valueOf(lists.get(0).getBuildInfo()));
            param.put("${doc_prjFees}", String.valueOf(lists.get(0).getTotalfee()));
            param.put("${doc_prjPlnr}", String.valueOf(lists.get(0).getPlnr()));
            param.put("${doc_prjAddTime}", String.valueOf(lists.get(0).getAdddatetime()));
            param.put("${doc_prjPgnr}", String.valueOf(lists.get(0).getPgnr()));

            Object oldData = lists.get(0).getOlddata();
            Object nowData = lists.get(0).getNowdata();

            JSONObject jsonobjectOld = JSON.parseObject(String.valueOf(oldData));
            JSONObject jsonobjectNow = JSON.parseObject(String.valueOf(nowData));

            Object objOld = jsonobjectOld.get("dataGrid");
            Object objNow = jsonobjectNow.get("dataGrid");

            JSONArray arrayOld = JSON.parseArray(String.valueOf(objOld));
            JSONArray arrayNow = JSON.parseArray(String.valueOf(objNow));

            List<Map<String, List<String>>> tabListMaps = new ArrayList<Map<String, List<String>>>();
            Map<String, List<String>> tabMaps = null;
            List<String> tabList = null;
            String bdzname = "";
            String xlname = "";
            Object type = jsonobjectOld.get("type");
            String filePath = "";
            filePath = getFilePath(request, type);

            for (int i = 0; i < arrayOld.size(); i++) {
                if (i > 0) {
                    continue;
                }
                JSONObject objtmp = (JSONObject) arrayOld.get(i);
                if (!objtmp.isEmpty()) {
                    tabList = new ArrayList<String>();
                    bdzname = objtmp.get("BDZNAME").toString();
                    xlname = ObjectUtils.toString(objtmp.get("XLNAME"), "");
                    tabList.add(String.valueOf(objtmp.get("BDZNAME")));
                    tabList.add(String.valueOf(objtmp.get("XLNAME")));
                    if ("xl".equals(type) || type == null) {
                        tabList.add(String.valueOf(objtmp.get("XLCD")));
                        tabList.add(String.valueOf(objtmp.get("GDBJ")));
                        tabList.add(String.valueOf(objtmp.get("TotalZJRL")));
                        tabList.add(String.valueOf(objtmp.get("YearZDFH")));
                        tabList.add(String.valueOf(objtmp.get("XLFZL")));
                        tabList.add(String.valueOf(objtmp.get("JKWLLKG")));
                        tabList.add(String.valueOf(objtmp.get("FDNUM")));
                        tabList.add(String.valueOf(objtmp.get("")));
                    } else if ("pb".equals(type)) {
                        tabList.add(String.valueOf(objtmp.get("PBNAME")));
                        tabList.add(String.valueOf(objtmp.get("RL")));
                        tabList.add(String.valueOf(objtmp.get("GDBJ")));
                        tabList.add(String.valueOf(objtmp.get("HS")));
                        tabList.add(String.valueOf(objtmp.get("HJRL")));
                        tabList.add(String.valueOf(objtmp.get("FZL")));
                    }
                    tabMaps = new HashMap<String, List<String>>();
                    tabMaps.put("${tb1}", tabList);
                    tabListMaps.add(tabMaps);
                }
            }
            for (int i = 0; i < arrayNow.size(); i++) {
                if (i > 0) {
                    continue;
                }
                JSONObject objtmp = (JSONObject) arrayNow.get(i);
                if (!objtmp.isEmpty()) {
                    tabList = new ArrayList<String>();
                    tabList.add(String.valueOf(objtmp.get("BDZNAME")));
                    tabList.add(String.valueOf(objtmp.get("XLNAME")));
                    if ("xl".equals(type) || type == null) {
                        tabList.add(String.valueOf(objtmp.get("XLCD")));
                        tabList.add(String.valueOf(objtmp.get("GDBJ")));
                        tabList.add(String.valueOf(objtmp.get("TotalZJRL")));
                        tabList.add(String.valueOf(objtmp.get("YearZDFH")));
                        tabList.add(String.valueOf(objtmp.get("XLFZL")));
                        tabList.add(String.valueOf(objtmp.get("JKWLLKG")));
                        tabList.add(String.valueOf(objtmp.get("FDNUM")));
                        tabList.add(String.valueOf(objtmp.get("")));
                    } else if ("pb".equals(type)) {
                        tabList.add(String.valueOf(objtmp.get("PBNAME")));
                        tabList.add(String.valueOf(objtmp.get("RL")));
                        tabList.add(String.valueOf(objtmp.get("GDBJ")));
                        tabList.add(String.valueOf(objtmp.get("HS")));
                        tabList.add(String.valueOf(objtmp.get("HJRL")));
                        tabList.add(String.valueOf(objtmp.get("FZL")));
                    }

                    tabMaps = new HashMap<String, List<String>>();
                    tabMaps.put("${tb2}", tabList);
                    tabListMaps.add(tabMaps);
                }
            }

            for (int i = 0; i < arrayOld.size(); i++) {
                if (i == 0) {
                    continue;
                }
                JSONObject objtmp = (JSONObject) arrayOld.get(i);
                JSONObject objtmp2 = (JSONObject) arrayNow.get(i);
                if (!objtmp.isEmpty()) {
                    tabList = new ArrayList<String>();
                    tabList.add(bdzname);
                    tabList.add(xlname);
                    tabList.add(ObjectUtils.toString(objtmp.get("ValueName"), ""));
                    tabList.add(ObjectUtils.toString(objtmp.get("TitleType"), ""));
                    tabList.add(ObjectUtils.toString(objtmp.get("ms1"), ""));
                    tabList.add(ObjectUtils.toString(objtmp.get("ProblemValue"), ""));
                    tabList.add(ObjectUtils.toString(objtmp.get("isFin1"), ""));
                    tabList.add(ObjectUtils.toString(objtmp2.get("ValueName2"), ""));
                    tabList.add(ObjectUtils.toString(objtmp2.get("TitleType2"), ""));
                    tabList.add(ObjectUtils.toString(objtmp2.get("ms12"), ""));
                    tabList.add(ObjectUtils.toString(objtmp2.get("ProblemValue2"), ""));
                    tabList.add(ObjectUtils.toString(objtmp2.get("isFin12"), ""));
                    tabMaps = new HashMap<String, List<String>>();
                    tabMaps.put("${tb3}", tabList);
                    tabListMaps.add(tabMaps);
                }
            }

            CustomXWPFDocument doc = WordUtil.generateWord(param, filePath,
                    tabListMaps);
            FileOutputStream fopts = new FileOutputStream(dowPatch
                    + lists.get(0).getPrjname().replace("/", "") + DateTimeUtils.dateToStr(new Date()) + ".docx");
            doc.write(fopts);
            fopts.close();

            result = "存在附件";
        } else {
            result = "不存在附件";
        }

        PrintWriter writer;
        try {
            writer = response.getWriter();
            writer.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //根据条件判断模板
    private String getFilePath(HttpServletRequest request, Object type) {
        String filePath;
        if ("pb".equals(type)) {
            filePath = request.getSession().getServletContext().getRealPath("/") + "template/pgReprotTemplet_pb.docx";
        } else if ("xl".equals(type)) {
            filePath = request.getSession().getServletContext().getRealPath("/") + "template/pgReprotTemplet.docx";
        } else {
            filePath = request.getSession().getServletContext().getRealPath("/") + "template/pgReprotTemplet.docx";
        }
        return filePath;
    }

    @RequestMapping(value = "/exportWordDocs", method = RequestMethod.POST)
    public void exportWordDocs(HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        String result = "";
        PrintWriter writer;
        writer = response.getWriter();
        String belongs = request.getParameter("belongs");
        String prjids = request.getParameter("prjids");
        String[] prjidList = prjids.split(",");
        String[] belongList = belongs.split(",");
        List<String> list = Arrays.asList(prjidList);
        List<String> list1 = Arrays.asList(belongList);
        String prjId = "";
        String belong = "";
        String dowPatch = request.getSession().getServletContext().getRealPath("/file/cache/");
        delAllFile(dowPatch);
        for (int i = 0; i < list.size(); i++) {
            prjId = list.get(i);
            belong = list1.get(i);
            List<GPlanCbxmWithBLOBs> lists = null;
            GPlanCbxmWithBLOBs gPlanCbxmWithBLOBs = new GPlanCbxmWithBLOBs();
            gPlanCbxmWithBLOBs.setPrjid(prjId);
            gPlanCbxmWithBLOBs.setStatus("评估通过");
            lists = gPlanCbxmService.selectByParameter(gPlanCbxmWithBLOBs);
            if (lists.size() > 0) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("${doc_prjName}", String.valueOf(lists.get(0).getPrjname()));
                param.put("${doc_prjLocation}", String.valueOf(lists.get(0).getProjectlocation()));
                param.put("${doc_prjContent}", String.valueOf(lists.get(0).getBuildInfo()));
                param.put("${doc_prjFees}", String.valueOf(lists.get(0).getTotalfee()));
                param.put("${doc_prjPlnr}", String.valueOf(lists.get(0).getPlnr()));
                param.put("${doc_prjAddTime}", String.valueOf(lists.get(0).getAdddatetime()));
                param.put("${doc_prjPgnr}", String.valueOf(lists.get(0).getPgnr()));

                Object oldData = lists.get(0).getOlddata();
                Object nowData = lists.get(0).getNowdata();

                JSONObject jsonobjectOld = JSON.parseObject(String.valueOf(oldData));
                JSONObject jsonobjectNow = JSON.parseObject(String.valueOf(nowData));
                Object objOld = jsonobjectOld.get("dataGrid");
                Object objNow = jsonobjectNow.get("dataGrid");

                JSONArray arrayOld = JSON.parseArray(String.valueOf(objOld));
                JSONArray arrayNow = JSON.parseArray(String.valueOf(objNow));

                List<Map<String, List<String>>> tabListMaps = new ArrayList<Map<String, List<String>>>();
                Map<String, List<String>> tabMaps = null;
                List<String> tabList = null;

                String bdzname = "";
                String xlname = "";
                Object type = jsonobjectOld.get("type");
                String filePath = "";
                filePath = getFilePath(request, type);
                for (int j = 0; j < arrayOld.size(); j++) {
                    if (j > 0) {
                        continue;
                    }
                    JSONObject objtmp = (JSONObject) arrayOld.get(j);
                    if (!objtmp.isEmpty()) {
                        tabList = new ArrayList<String>();
                        bdzname = ObjectUtils.toString(objtmp.get("BDZNAME"), "");
                        xlname = ObjectUtils.toString(objtmp.get("XLNAME"), "");
                        tabList.add(String.valueOf(objtmp.get("BDZNAME")));
                        tabList.add(String.valueOf(objtmp.get("XLNAME")));
                        if ("xl".equals(type) || type == null) {
                            tabList.add(String.valueOf(objtmp.get("XLCD")));
                            tabList.add(String.valueOf(objtmp.get("GDBJ")));
                            tabList.add(String.valueOf(objtmp.get("TotalZJRL")));
                            tabList.add(String.valueOf(objtmp.get("YearZDFH")));
                            tabList.add(String.valueOf(objtmp.get("XLFZL")));
                            tabList.add(String.valueOf(objtmp.get("JKWLLKG")));
                            tabList.add(String.valueOf(objtmp.get("FDNUM")));
                            tabList.add(String.valueOf(objtmp.get("")));
                        } else if ("pb".equals(type)) {
                            tabList.add(String.valueOf(objtmp.get("PBNAME")));
                            tabList.add(String.valueOf(objtmp.get("RL")));
                            tabList.add(String.valueOf(objtmp.get("GDBJ")));
                            tabList.add(String.valueOf(objtmp.get("HS")));
                            tabList.add(String.valueOf(objtmp.get("HJRL")));
                            tabList.add(String.valueOf(objtmp.get("FZL")));
                        }

                        tabMaps = new HashMap<String, List<String>>();
                        tabMaps.put("${tb1}", tabList);
                        tabListMaps.add(tabMaps);
                    }
                }
                for (int j = 0; j < arrayNow.size(); j++) {
                    if (j > 0) {
                        continue;
                    }
                    JSONObject objtmp = (JSONObject) arrayNow.get(j);
                    if (!objtmp.isEmpty()) {
                        tabList = new ArrayList<String>();
                        tabList.add(String.valueOf(objtmp.get("BDZNAME")));
                        tabList.add(String.valueOf(objtmp.get("XLNAME")));
                        if ("xl".equals(type) || type == null) {
                            tabList.add(String.valueOf(objtmp.get("XLCD")));
                            tabList.add(String.valueOf(objtmp.get("GDBJ")));
                            tabList.add(String.valueOf(objtmp.get("TotalZJRL")));
                            tabList.add(String.valueOf(objtmp.get("YearZDFH")));
                            tabList.add(String.valueOf(objtmp.get("XLFZL")));
                            tabList.add(String.valueOf(objtmp.get("JKWLLKG")));
                            tabList.add(String.valueOf(objtmp.get("FDNUM")));
                            tabList.add(String.valueOf(objtmp.get("")));
                        } else if ("pb".equals(type)) {
                            tabList.add(String.valueOf(objtmp.get("PBNAME")));
                            tabList.add(String.valueOf(objtmp.get("RL")));
                            tabList.add(String.valueOf(objtmp.get("GDBJ")));
                            tabList.add(String.valueOf(objtmp.get("HS")));
                            tabList.add(String.valueOf(objtmp.get("HJRL")));
                            tabList.add(String.valueOf(objtmp.get("FZL")));
                        }

                        tabMaps = new HashMap<String, List<String>>();
                        tabMaps.put("${tb2}", tabList);
                        tabListMaps.add(tabMaps);
                    }
                }

                for (int j = 0; j < arrayOld.size(); j++) {
                    if (j == 0) {
                        continue;
                    }
                    JSONObject objtmp = (JSONObject) arrayOld.get(j);
                    JSONObject objtmp2 = (JSONObject) arrayNow.get(j);
                    if (!objtmp.isEmpty()) {
                        tabList = new ArrayList<String>();
                        tabList.add(bdzname);
                        tabList.add(xlname);
                        tabList.add(ObjectUtils.toString(objtmp.get("ValueName"), ""));
                        tabList.add(ObjectUtils.toString(objtmp.get("TitleType"), ""));
                        tabList.add(ObjectUtils.toString(objtmp.get("ms1"), ""));
                        tabList.add(ObjectUtils.toString(objtmp.get("ProblemValue"), ""));
                        tabList.add(ObjectUtils.toString(objtmp.get("isFin1"), ""));
                        tabList.add(ObjectUtils.toString(objtmp2.get("ValueName2"), ""));
                        tabList.add(ObjectUtils.toString(objtmp2.get("TitleType2"), ""));
                        tabList.add(ObjectUtils.toString(objtmp2.get("ms12"), ""));
                        tabList.add(ObjectUtils.toString(objtmp2.get("ProblemValue2"), ""));
                        tabList.add(ObjectUtils.toString(objtmp2.get("isFin12"), ""));
                        tabMaps = new HashMap<String, List<String>>();
                        tabMaps.put("${tb3}", tabList);
                        tabListMaps.add(tabMaps);
                    }
                }

                CustomXWPFDocument doc = WordUtil.generateWord(param, filePath, tabListMaps);
                FileOutputStream fopts = new FileOutputStream(dowPatch
                        + lists.get(0).getPrjname().replace("/", "") + DateTimeUtils.dateToStr(new Date()) + ".docx");
                doc.write(fopts);
                fopts.close();
                result = "存在附件";
            } else {
                result = "不存在附件";
            }
        }
        try {
            writer.write(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportWordDocsAll(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String dowPatch = request.getSession().getServletContext().getRealPath("/file/cache/");
        delAllFile(dowPatch);
        List<GPlanCbxmWithBLOBs> lists = null;
        GPlanCbxmWithBLOBs gPlanCbxmWithBLOBs = new GPlanCbxmWithBLOBs();
        gPlanCbxmWithBLOBs.setStatus("评估通过");
        lists = gPlanCbxmService.selectByParameter(gPlanCbxmWithBLOBs);
        for (int j = 0; j < lists.size(); j++) {
            if (lists.size() > 0) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("${doc_prjName}", String.valueOf(lists.get(0).getPrjname()));
                param.put("${doc_prjLocation}", String.valueOf(lists.get(0).getProjectlocation()));
                param.put("${doc_prjContent}", String.valueOf(lists.get(0).getBuildInfo()));
                param.put("${doc_prjFees}", String.valueOf(lists.get(0).getTotalfee()));
                param.put("${doc_prjPlnr}", String.valueOf(lists.get(0).getPlnr()));
                param.put("${doc_prjAddTime}", String.valueOf(lists.get(0).getAdddatetime()));
                param.put("${doc_prjPgnr}", String.valueOf(lists.get(0).getPgnr()));

                Object oldData = lists.get(0).getOlddata();
                Object nowData = lists.get(0).getNowdata();

                JSONObject jsonobjectOld = JSON.parseObject(String.valueOf(oldData));
                JSONObject jsonobjectNow = JSON.parseObject(String.valueOf(nowData));

                Object objOld = jsonobjectOld.get("dataGrid");
                Object objNow = jsonobjectNow.get("dataGrid");

                JSONArray arrayOld = JSON.parseArray(String.valueOf(objOld));
                JSONArray arrayNow = JSON.parseArray(String.valueOf(objNow));

                List<Map<String, List<String>>> tabListMaps = new ArrayList<Map<String, List<String>>>();
                Map<String, List<String>> tabMaps = null;
                List<String> tabList = null;

                String bdzname = "";
                String xlname = "";
                Object type = jsonobjectOld.get("type");
                String filePath = "";
                filePath = getFilePath(request, type);
                for (int i = 0; i < arrayOld.size(); i++) {
                    if (i > 0) {
                        continue;
                    }
                    JSONObject objtmp = (JSONObject) arrayOld.get(i);
                    if (!objtmp.isEmpty()) {
                        tabList = new ArrayList<String>();
                        bdzname = ObjectUtils.toString(objtmp.get("BDZNAME"), "");
                        xlname = ObjectUtils.toString(objtmp.get("XLNAME"), "");
                        tabList.add(String.valueOf(objtmp.get("BDZNAME")));
                        tabList.add(String.valueOf(objtmp.get("XLNAME")));
                        if ("xl".equals(type) || type == null) {
                            tabList.add(String.valueOf(objtmp.get("XLCD")));
                            tabList.add(String.valueOf(objtmp.get("GDBJ")));
                            tabList.add(String.valueOf(objtmp.get("TotalZJRL")));
                            tabList.add(String.valueOf(objtmp.get("YearZDFH")));
                            tabList.add(String.valueOf(objtmp.get("XLFZL")));
                            tabList.add(String.valueOf(objtmp.get("JKWLLKG")));
                            tabList.add(String.valueOf(objtmp.get("FDNUM")));
                            tabList.add(String.valueOf(objtmp.get("")));
                        } else if ("pb".equals(type)) {
                            tabList.add(String.valueOf(objtmp.get("PBNAME")));
                            tabList.add(String.valueOf(objtmp.get("RL")));
                            tabList.add(String.valueOf(objtmp.get("GDBJ")));
                            tabList.add(String.valueOf(objtmp.get("HS")));
                            tabList.add(String.valueOf(objtmp.get("HJRL")));
                            tabList.add(String.valueOf(objtmp.get("FZL")));
                        }

                        tabMaps = new HashMap<String, List<String>>();
                        tabMaps.put("${tb1}", tabList);
                        tabListMaps.add(tabMaps);
                    }
                }
                for (int i = 0; i < arrayNow.size(); i++) {
                    if (i > 0) {
                        continue;
                    }
                    JSONObject objtmp = (JSONObject) arrayNow.get(i);
                    if (!objtmp.isEmpty()) {
                        tabList = new ArrayList<String>();
                        tabList.add(String.valueOf(objtmp.get("BDZNAME")));
                        tabList.add(String.valueOf(objtmp.get("XLNAME")));
                        if ("xl".equals(type) || type == null) {
                            tabList.add(String.valueOf(objtmp.get("XLCD")));
                            tabList.add(String.valueOf(objtmp.get("GDBJ")));
                            tabList.add(String.valueOf(objtmp.get("TotalZJRL")));
                            tabList.add(String.valueOf(objtmp.get("YearZDFH")));
                            tabList.add(String.valueOf(objtmp.get("XLFZL")));
                            tabList.add(String.valueOf(objtmp.get("JKWLLKG")));
                            tabList.add(String.valueOf(objtmp.get("FDNUM")));
                            tabList.add(String.valueOf(objtmp.get("")));
                        } else if ("pb".equals(type)) {
                            tabList.add(String.valueOf(objtmp.get("PBNAME")));
                            tabList.add(String.valueOf(objtmp.get("RL")));
                            tabList.add(String.valueOf(objtmp.get("GDBJ")));
                            tabList.add(String.valueOf(objtmp.get("HS")));
                            tabList.add(String.valueOf(objtmp.get("HJRL")));
                            tabList.add(String.valueOf(objtmp.get("FZL")));
                        }

                        tabMaps = new HashMap<String, List<String>>();
                        tabMaps.put("${tb2}", tabList);
                        tabListMaps.add(tabMaps);
                    }
                }

                for (int i = 0; i < arrayOld.size(); i++) {
                    if (i == 0) {
                        continue;
                    }
                    JSONObject objtmp = (JSONObject) arrayOld.get(i);
                    JSONObject objtmp2 = (JSONObject) arrayNow.get(i);
                    if (!objtmp.isEmpty()) {
                        tabList = new ArrayList<String>();
                        tabList.add(bdzname);
                        tabList.add(xlname);
                        tabList.add(ObjectUtils.toString(objtmp.get("ValueName"), ""));
                        tabList.add(ObjectUtils.toString(objtmp.get("TitleType"), ""));
                        tabList.add(ObjectUtils.toString(objtmp.get("ms1"), ""));
                        tabList.add(ObjectUtils.toString(objtmp.get("ProblemValue"), ""));
                        tabList.add(ObjectUtils.toString(objtmp.get("isFin1"), ""));
                        tabList.add(ObjectUtils.toString(objtmp2.get("ValueName2"), ""));
                        tabList.add(ObjectUtils.toString(objtmp2.get("TitleType2"), ""));
                        tabList.add(ObjectUtils.toString(objtmp2.get("ms12"), ""));
                        tabList.add(ObjectUtils.toString(objtmp2.get("ProblemValue2"), ""));
                        tabList.add(ObjectUtils.toString(objtmp2.get("isFin12"), ""));
                        tabMaps = new HashMap<String, List<String>>();
                        tabMaps.put("${tb3}", tabList);
                        tabListMaps.add(tabMaps);
                    }
                }
                Object prjName = lists.get(j).getPrjname();
                CustomXWPFDocument doc = WordUtil.generateWord(param, filePath, tabListMaps);
                FileOutputStream fopts = new FileOutputStream(dowPatch
                        + ObjectUtils.toString(prjName, "").replace("/", "") + DateTimeUtils.dateToStr(new Date()) + ".docx");
                doc.write(fopts);
                fopts.close();
            }
        }
    }

    /**
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception 流异常
     */
    @RequestMapping(value = "/batchWordDocs",method = RequestMethod.GET)
    public void batchWordDocs(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> fileNames = null;
        String dowPatch = request.getSession().getServletContext().getRealPath("/file/cache/");
        //响应头的设置
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");

        //设置压缩包的名字
        //解决不同浏览器压缩包名字含有中文时乱码的问题
        String downloadName = "评审报告.zip";
        String agent = request.getHeader("USER-AGENT");
        downloadName = getDownloadName(downloadName, agent);
        response.setContentType("application/x-msdownload");
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
        fileNames = getAllFile(dowPatch, false);
        //循环将文件写入压缩流
        DataOutputStream os = getDataOutputStream(request, fileNames, zipos);
        //关闭流
        closeStream(zipos, os);
    }

    /**
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception 流异常
     */
    @RequestMapping(value = "batchWordDocsAll", method = RequestMethod.GET)
    public void batchWordDocsAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
        exportWordDocsAll(request, response);
        List<String> fileNames = null;
        String dowPatch = request.getSession().getServletContext().getRealPath("/file/cache/");
        //响应头的设置
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");

        //解决不同浏览器压缩包名字含有中文时乱码的问题
        String downloadName = "评审报告.zip";
        String agent = request.getHeader("USER-AGENT");
        downloadName = getDownloadName(downloadName, agent);
        response.setContentType("application/x-msdownload");
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
        fileNames = getAllFile(dowPatch, false);
        //循环将文件写入压缩流
        DataOutputStream os = getDataOutputStream(request, fileNames, zipos);
        //关闭流
        closeStream(zipos, os);
    }

    static void closeStream(ZipOutputStream zipos, DataOutputStream os) {
        try {
            if (os != null) {
                os.flush();
                os.close();
            }
            if (zipos != null) {
                zipos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DataOutputStream getDataOutputStream(HttpServletRequest request, List<String> fileNames, ZipOutputStream zipos) {
        DataOutputStream os = null;
        for (int i = 0; i < fileNames.size(); i++) {
            //模拟文件，myFile.txt为需要下载的文件
            String filePath = request.getSession().getServletContext().getRealPath("/file/cache/" + fileNames.get(i).replace("/", ""));
            File file = new File(filePath);
            try {
                //添加ZipEntry，并ZipEntry中写入文件流
                //这里，加上i是防止要下载的文件有重名的导致下载失败
                if (zipos != null) {
                    String[] str = fileNames.get(i).split("/");
                    zipos.putNextEntry(new ZipEntry(i + str[str.length - 1]));
                }
                os = new DataOutputStream(zipos);
                InputStream is = new FileInputStream(file);
                byte[] b = new byte[100];
                int length;
                while ((length = is.read(b)) != -1) {
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
        return os;
    }

    private String getDownloadName(String downloadName, String agent) {
        downloadName = getString(downloadName, agent, MSIE, TRIDENT);
        return downloadName;
    }

    static String getString(String downloadName, String agent, String msie, String trident) {
        try {
            if (agent.contains(msie) || agent.contains(trident)) {
                downloadName = java.net.URLEncoder.encode(downloadName, "UTF-8");
            } else {
                downloadName = new String(downloadName.getBytes("UTF-8"), "ISO-8859-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return downloadName;
    }

    /**
     * 获取路径下的所有文件/文件夹
     *
     * @param directoryPath  需要遍历的文件夹路径
     * @param isAddDirectory 是否将子文件夹的路径也添加到list集合中
     * @return List<String>
     */
    private List<String> getAllFile(String directoryPath, boolean isAddDirectory) {
        List<String> list = new ArrayList<>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (isAddDirectory) {
                        list.add(file.getName());
                    }
                    list.addAll(getAllFile(file.getName(), isAddDirectory));
                } else {
                    list.add(file.getName());
                }
            }
        }
        return list;
    }
}
