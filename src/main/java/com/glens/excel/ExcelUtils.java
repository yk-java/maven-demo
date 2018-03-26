package com.glens.excel;

import com.glens.utils.DateTimeUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by admin on 2017/1/17.
 *
 * @author yk
 */
public class ExcelUtils {

    private final static Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    private final static String EXCEL2003 = ".xls";
    private final static String EXCEL2007 = ".xlsx";

    public static <T> List<T> readExcel(String path, Class<T> cls) {
        List<T> dataList = new ArrayList<>();

        Workbook workbook = null;
        try {
            if (path.endsWith(EXCEL2007)) {
                FileInputStream is = new FileInputStream(new File(path));
                workbook = new XSSFWorkbook(is);
            }
            if (path.endsWith(EXCEL2003)) {
                FileInputStream is = new FileInputStream(new File(path));
                workbook = new HSSFWorkbook(is);
            }
            if (workbook != null) {
                //类映射  注解 value-->bean columns
                Map<String, List<Field>> classMap = new HashMap<>(100);
                Field[] fields = cls.getDeclaredFields();
                for (Field field : fields) {
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null) {
                        String value = annotation.value();
                        if (StringUtils.isBlank(value)) {
                            continue;
                        }
                        if (!classMap.containsKey(value)) {
                            classMap.put(value, new ArrayList<>());
                        }
                        field.setAccessible(true);
                        classMap.get(value).add(field);
                    }
                }
                //索引-->columns
                Map<Integer, List<Field>> reflectionMap = new HashMap<>(100);
                //默认读取第一个sheet
                Sheet sheet = workbook.getSheetAt(0);

                boolean firstRow = true;
                for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (firstRow) {
                        //首行  提取注解
                        for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j);
                            String cellValue = getCellValue(cell);
                            if (classMap.containsKey(cellValue)) {
                                reflectionMap.put(j, classMap.get(cellValue));
                            }
                        }
                        firstRow = false;
                    } else {
                        //忽略空白行
                        if (row == null) {
                            continue;
                        }
                        try {
                            T t = cls.newInstance();
                            //判断是否为空白行
                            boolean allBlank = true;
                            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                                if (reflectionMap.containsKey(j)) {
                                    Cell cell = row.getCell(j);
                                    String cellValue = getCellValue(cell);
                                    if (StringUtils.isNotBlank(cellValue)) {
                                        allBlank = false;
                                    }
                                    List<Field> fieldList = reflectionMap.get(j);
                                    for (Field field : fieldList) {
                                        try {
                                            handleField(t, cellValue, field);
                                        } catch (Exception e) {
                                            log.error(String.format("reflect field:%s value:%s exception!", field.getName(), cellValue), e);
                                        }
                                    }
                                }
                            }
                            if (!allBlank) {
                                dataList.add(t);
                            } else {
                                log.warn(String.format("row:%s is blank ignore!", i));
                            }
                        } catch (Exception e) {
                            log.error(String.format("parse row:%s exception!", i), e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("parse excel exception!", e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataList;
    }

    private static <T> void handleField(T t, String value, Field field) throws Exception {
        Class<?> type = field.getType();
        if (type == null || type == void.class || StringUtils.isBlank(value)) {
            return;
        }
        if (type == Object.class) {
            field.set(t, value);
        } else if (type.getSuperclass() == null || type.getSuperclass() == Number.class) {
            //数字类型
            if (type == int.class || type == Integer.class) {
                field.set(t, NumberUtils.toInt(value));
            } else if (type == long.class || type == Long.class) {
                field.set(t, NumberUtils.toLong(value));
            } else if (type == byte.class || type == Byte.class) {
                field.set(t, NumberUtils.toByte(value));
            } else if (type == short.class || type == Short.class) {
                field.set(t, NumberUtils.toShort(value));
            } else if (type == double.class || type == Double.class) {
                field.set(t, NumberUtils.toDouble(value));
            } else if (type == float.class || type == Float.class) {
                field.set(t, NumberUtils.toFloat(value));
            } else if (type == char.class || type == Character.class) {
                field.set(t, CharUtils.toChar(value));
            } else if (type == boolean.class) {
                field.set(t, BooleanUtils.toBoolean(value));
            } else if (type == BigDecimal.class) {
                field.set(t, new BigDecimal(value));
            }
        } else if (type == Boolean.class) {
            field.set(t, BooleanUtils.toBoolean(value));
        } else if (type == Date.class) {
            field.set(t, DateTimeUtils.strToDate(value));
        } else if (type == String.class) {
            field.set(t, value);
        } else {
            Constructor<?> constructor = type.getConstructor(String.class);
            field.set(t, constructor.newInstance(value));
        }
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return DateTimeUtils.dateToStr(DateUtil.getJavaDate(cell.getNumericCellValue()));
            } else {
                return new BigDecimal(cell.getNumericCellValue()).toString();
            }
        } else if (cell.getCellTypeEnum() == CellType.STRING) {
            return StringUtils.trimToEmpty(cell.getStringCellValue());
        } else if (cell.getCellTypeEnum() == CellType.FORMULA) {
            return StringUtils.trimToEmpty(cell.getCellFormula());
        } else if (cell.getCellTypeEnum() == CellType.BLANK) {
            return "";
        } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellTypeEnum() == CellType.ERROR) {
            return "ERROR";
        } else {
            return cell.toString().trim();
        }
    }

    public static <T> void writeExcel(HttpServletResponse response, String fileName, List<T> dataList, Class<T> cls) {
        Field[] fields = cls.getDeclaredFields();
        List<Field> fieldList = fieldList(fields);
        Workbook wb = new XSSFWorkbook();

        Sheet sheet = wb.createSheet("sheet");
        AtomicInteger ai = new AtomicInteger();
        {
            Row row = sheet.createRow(ai.getAndIncrement());
            AtomicInteger aj = new AtomicInteger();

            //写入头部
            fieldList.forEach(field -> {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                String columnName = "";
                if (annotation != null) {
                    columnName = annotation.value();
                }
                Cell cell = row.createCell(aj.getAndIncrement());

                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);

                Font font = wb.createFont();
                font.setBold(false);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(columnName);
            });
        }

        if (CollectionUtils.isNotEmpty(dataList)) {
            dataList.forEach(t -> {
                Row row = sheet.createRow(ai.getAndIncrement());
                setCellValues(fieldList, t, row);
            });
        }

        //冻结窗格
        wb.getSheet("sheet").createFreezePane(0, 1, 0, 1);
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/ms-excel;charset=UTF-8");
            fileName = fileName + EXCEL2007;
            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(new String(fileName.getBytes("gbk"), "iso-8859-1")));
            wb.write(out);
        } catch (IOException e) {
            log.error("输出流错误");
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据模板填充数据
     *
     * @param response HttpServletResponse
     * @param resList  数据库查询数据
     * @param cls      数据对象
     */
    public static  <T> void downExcelByTemplate(String filePath, HttpServletResponse response, List<T> resList, Class<T> cls) {
        Field[] fields = cls.getDeclaredFields();
        //获取对象所有字段
        List<Field> fieldList = fieldList(fields);
        OutputStream ouputStream;
        try {
            // 第一步，创建一个webbook，对应一个Excel文件
            HSSFWorkbook workbook = GetExcelModel(filePath);
            // 第二步，在webbook中添加sheet,对应Excel文件中的sheet
            HSSFSheet sheet = workbook.getSheetAt(0);
            AtomicInteger ai = new AtomicInteger();

            // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
            if (CollectionUtils.isNotEmpty(resList)) {
                resList.forEach(t -> {
                    Row row = sheet.getRow(ai.getAndIncrement() + 4);
                    setCellValues(fieldList, t, row);
                });
            }
            // 第六步，将文件存到指定位置
            String filename = "test_" + DateTimeUtils.dateToStr(new Date())
                    + EXCEL2007;
            // 获取下载时客户端Excel的名称
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String(filename.getBytes("gbk"), "iso-8859-1"));
            ouputStream = response.getOutputStream();
            workbook.write(ouputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //为表格赋值
    private static <T> void setCellValues(List<Field> fieldList, T t, Row row) {
        AtomicInteger aj = new AtomicInteger();
        fieldList.forEach(field -> {
            Class<?> type = field.getType();
            Object value = "";
            try {
                value = field.get(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Cell cell = row.createCell(aj.getAndIncrement()+2);
            if (value != null) {
                if (type == Date.class) {
                    cell.setCellValue(DateTimeUtils.dateToStr((Date) value));
                } else {
                    cell.setCellValue(value.toString());
                }
            }
        });
    }

    //为对象字段排序
    private static List<Field> fieldList(Field[] fields) {
        return Arrays.stream(fields).filter(field -> {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation != null && annotation.col() > 0) {
                field.setAccessible(true);
                return true;
            }
            return false;
        }).sorted(Comparator.comparing(field -> {
            int col = 0;
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation != null) {
                col = annotation.col();
            }
            return col;
        })).collect(Collectors.toList());
    }

    // 读取excel模板文件，生成表头
    private static HSSFWorkbook GetExcelModel(String filePath) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            POIFSFileSystem ps = new POIFSFileSystem(fis);
            workbook = new HSSFWorkbook(ps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;
    }
}
