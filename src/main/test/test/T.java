package test;

import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yk on 2018/1/20.
 */
public class T {

    @Test
    public void test() {
        File file = new File("a.txt");
        String filename = file.getName();
        String[] str = filename.split("\\.");
        String filetype = str[str.length - 1];
        System.out.println(filetype);
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        System.out.println(formatter.format(new Date()));
    }
}
