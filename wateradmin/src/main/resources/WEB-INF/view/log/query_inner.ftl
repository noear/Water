<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 日志查询</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        function queryForm() {
            location.href = "/log/query/inner?logger="+$('#logger').val()+"&tag_name=${tag_name}";
        }

        function queryTag() {
            location.href = "/log/query/inner?tag_name=${tag_name}";
        }

        $(function (){
            $(".log a").click(function (){
                let traceId = $(this).attr('traceId');
                if(traceId){
                    UrlQueryBy("tagx",traceId,'page');
                }
            });
        });
    </script>
    <style>
        body > header agroup{font-size: 16px;}

        .level5,.level5 a{color:red;}
        .level4,.level4 a{color:orange;}
        .level3,.level3 a{color:green;}
        .level2,.level2 a{color:blue;}

        .log a{text-decoration:underline; cursor: default;}
    </style>
</head>
<body>

<main>
    <toolbar>
        <left>
            <form accept-charset="UTF-8">
                <input type="hidden" name="tag_name" value="${tag_name}">
                <select id="logger" name="logger" onchange="queryForm();">
                    <option value="">选择服务日志</option>
                    <#list logs as m>
                        <option value="${m.logger}">${m.logger} (${m.row_num_today})</option>
                    </#list>
                </select>&nbsp;&nbsp;
                标签：<input type="text" class="w250"  name="tagx" placeholder="Tag@Tag1@Tag2@Tag3 or *TraceId" id="tagx"/>&nbsp;&nbsp;
                时间：<input type="text"  name="log_fulltime" placeholder="yyyy-MM-dd HH:MM:ss.SSS" id="log_fulltime" style="width: 180px;"/>&nbsp;&nbsp;

                <button type="submit">查询</button>
                <script>
                    <#if log??>
                    $('#logger').val('${logger!}');
                    </#if>
                </script>
            </form>
        </left>
        <right>
            <@stateselector stateKey="level" items="ALL,TRACE,DEBUG,INFO,WARN,ERROR"/>
        </right>

    </toolbar>

    <div id="content">
        <#list list as log>
            <div class="break log">
                ${log.html()!}
            </div>
            <br>
        </#list>
    </div>
</main>

</body>
</html>