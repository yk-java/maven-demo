package com.glens.exportword;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;

/**
 * @author GaoJiaNan
 * @version V1.0
 * @ClassName: WordUtil.java
 * @Description: TODO(适用于word 2007 poi 版本 3.7)
 * @Date 2015-5-27 下午5:53:52
 */
public class WordUtil {
    /**
     * 根据指定的参数值、模板，生成 word 文档
     *
     * @param param    需要替换的变量
     * @param template 模板
     */
    public static CustomXWPFDocument generateWord(Map<String, Object> param,
                                                  String template, List<Map<String, List<String>>> tabListMaps) {
        CustomXWPFDocument doc = null;
        try {
            OPCPackage pack = POIXMLDocument.openPackage(template);
            doc = new CustomXWPFDocument(pack);
            if (param != null && param.size() > 0) {

                // 处理段落-替换
                List<XWPFParagraph> paragraphList = doc.getParagraphs();
                processParagraphs(paragraphList, param, doc);

                // 处理表格-替换
                Iterator<XWPFTable> it = doc.getTablesIterator();
                while (it.hasNext()) {
                    XWPFTable table = it.next();
                    List<XWPFTableRow> rows = table.getRows();
                    for (XWPFTableRow row : rows) {
                        List<XWPFTableCell> cells = row.getTableCells();
                        for (XWPFTableCell cell : cells) {
                            List<XWPFParagraph> paragraphListTable = cell
                                    .getParagraphs();
                            processParagraphs(paragraphListTable, param, doc);
                        }
                    }
                }
            }

            // 处理表格-增加行
            String flagStr = "", temp = "";
            List<XWPFTable> list = doc.getTables();
            for (XWPFTable xwpfTable : list) {

                XWPFTableRow crtRow = xwpfTable.getRow(0);
                XWPFTableCell cell = crtRow.getTableCells().get(0);
                flagStr = cell.getText();
                temp = cell.getText();
                if (flagStr.contains("$")) {
                    flagStr = flagStr.substring(flagStr.indexOf("$"), flagStr.length());
                    if (temp.contains(flagStr)) {
                        cell.removeParagraph(0);
                        cell.setText(temp.substring(0, temp.indexOf("$")));
                    }

                    if (StringUtils.isNotEmpty(flagStr)) {
                        for (Map<String, List<String>> map : tabListMaps) {
                            if (map.containsKey(flagStr)) {
                                List<String> tbList = map.get(flagStr);
                                if (tbList.size() > 0) {
                                    XWPFTableRow newCrtRow = xwpfTable.createRow();
                                    List<XWPFTableCell> newCells = newCrtRow.getTableCells();
                                    for (int j = 0; j < newCells.size(); j++) {
                                        CTTcPr cellPr = newCells.get(j).getCTTc().addNewTcPr(); // 单元格属性
                                        // 设置宽度
                                        cellPr.addNewTcW().setW(BigInteger.valueOf(3000));
                                        newCells.get(j).removeParagraph(0);
                                        newCells.get(j).setText(tbList.get(j));
                                    }
                                }
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * 处理段落
     *
     * @param paragraphList
     */
    public static void processParagraphs(List<XWPFParagraph> paragraphList,
                                         Map<String, Object> param, CustomXWPFDocument doc) {
        if (paragraphList != null && paragraphList.size() > 0) {
            for (XWPFParagraph paragraph : paragraphList) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String text = run.getText(0);
                    if (text != null) {
                        boolean isSetText = false;
                        for (Entry<String, Object> entry : param.entrySet()) {
                            String key = entry.getKey();
                            if (text.contains(key)) {
                                isSetText = true;
                                Object value = entry.getValue();
                                if (value instanceof String) { // 文本替换
                                    text = text.replace(key, value.toString());
                                } else if (value instanceof Map) { // 图片替换
                                    text = text.replace(key, "");
                                    Map pic = (Map) value;
                                    Double d_w = Double.parseDouble(pic.get(
                                            "width").toString());
                                    Double d_h = Double.parseDouble(pic.get(
                                            "height").toString());
                                    int width = d_w.intValue();
                                    int height = d_h.intValue();
                                    int picType = getPictureType(pic
                                            .get("type").toString());
                                    byte[] byteArray = (byte[]) pic
                                            .get("content");
                                    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(
                                            byteArray);
                                    try {
                                        int ind = 0;
                                        //int ind = doc.addPicture(byteInputStream, picType);
                                        doc.createPicture(ind, width, height,
                                                paragraph);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        if (isSetText) {
                            run.setText(text, 0);
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据图片类型，取得对应的图片类型代码
     *
     * @param picType
     * @return int
     */
    private static int getPictureType(String picType) {
        int res = CustomXWPFDocument.PICTURE_TYPE_PICT;
        if (picType != null) {
            if (picType.equalsIgnoreCase("png")) {
                res = CustomXWPFDocument.PICTURE_TYPE_PNG;
            } else if (picType.equalsIgnoreCase("dib")) {
                res = CustomXWPFDocument.PICTURE_TYPE_DIB;
            } else if (picType.equalsIgnoreCase("emf")) {
                res = CustomXWPFDocument.PICTURE_TYPE_EMF;
            } else if (picType.equalsIgnoreCase("jpg")
                    || picType.equalsIgnoreCase("jpeg")) {
                res = CustomXWPFDocument.PICTURE_TYPE_JPEG;
            } else if (picType.equalsIgnoreCase("wmf")) {
                res = CustomXWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }

    /**
     * 将输入流中的数据写入字节数组
     *
     * @param in
     * @return
     */
    public static byte[] inputStream2ByteArray(InputStream in, boolean isClose) {
        byte[] byteArray = null;
        try {
            int total = in.available();
            byteArray = new byte[total];
            in.read(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (isClose) {
                try {
                    in.close();
                } catch (Exception e2) {
                    System.out.println("关闭流失败");
                }
            }
        }
        return byteArray;
    }
}
