<!doctype html>
<html class="frm10">
<head>
    <title>文件</title>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/jtadmin.js"></script>
    <script src="${js}/layer.js"></script>
    <script>
        window.postReview = function(vm){
            if(vm.path.startsWith("/" + vm.tag) == false){
                alert("路径必须以/{tag}开始");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
<datalist id="label_list">
    <option value="filter.path">路径过滤器；请在备注里添加'路径'，例：/path/#说明</option>
    <option value="filter.file">文件过滤器；请在备注里添加'文件后缀'，例：md#说明</option>
    <option value="hook.start">启动勾子；引擎启动时会被运行</option>
</datalist>
<datalist id="content_type_list">
    <option value="text/plain"></option>
    <option value="text/css"></option>
    <option value="text/xml"></option>
    <option value="text/html"></option>
    <option value="text/javascript"></option>
    <option value="image/jpeg"></option>
    <option value="image/png"></option>
    <option value="image/gif"></option>
    <option value="application/javascript"></option>
    <option value="application/octet-stream"></option>
    <option value="application/json"></option>
    <option value="code/internal">内部代码，不支持http请求</option>
</datalist>

<datalist id="edit_mode_list">
    <option value="javascript"></option>
    <option value="groovy"></option>
    <option value="python"></option>
    <option value="ruby"></option>
    <option value="lua"></option>
    <option value="graaljs"></option>

    <option value="freemarker"></option>
    <option value="velocity"></option>
    <option value="thymeleaf"></option>
    <option value="beetl"></option>
    <option value="enjoy"></option>

    <option value="xml"></option>
    <option value="html"></option>
    <option value="css"></option>
    <option value="mysql"></option>
    <option value="markdown"></option>
    <option value="json"></option>
</datalist>

<toolbar class="blockquote">
    <left class="ln30">
        <h2><a href="javascript:history.back()" class="noline">即时接口</a></h2> / 设置
    </left>
    <right class="form">
        <n>ctrl + s 可快捷保存</n>
        <button type="button" jt-post="./ajax/save" jt-goback="parent.location='./home?tag_name='+$('#tag').val().trim()" jt-ctls>保存</button>
        <#if ((m1.path!'')?length > 1)>

            <button type="button" class="minor" jt-post="./ajax/del?id=${id}" jt-goback="parent.location='./home?tag_name='+$('#tag').val().trim()" jt-confirm="del">删除</button>
        </#if>
    </right>
</toolbar>

<detail>
    <form>
        <input type="hidden" id="id" value="${id}" />
        <table>
            <tbody>
            <tr>
                <th width="100">tag</th><td><input type="text" id="tag"  value="${m1.tag!tag}" required jt-alert="请输入包" />*
                    <n>（字母或数字或_组成）</n>
                </td>
            </tr>

            <tr>
                <th>路径</th>
                <td><input type="text" class="longtxt" id="path" value="${m1.path!('/'+tag)}" required jt-alert="请输入路径"/>*
                    <n-l>例：/{tag}/xxx 或 /{tag}_xxx/xxx</n-l>
                </td>
            </tr>
            <tr>
                <th>编辑模板</th>
                <td><input type="text" id="edit_mode" value="${m1.edit_mode!'javascript'}" list="edit_mode_list" autocomplete="off" required jt-alert="请选择编辑模式" />*</td>
            </tr>
            <tr>
                <th>备注</th><td><input type="text" id="note" value="${m1.note!}"/></td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <checkbox>
                        <label class="mar10-r"><input type="checkbox" id="is_disabled" ${m.is_disabled?string('checked','')} /><a>禁止使用</a></label>
                        <label class="mar10-r"><input type="checkbox" id="is_staticize" ${m.is_staticize?string('checked','')} /><a>静态文件</a></label>
                    </checkbox>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <hr/>
                </td>
            </tr>
            <tr>
                <th>标记</th><td><input type="text" id="label" list="label_list" autocomplete="off" value="${m1.label!}"  /></td>
            </tr>
            <tr>
                <th>跳转到</th>
                <td><input type="text" class="longtxt" id="link_to" value="${m1.link_to!}"/></td>
            </tr>
            <tr>
                <th>内容类型</th>
                <td><input type="text" id="content_type" value="${m1.content_type!}" list="content_type_list" autocomplete="off"/>
                    <n>（动态变化的不要选）</n>
                </td>
            </tr>
            </tbody>
        </table>
    </form>
    </detail>
</body>
</html>