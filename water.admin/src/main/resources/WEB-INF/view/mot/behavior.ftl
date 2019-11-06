<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 行为记录</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="https://static.kdz6.cn/lib/echarts.min.js" async="async"></script><!-- 起到缓存作用 -->
    <script>
        $(function () {
            if ('${tag_name!}') {
                $('#${tag_name}').addClass('sel');
            } else {
                $('tree li:first').addClass('sel');
            }

        });
        var tagName = '${tag_name}';
        function node_onclick(tag_name,obj) {
            tagName = tag_name
            $('li.sel').removeClass('sel');
            $(obj).addClass("sel");
            $("#table").attr('src',"/mot/behavior/inner?tag_name="+tagName);
        };
    </script>
</head>
<body>
<main>
    <middle>
        <tree id="tree">
            <ul>
                <#list tags as m>
                    <li onclick="node_onclick('${m.tag}',this)" id="${m.tag}"> ${m.tag}</li>
                </#list>
            </ul>
        </tree>
    </middle>
    <right class="frm">
        <iframe src="/mot/behavior/inner?tag_name=${tag_name!}" frameborder="0" id="table"></iframe>
    </right>
</main>

</body>
</html>