<!DOCTYPE HTML>
<html class="frm10">
<head>
    <title>${app} - 查询简报</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <style>
        tabbar button{
            cursor: pointer;
        }
        toolbar cell button{
            cursor: pointer;
        }
        toolbar cell input{
            margin-right: 5px;
        }
    </style>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        var Id = 0;
        function editSQL(type) {
            if (type==0){
                location.href="/dev/report/edit?row_id=0";
            } else {
                if (Id==0){
                    layer.msg("未选择脚本");
                    return;
                }
                location.href="/dev/report/edit?row_id="+Id;
            }
        };
        function doSQL(row_id,obj) {
            $('#params-toolbar').css("display","none");
            $('#query_rst').empty();
            $.ajax({
                type: "POST",
                url: "/dev/report/ajax/getConditionBuild",
                data: {
                    "row_id": row_id
                },
                success: function (data) {
                    if(data.hasCondition){
                        $('#params-toolbar').css("display","block");
                        $('#params').empty();
                        $('#params').html(data.htm+'<button id="queryBtn" onclick="query(this)" type="button">查询</button>');

                        //直接查
                        query(document.getElementById("queryBtn"));
                    }else{
                        $.post("/dev/report/ajax/do",{row_id:row_id,is_condition:false},function(rst){
                            $("#query_rst").html(rst);
                        });
                    }
                }
            });

            if (Id==0){
                $("#edit").removeClass("minor");
                $("#edit").addClass("edit");
            }
            Id = row_id;
            $(obj).siblings().removeClass("sel");
            $(obj).addClass("sel");
        };

        function query(obj) {
            var list = $(obj).siblings();
            var len = list.length;
            var data = {};
            for(i=0;i<len;i++){
                var input = list[i];
                var alt = '@'+input.alt;
                var v = input.value;
                data[alt] = v;
            }
            console.log(data);

            $.post("/dev/report/ajax/do",{row_id:Id,is_condition:true,conditon_param:JSON.stringify(data)},function(rst){
                $("#query_rst").html(rst);
            });
        };
    </script>
</head>
<body>
<main>
    <block>
        <tabbar>
            <#list reports as m>
                <button onclick="doSQL(${m.row_id!},this)" type="button">${m.name!}</button>
            </#list>
        </tabbar>
        <#if is_admin==1>
            <button id="add" onclick="editSQL(0)" type="button"  class="edit">新增</button>&nbsp;
            <button id="edit" onclick="editSQL(1)" type="button"  class="minor">编辑</button>
        </#if>
    </block>

    <toolbar style="display: none" id="params-toolbar">
        <cell id="params"></cell>
    </toolbar>
    <datagrid>
        <div id="query_rst" style="overflow-x: scroll"></div>
    </datagrid>
</main>
</body>
</html>