<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">

    <title>Activiti部署流程</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="Activiti部署流程">
    <%@ include file="common_js.jsp" %>
    <script type="text/javascript">
        $(function () {
            createGrid();
        });

        function getQueryParams() {
            return {};
        }

        function createGrid() {
            $('#gvGrid').datagrid({
                fit: true,
                striped: true,
                fitColumns: true,
                collapsible: false,
                url: '<%=basePath%>/activiti/getProcessList',
                rownumbers: true,
                queryParams: getQueryParams(),
                remoteSort: false, //定义不从服务器的数据进行排序.
                pagination: false,
                columns: [[{
                    field: "id",
                    title: "流程定义ID",
                    width: 80
                }, {
                    field: "deploymentId",
                    title: "部署ID",
                    width: 80
                }, {
                    field: "name",
                    title: "流程定义名称",
                    width: 80
                }, {
                    field: "key",
                    title: "流程定义KEY",
                    width: 80
                }, {
                    field: "version",
                    title: "版本号",
                    width: 80
                }, {
                    field: "resourceName",
                    title: "XML资源名称",
                    width: 160
                }, {
                    field: "diagramResourceName",
                    title: "图片资源名称",
                    width: 160
                },
                    {
                        field: "delFile",
                        title: "操作",
                        width: 50,
                        align: "center",
                        formatter: function (value, row, index) {
                            let str = "";
                            str += '<a href="javascript:delDeploy(\'' + row.deploymentId + '\')"><img border="0" title="删除部署的流程" src="'
                                + '<%=basePath%>/js/libs/easyui/themes/icons/cancel.png" /></a>';
                            return str;
                        }
                    }]],
                toolbar: '#gridToolbar'
            });

            //表头都居中
            $("div.datagrid-header>.datagrid-header-inner>table>tbody div").css({
                "text-align": "center"
            });
        }

        function refresh() {
            let $gvGrid = $("#gvGrid");
            $gvGrid.datagrid("load", getQueryParams());
            $gvGrid.datagrid("clearSelections");
        }

        function deploy() {
            let file = $("#fileInfo").textbox("getValue");
            if (!file) {
                $.dialog.tips("未选取附件，无法进行上传操作", 2, "alert.gif");
                return;
            }
            let ext = file.substring(file.lastIndexOf("."), file.length).toUpperCase();
            if (ext !== ".ZIP" && ext !== ".BAR" && ext !== ".BPMN" && file.toUpperCase() !== "BPMN20.XML") {
                $.dialog.tips("不支持附件类型" + ext, 2, "alert.gif");
                return;
            }

            $("#deploy_form").form("submit", {
                url: "<%=basePath%>/activiti/deploy",
                onSubmit: function () {
                    return true;
                },
                success: function (data) {
                    let data = eval('(' + data + ')');
                    if (data.result) {
                        refresh();
                    } else {
                        $.messager.alert('警告', data.errorMsg, 'error');
                    }
                }
            });
        }

        function delDeploy(deploymentId) {
            if (!deploymentId) {
                $.dialog.tips("请选择需要删除的流程", 2, "alert.gif");
                return;
            }
            $.post("<%=basePath%>/activiti/delDeploy", {
                deploymentId: deploymentId
            }, function (data) {
                if (data.result) {
                    refresh();
                } else {
                    alert(data.errorMsg);
                }
            }, 'json');
        }
    </script>

</head>

<body class="easyui-layout">
<div data-options="region:'center',border:false">
    <table id="gvGrid">
    </table>
</div>
<div id="gridToolbar"
     style="padding: 0; height: auto;width:100%;font-size: 12px;">
    <table style="width:100%; height:auto;">
        <tr>
            <td class="left" style=" float:left;">
                <div>
                    <form id="deploy_form" enctype="multipart/form-data"
                          method="post" style="margin: 0;">
                        <table style="font-size: 12px;">
                            <tr>
                                <td align="right">文件：</td>
                                <td>
                                    <label for="fileInfo"></label>
                                    <input id="fileInfo" name="fileInfo"
                                           class="easyui-filebox"
                                           data-options="prompt:'支持文件格式：zip、bar、bpmn、bpmn20.xml',buttonText:'选择文件'"
                                           style="width:330px;height:27px"/>
                                </td>
                                <td>
                                    <a href="javascript:deploy()"
                                       class="easyui-linkbutton" iconCls="icon-upload" plain="true">部署流程</a>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
