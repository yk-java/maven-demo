package com.glens.utils;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipInputStream;


/**
 * Title:
 * Description:
 *
 * @author yk.
 * @version Version 1.0
 * Date: 2018/1/19 11:39
 */
public class ZipUtils {
    private static final String ENCODING = "GBK";
    private static final String SUFFIX = ".zip";
    private static final Integer SIZE = 1024;
    public ZipUtils() {
    }

    public static void compress(String sourcePath, String zipPath) throws IOException {
        compress(sourcePath, zipPath, "", "");
    }

    private static void compress(String sourcePath, String zipPath, String encoding, String comment) throws FileNotFoundException, IOException {
        File sourceFile = new File(sourcePath);
        if (sourceFile.exists() && (!sourceFile.isDirectory() || Objects.requireNonNull(sourceFile.list()).length != 0)) {
            String fileName;
            int index;
            if (zipPath != null && !"".equals(zipPath)) {
                if (!zipPath.endsWith(SUFFIX)) {
                    fileName = sourceFile.getName();
                    index = fileName.lastIndexOf(".");
                    zipPath = zipPath + File.separator + (index > -1 ? fileName.substring(0, index) : fileName) + SUFFIX;
                }
            } else {
                fileName = sourceFile.getAbsolutePath();
                index = fileName.lastIndexOf(".");
                zipPath = (index > -1 ? fileName.substring(0, index) : fileName) + SUFFIX;
            }

            if (encoding == null || "".equals(encoding)) {
                encoding = ENCODING;
            }

            File zipFile = new File(zipPath);
            if (!zipFile.getParentFile().exists()) {
               Boolean b = zipFile.getParentFile().mkdirs();
            }

            FileOutputStream fos;
            fos = getFileOutputStream(zipPath);

            CheckedOutputStream csum = new CheckedOutputStream(fos, new CRC32());
            ZipOutputStream zos = new ZipOutputStream(csum);
            zos.setEncoding(encoding);
            zos.setComment(comment);
            zos.setMethod(8);
            zos.setLevel(9);
            BufferedOutputStream bout = null;

            try {
                bout = new BufferedOutputStream(zos);
                compressRecursive(zos, bout, sourceFile, sourceFile.getParent());
            } finally {
                fos.close();
                if (bout != null) {
                    try {
                        bout.close();
                    } catch (Exception ignored) {

                    }
                }

            }
        } else {
            throw new FileNotFoundException("要压缩的文件或目录不存在，或者要压缩的目录为空");
        }
    }

    public static void compress(List<String> sourcePaths, String zipPath) throws IOException {
        compress(sourcePaths, zipPath, "", "");
    }

    private static void compress(List<String> sourcePaths, String zipPath, String encoding, String comment) throws FileNotFoundException, IOException {
        if (zipPath != null && !"".equals(zipPath) && zipPath.endsWith(SUFFIX)) {
            if (encoding == null || "".equals(encoding)) {
                encoding = ENCODING;
            }

            File zipFile = new File(zipPath);
            if (!zipFile.getParentFile().exists()) {
                Boolean b = zipFile.getParentFile().mkdirs();
            }

            FileOutputStream fos;

            fos = getFileOutputStream(zipPath);

            CheckedOutputStream csum = new CheckedOutputStream(fos, new CRC32());
            ZipOutputStream zos = new ZipOutputStream(csum);
            zos.setEncoding(encoding);
            zos.setComment(comment);
            zos.setMethod(8);
            zos.setLevel(9);
            BufferedOutputStream bout = null;

            try {
                bout = new BufferedOutputStream(zos);
                int i = 0;

                for(int len = sourcePaths.size(); i < len; ++i) {
                    File sourceFile = new File(sourcePaths.get(i));
                    compressRecursive(zos, bout, sourceFile, sourceFile.getParent());
                }
            } finally {
                if (bout != null) {
                    try {
                        bout.close();
                    } catch (Exception ignored) {

                    }
                }
            }
        } else {
            throw new FileNotFoundException("必须指定一个压缩路径，而且该路径必须以'.zip'为结尾");
        }
    }

