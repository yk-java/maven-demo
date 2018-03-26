<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>企业信息管理系统-登录</title>
    <link href="css/page/login.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="second_body">
    <form data-bind="submit:loginClick">
        <div class="logo"></div>
        <div class="title-zh">企业信息管理系统</div>
        <div class="title-en" style="">Enterprise Information Manage System</div>
        <div class="message" data-bind="html:message"></div>
        <table border="0" style="width:300px;">
            <tr>
                <td style="white-space:nowrap; padding-bottom: 5px;width:55px;">用户名：</td>
                <td colspan="2">
                    <input type="text" id="userCode" class="login" data-bind="value:form.userCode" />
                </td>
            </tr>
            <tr>
                <td class="lable" style="white-space:nowrap; letter-spacing: 0.5em; vertical-align: middle">密码：</td>
                <td colspan="2">
                    <input type="password" id="password" class="login" data-bind="value:form.password" />
                </td>
            </tr>
            <tr>
                <td></td>
                <td colspan="2">
                    <input type="checkbox" data-bind="checked:form.remember" /><span>系统记住我</span>
                </td>
            </tr>
            <tr>
                <td colspan="3" style="text-align:center">
                    <input type="submit" value="登录" class="login_button" />
                    <input type="button" value="重置" class="reset_botton" data-bind="click:resetClick" />
                </td>
            </tr>
        </table>
    </form>
</div>

<script type="text/javascript" src="js/common/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="js/common/jquery.cookie.js"></script>
<script type="text/javascript" src="js/common/utils.js"></script>
<script type="text/javascript" src="js/core/common.js"></script>
<script type="text/javascript" src="js/core/knockout-3.4.2.js"></script>
<script type="text/javascript" src="js/login/login.js"></script>

<script type="text/javascript">
    $(function () {
        ko.applyBindings(new viewModel());
    });
</script>
</body>
</html>