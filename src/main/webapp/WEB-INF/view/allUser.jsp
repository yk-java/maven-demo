<%@ page contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>全部员工</title>
<script type="text/javascript" src="../../js/common/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$(".down").click(function(){
            let href = $(this).attr("href");
            $("form").attr("action", href).submit();
			return false;
		});
        $(".delete").click(function(){
            let href = $(this).attr("href");
            $("#deleteUser").attr("action", href).submit();
            return false;
        });
	});
</script>
</head>
<body>

	<form action="" method="get">
	</form>

	<form id="deleteUser" action="" method="post">
		<input type="hidden" name="_method" value="DELETE" />
	</form>

	<c:choose>
		<c:when test="${empty requestScope.ALLUSER}">
			没有员工信息
		</c:when>
		<c:otherwise>
			<table border="1">
				<tr>
					<th>编号</th>
					<th>姓名</th>
					<th>年龄</th>
					<th>地址</th>
					<th>操作</th>
				</tr>
				<c:forEach items="${requestScope.ALLUSER }" var="UserInfo">
					<tr>
						<td>&nbsp;${UserInfo.userId }&nbsp;</td>
						<td>&nbsp;${UserInfo.userName }&nbsp;</td>
						<td>${UserInfo.userAge }</td>
						<td>${UserInfo.userAddress }</td>
						<td><a class="delete"
							href="${pageContext.request.contextPath }/user/delete/${UserInfo.userId}">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;<a
							href="${pageContext.request.contextPath }/user/detail/${UserInfo.userId}">更新</a></td>
					</tr>
				</c:forEach>
			</table>
		</c:otherwise>
	</c:choose>

	<c:choose>
		<c:when test="${empty requestScope.attachment}">
			没有附件信息
		</c:when>
		<c:otherwise>
			<table border="1">
				<tr>
					<th>附件编号</th>
					<th>附件名称</th>
					<th>附件地址</th>
					<th>附件类型</th>
					<th>操作</th>
				</tr>
				<c:forEach items="${requestScope.attachment}" var="AttachmentInfo">
					<tr>
						<td>&nbsp;${AttachmentInfo.attachId }&nbsp;</td>
						<td>&nbsp;${AttachmentInfo.attachSaveName }&nbsp;</td>
						<td>${AttachmentInfo.attachSavePath }</td>
						<td>${AttachmentInfo.attachType }</td>
						<td><a class="down"
							   href="${pageContext.request.contextPath }/file/down/${AttachmentInfo.attachId}">下载</a>
					</tr>
				</c:forEach>
			</table>
		</c:otherwise>
	</c:choose>
</body>
</html>