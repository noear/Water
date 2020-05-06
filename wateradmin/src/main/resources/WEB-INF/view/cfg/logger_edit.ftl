<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 日志配置-编辑</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        datagrid b{color: #8D8D8D;font-weight: normal}
    </style>

    <script>
        var logger_id = '${log.logger_id}';

        function save() {
            var tag = $('#tag').val();
            var logger = $('#logger').val();
            var keep_days = $('#keep_days').val();
            var source = $('#source').val();
            var note = $('#note').val();

            if (tag == null || tag == "" || tag == undefined) {
                top.layer.msg("标签名称不能为空！");
                return;
            }

            if(logger_id==null){
                logger_id=0;
            }
            $.ajax({
                type:"POST",
                url:"/cfg/logger/edit/ajax/save",
                data:{
                    "logger_id":logger_id,
                    "tag":tag,
                    "logger":logger,
                    "keep_days":keep_days,
                    "source":source,
                    "note":note
                },
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功')
                        setTimeout(function(){
                            parent.location.href="/cfg/logger?tag_name="+tag;
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        function del() {
            if(!logger_id){
                return;
            }

            if(!confirm("确定要删除吗？")){
                return;
            }

            $.ajax({
                type:"POST",
                url:"/cfg/logger/edit/ajax/del",
                data:{
                    "logger_id":logger_id
                },
                success:function (data) {
                    if(data.code==1) {
                        top.layer.msg('操作成功')
                        setTimeout(function(){
                            parent.location.href="/cfg/logger?tag_name=${log.tag!}";
                        },800);
                    }else{
                        top.layer.msg(data.msg);
                    }
                }
            });
        }

        $(function () {
            document.getElementById('source').value="${log.source!}";

            ctl_s_save_bind(document,save);
        });
    </script>
</head>
<body>
<toolbar class="blockquote">
    <left class="ln30">
        <h2><a onclick="history.back(-1)" href="#" class="noline">日志配置</a></h2> / 编辑
    </left>
    <right class="form">
        <n>ctrl + s 可快捷保存</n>
        <button type="button" onclick="save()">保存</button>
        <#if is_admin == 1>
        <button type="button" class="minor" onclick="del()">删除</button>
        </#if>
    </right>
</toolbar>

<detail>
    <form>
        <table>
            <tr>
            <tr>
                <th>tag*</th>
                <td><input type="text" id="tag" autofocus value="${tag_name!}"/></td>
            </tr>
            <tr>
                <th>logger*</th>
                <td><input type="text" id="logger" value="${log.logger!}"/></td>
            </tr>
            <tr>
                <th>保留天数</th>
                <td><input type="text" id="keep_days" value="${log.keep_days!}"/></td>
            </tr>
            <tr>
                <th>数据源</th>
                <td>
                    <select id="source">
                        <option value=""></option>
                        <#list option_sources as sss>
                            <option value="${sss}">${sss}</option>
                        </#list>
                    </select>
                </td>
            </tr>
            <tr>
                <th>备注</th>
                <td><input type="text" class="longtxt" id="note" value="${log.note!}"/></td>
            </tr>
        </table>
    </form>
</detail>
</body>
</html>