<!doctype html>
<html lang="zh_CN">
<head>
    <meta charset="UTF-8">
    <title>源码：${m1.path!}</title>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/base64.js" ></script>
    <script src="${js}/jtadmin.js?v=4"></script>
    <script src="${js}/layer.js"></script>
    <script src="/monaco-editor/0.22.3/min/vs/loader.min.js"></script>
<#--    <script src="//cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.20.0/min/vs/loader.min.js" ></script>-->

    <style>
        html,body{margin:0px;padding:0px;overflow:hidden;}
        main{margin:10px;}

        .btn2 { background-color: #fd6721; color: #fff; border: none; min-width: 120px; height:30px; font-size: 12px; }
        .btn2:hover { background-color: #fd7f38; }
        .btn2:disabled { background-color: #aaa; }

        main > pre{border:1px solid #C9C9C9; margin: 0px;}

        em{color:#999;font-style:normal;}
        .code_run{color:#999;text-decoration : none}
    </style>
    <script>
        var base64 = new Base64();

        function file_save() {
            var fc = window.editor.getValue();
            var fc64 = base64.encode(fc);

            ajaxPost({url:"./code/ajax/save", data:{'fc64':fc64, 'id':${id}, 'path':'${m1.path!}'}});
        }
        <#if is_admin = 1>
        ctl_s_save_bind(document, file_save);
        </#if>
    </script>
</head>
<body>
<main>
    <pre id="editor" style="height: calc(100vh - 80px); "></pre>

    <flex style="margin-top: 18px;">
        <left class="col-6">
            <#if is_admin = 1>
            <button class="btn2" type="button" onclick="file_save()">保存</button><em>（或 ctrl + s）</em>
            </#if>
            <a href="${paas_uri}${m1.path!}?_debug=1" class="code_run" onclick="return confirm('确定要调试吗？')" target="_blank">debug</a>
        </left>
        <right class="col-6">
            <@versions table="paas_file" keyName="file_id" keyValue="${m1.file_id}">
                window.editor.setValue(m.content);
            </@versions>
        </right>
    </flex>


    <script>
        var code64 = "${code64}";

        require.config({ paths: { 'vs': '/monaco-editor/0.22.3/min/vs' }});
        require(['vs/editor/editor.main'], function() {
            $.get("/_static/luffy.d.txt?v=1",(rst)=>{
                monaco.languages.typescript.javascriptDefaults.addExtraLib(rst);
            });

            window.editor = monaco.editor.create(document.getElementById('editor'), {
                value:base64.decode(code64),
                language: '${edit_mode}'
            });
        });
    </script>
</main>
</body>
</html>