    private static FileOutputStream getFileOutputStream(String zipPath) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(zipPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fos;
    }

    private static void compressRecursive(ZipOutputStream zos, BufferedOutputStream bout, File sourceFile, String prefixDir) throws IOException, FileNotFoundException {
        String entryName = sourceFile.getAbsolutePath().substring(prefixDir.length() + File.separator.length());
        if (sourceFile.isDirectory()) {
            File[] srcFiles = sourceFile.listFiles();

            for(int i = 0; i < Objects.requireNonNull(srcFiles).length; ++i) {
                compressRecursive(zos, bout, srcFiles[i], prefixDir);
            }
        } else {
            ZipEntry zipEntry = new ZipEntry(entryName);
            zos.putNextEntry(zipEntry);
            BufferedInputStream bin = null;

            try {
                bin = new BufferedInputStream(new FileInputStream(sourceFile));
                byte[] buffer = new byte[1024];

                int readCount;
                while((readCount = bin.read(buffer)) != -1) {
                    bout.write(buffer, 0, readCount);
                }

                bout.flush();
                zos.closeEntry();
            } finally {
                if (bin != null) {
                    try {
                        bin.close();
                    } catch (IOException ignored) {

                    }
                }

            }
        }

    }

    public static void decompress(String zipPath, String targetPath) throws Exception {
        decompress(zipPath, targetPath, null);
    }

    private static void decompress(String zipPath, String targetPath, String encoding) throws Exception {
        File file = new File(zipPath);
        if (!file.exists()) {
            throw new Exception("要解压的文件不存在!");
        } else {
            if (targetPath == null || "".equals(targetPath)) {
                targetPath = file.getParent();
            }
            if (encoding == null) {
                encoding = ENCODING;
            }

            ZipFile zipFile = new ZipFile(file, encoding);
            Enumeration files = zipFile.getEntries();
            ZipEntry entry;

            while (true) {
                while (files.hasMoreElements()) {
                    entry = (ZipEntry) files.nextElement();
                    if (entry.isDirectory()) {
                        String name = entry.getName();
                        name = name.substring(0, name.length() - 1);
                        File f = new File(targetPath + name);
                        f.mkdirs();
                    } else {
                        File f = new File(targetPath + entry.getName());
                        f.getParentFile().mkdirs();
                        f.createNewFile();
                        InputStream is = zipFile.getInputStream(entry);
                        FileOutputStream fos = new FileOutputStream(f);
                        byte[] b = new byte[1024];

                        int length;
                        while ((length = is.read(b, 0, SIZE)) != -1) {
                            fos.write(b, 0, length);
                        }
                        is.close();
                        fos.close();
                    }
                }
                zipFile.close();
                return;
            }
        }
    }



    public static void main(String[] args) throws Exception {
        compressTest();
    }

    private static void compressTest() throws Exception {
        String sourcePath = "E://周任务";
        String zipPath = "D://temp123//工程管理小工具";
        String comment = "工程管理小工具文件";
        compress(sourcePath, zipPath, ENCODING, comment);
    }

    private static void decompressTest() throws Exception {
        String zipPath = "C://工程管理小工具.zip";
        String targetPath = "C:" + File.separator + "temp";
        decompress(zipPath, targetPath, ENCODING);
    }

    private static void compressTest2() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("E:" + File.separator + "文档" + File.separator + "音乐");
        list.add("E:" + File.separator + "文档" + File.separator + "视频");
        list.add("E:" + File.separator + "文档" + File.separator + "资料");
        list.add("E:" + File.separator + "文档" + File.separator + "书籍");
        String zipPath = "E:" + File.separator + "我的文档压缩文件.zip";
        String comment = "我的文档压缩文件";
        compress(list, zipPath, ENCODING, comment);
    }
}
