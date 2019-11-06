<table>
    <thead>
    <tr>
        <td width="120px">名称</td>
        <td width="65px">地址</td>
        <td style="word-wrap:break-word; word-break:break-all;">备注</td>
        <td width="70px">检测类型</td>
        <td>检查地址</td>
        <td width="110px">最后检查时间</td>
        <td width="50px">最后检<br/>查状态</td>
        <td width="50px">最后检<br/>查备注</td>
        <#if is_admin == 1>
            <td width="80px">操作</td>
        </#if>
    </tr>
    </thead>
    <tbody id="tbody" >
    <#list services as m>
                    <#if m.check_last_state == 1>
                        <tr style="color: red">
                    </#if>
        <td class="left">${m.name}</td>
        <td class="left">${m.address}</td>
        <td class="left">${m.note}</td>
        <td>
                            <#if m.check_type == 0>
                                被动检查
                            </#if>
                            <#if m.check_type == 1>
                                主动签到
                            </#if>
        </td>
        <td style="text-align: left">${m.check_url!}</td>
        <td style='${m.isAlarm()?string("color:red","")}'>${(m.check_last_time?string('MM-dd HH:mm:ss'))!}</td>
        <td>
                            <#if m.check_last_state == 0>
                                ok
                            </#if>
                            <#if m.check_last_state == 1>
                                error
                            </#if>
        </td>
        <td style="text-align: left;">${m.check_last_note}</td>
                        <#if is_admin == 1>
                            <td >
                                <a class="t2" onclick="deleteService('${m.service_id}')">删除</a> |
                                <#if m.is_enabled == 1>
                                    <a class="t2" onclick="disableService('${m.service_id}',0)">禁用</a>
                                </#if>
                                <#if m.is_enabled == 0>
                                    <a class="t2" onclick="disableService('${m.service_id}',1)">启用</a>
                                </#if>
                            </td>
                        </#if>
    </tr>
    </#list>
    </tbody>
</table>
