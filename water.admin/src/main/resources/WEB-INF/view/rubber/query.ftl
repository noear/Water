<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 代码查询</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/clipboard.min.js"></script>
    <script src="${js}/layer.js"></script>

</head>
<body>

<main>

        <toolbar>

                <left><form>
                    <select id="code_type" name="code_type">
                        <option value="0">模型构造代码</option>
                        <option value="1">字段动态代码</option>
                        <option value="2">计算事件代码</option>
                        <option value="3">数据扫描代码</option>
                    </select>：<input type="text" placeholder="代码片断" id="code" name="code"/>
                    <button type="submit">查询</button></form>
                </left>

        </toolbar>
            <datagrid>
                <table>
                    <thead>
                    <tr>
                        <td width="40px">ID</td>
                        <td width="120px">分类</td>
                        <td width="300px">名称</td>
                        <td>备注</td>
                        <td width="110px">操作</td>
                    </tr>
                    </thead>
                    <tbody id="tbody">
                        <#list list as m>
                            <tr>
                                <td>${m.id}</td>
                                <td style="text-align: left">${m.tag}</td>
                                <td style="text-align: left">${m.name}</td>
                                <td style="text-align: left">${m.note}</td>
                                <td>
                                    <a href="/rubber/query/code?code_type=${m.code_type}&id=${m.id}" class="t2">查看代码</a> |
                                    <a href="#" onclick="edit('${m.code_type}','${m.tag}',${m.pid},${m.id})" class="t2">编辑</a>
                                </td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            </datagrid>

</main>

</main>
</body>
<script>
    function edit(code_type,tag,pid,id) {
        if (code_type == 0) {
            //接口代码
            location.href = "/rubber/model?tag="+tag+"&model_id="+id;
        } else if (code_type == 1) {
            //计划任务
            location.href = "/rubber/model?tag="+tag+"&model_id="+pid+"&field_id="+id;
        } else if (code_type == 2) {
            //同步任务
            location.href = "/rubber/scheme?tag="+tag+"&scheme_id="+id;
        }else if (code_type == 3) {
            //同步任务
            location.href = "/rubber/block?tag="+tag+"&block_id="+id;
        }
    }
</script>
</html>