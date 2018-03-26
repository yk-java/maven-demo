package com.glens.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;

/**
 * Title:
 * Description:
 *
 * @author yk.
 * @version Version 1.0
 * Date: 2018/2/9 17:46
 */
public class FileUtils {
    public FileUtils() {
    }

    public static void main(String[] args) {
    }

    public static String fileSizeFormat(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024L) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576L) {
            fileSizeString = df.format((double) fileS / 1024.0D) + "K";
        } else if (fileS < 1073741824L) {
            fileSizeString = df.format((double) fileS / 1048576.0D) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1.073741824E9D) + "G";
        }

        return fileSizeString;
    }

    public static boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static String getSuffix(String path) {
        return StringUtils.isNotEmpty(path) ? path.substring(path.lastIndexOf(".") + 1).toLowerCase() : "";
    }

    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;

        try {
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }

            fi = new FileInputStream(sourceFile);
            fo = new FileOutputStream(targetFile);
            in = fi.getChannel();
            out = fo.getChannel();
            in.transferTo(0L, in.size(), out);
        } catch (IOException var14) {
            throw var14;
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException var13) {
                throw var13;
            }
        }

    }

    private static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath);
            File myFilePath = new File(folderPath);
            myFilePath.delete();
        } catch (Exception ignored) {
            ;
        }

    }

    public static boolean delFile(String path) {
        File file = new File(path);
        return file.exists() && file.delete();
    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return false;
        } else if (!file.isDirectory()) {
            return false;
        } else {
            String[] tempList = file.list();
            File temp = null;

            if (tempList != null) {
                for (String aTempList : tempList) {
                    if (path.endsWith(File.separator)) {
                        temp = new File(path + aTempList);
                    } else {
                        temp = new File(path + File.separator + aTempList);
                    }
                    if (temp.isFile()) {
                        temp.delete();
                    }

                    if (temp.isDirectory()) {
                        delAllFile(path + "/" + aTempList);
                        delFolder(path + "/" + aTempList);
                        flag = true;
                    }
                }
            }

            return flag;
        }
    }
}
