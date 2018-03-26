package com.glens.excel;

import java.lang.annotation.*;

/**
 * Title:
 * Description:
 *
 * @author yk.
 * @version Version 1.0
 * Date: 2018/1/17 15:41
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {
    String value() default "";

    /**
     *从1开始
     * @return int
     */
    int col() default 0;
}
