<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - ${fun_name} ${currenttime!}</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        header aside a{display:inline-block; height:100%; border-left:1px solid #444; padding:0 15px; margin-left:15px;}
    </style>
</head>
<body>
<@header/>
<main>
    <#if fun_type = 0>
        <left>
            <@leftmenu/>
        </left>
        <right class="frm">
            <iframe src="${fun_url}" frameborder="0" name="dock"></iframe>
        </right>
    <#else>
        <iframe src="${fun_url}" frameborder="0" name="dock"></iframe>
    </#if>
</main>
<@footer/>
</body>
</html>






