<#setting url_escaping_charset='utf-8'>
<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 性能监控</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
</head>
<body>
<main>
<toolbar>
        <form>
            服务：<input type="text"  name="name" placeholder="名称" id="name"/>&nbsp;&nbsp;
            <input type="text" name="serviceName" id="serviceName" value="${serviceName}" style="display: none"/>
            <button type="submit">查询</button>&nbsp;&nbsp;
        </form>
</toolbar>
<datagrid class="list">
    <table>
        <thead>
        <tr>
            <td sort="" class="left">服务@节点</td>
            <td width="70px" sort="average">平均<br/>响应</td>
            <td width="70px" sort="fastest">最快<br/>响应</td>
            <td width="70px" sort="slowest">最慢<br/>响应</td>
            <td width="80px" sort="total_num">请求<br/>次数</td>
            <td width="70px" sort="total_num_slow1">1s+<br/>次数</td>
            <td width="60px" sort="total_num_slow2">2s+<br/>次数</td>
            <td width="40px" sort="total_num_slow5">5s+<br/>次数</td>
            <td width="80px" sort="last_updatetime">更新时间</td>
            <td width="40px"></td>
        </tr>
        </thead>
        <tbody id="tbody">
        <#list speeds as m>
            <tr ${m.isHighlight()?string("class='t4'","")}>
                <td style="text-align: left;"><a href="/mot/service/check?s=${m.tag}@${m.name}" target="_blank">${m.tag}@${m.name}</a></td>
                <td style="text-align: right;">${m.average}</td>
                <td style="text-align: right;" ${(m.fastest>1000)?string("class='t4'","")}>${m.fastest}</td>
                <td style="text-align: right;" ${(m.slowest>1000)?string("class='t4'","")}>${m.slowest}</td>
                <td style="text-align: right;">${m.total_num}</td>
                <td style="text-align: right;">${m.total_num_slow1}</td>
                <td style="text-align: right;">${m.total_num_slow2}</td>
                <td style="text-align: right;">${m.total_num_slow5}</td>
                <td>${m.last_updatetime?string('dd HH:mm')}</td>
                <td><a href="/mot/speed/charts?tag=${m.tag}&name_md5=${m.name_md5?url}&service=${m.service}" style="color:blue;cursor:pointer;">详情</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</datagrid>
</main>
</body>
</html>