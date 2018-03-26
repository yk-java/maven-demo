<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String pathCommon = request.getContextPath();
	String basePathCommon = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ pathCommon + "/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePathCommon%>">

<title>公用JS页面</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="公用JS页面">

<link rel="stylesheet" type="text/css" href="<%=basePathCommon%>/js/libs/easyui/themes/bootstrap/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=basePathCommon%>/js/libs/easyui/themes/icon.css">
<script type="text/javascript"
	src="<%=basePathCommon%>/js/common/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="<%=basePathCommon%>/js/libs/lhgdialog/lhgdialog.min.js?skin=gwl"></script>
<script type="text/javascript"
	src="<%=basePathCommon%>/js/libs/easyui/jquery.easyui.min.js"></script>	
<script type="text/javascript"
	src="<%=basePathCommon%>/js/libs/easyui/extend.js"></script>
<script type="text/javascript"
	src="<%=basePathCommon%>/js/libs/easyui/easyui-lang-zh_CN.js"></script>
</head>
<body>
</body>
</html>
