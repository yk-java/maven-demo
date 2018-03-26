package com.glens.model;

import com.glens.excel.ExcelColumn;

/**
 * @author yk
 */
public class UserInfo {
    @ExcelColumn(value="用户编码", col=1)
    private Integer userId;

    @ExcelColumn(value="用户名", col=2)
    private String userName;

    private String userPassword;

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @ExcelColumn(value="年龄", col=3)
    private Integer userAge;

    @ExcelColumn(value="地址", col=4)
    private String userAddress;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
}