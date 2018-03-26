<%@ page contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>文件上传下载</title>
    <script type="text/javascript" src="<%=basePath%>js/common/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        var basePath="<%=basePath%>";
        $(function(){
            $("#excel").click(function(){
                exportBatch();
                return false;
            });
            $("#words").click(function(){
                exportWordsBatch();
                return false;
            });
        });
        function ajaxLoading(){
            $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");
            $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
        }
        function ajaxLoadEnd(){
            $(".datagrid-mask").remove();
            $(".datagrid-mask-msg").remove();
        }
        function exportBatch() {
            //get请求，可以传递参数，比方说我这里就传了一堆卷号，我只生成传过去的这堆卷号的检验记录
            //参数rollNumbers的细节就不展示了，业务相关
            window.location.href = basePath+'excel/downDistributionNetworkReport';
        }
        function exportWordsBatch(){
            $.post(basePath+"exportWord/exportWordDoc", {
                prjId:"2569b233dc2c63b1a4dd7c4b8f231927"
            }, function(result) {
                console.log(result);
                if (result != "不存在附件") {
                    window.location.href = basePath+"exportWord/batchWordDocs";
                } else {
                    $.dialog.tips("不存在附件。", 3, "fail.png");
                }
            }, 'text');
        }
    </script>
</head>
<body>
<h2>上传多个文件 实例</h2>
<form action="${pageContext.request.contextPath }/file/filesUpload" method="post" enctype="multipart/form-data">
    <p>
    选择文件:<input type="file" name="files" width="120px">
    <p>
    选择文件:<input type="file" name="files" width="120px">
    <p>
    选择文件:<input type="file" name="files" width="120px">
    <p>
    选择文件:<input type="file" name="files" width="120px">
    <p>
    选择文件:<input type="file" name="files" width="120px">
    <p>
    <input type="submit" value="提交">
</form>
<hr>
<form action="${pageContext.request.contextPath }/file/down/" method="get">
    <input type="submit" value="下载">
    <input id="excel" type="button" value="导出">
    <input id="words" type="button" value="导出word">
</form>
<form action="${pageContext.request.contextPath }/zip/downloadFiles" method="get">
    <input type="submit" value="zip下载">
</form>
</body>
</html>
