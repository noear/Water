<table>
    <thead>
    <tr>
        <td width="120px" class="left">名称</td>
        <td class="left">地址</td>
        <td width="50px">检测<br/>类型</td>
        <td>检查路径</td>
        <td width="120px">最后检查时间</td>
        <td width="60px">最后检<br/>查状态</td>
        <td width="60px">最后检<br/>查备注</td>
        <#if is_admin == 1>
            <td width="170px">操作</td>
        <#else>
            <td width="80px">操作</td>
        </#if>
    </tr>
    </thead>
    <tbody id="tbody" >
    <#list services as m>
        <#if m.check_last_state == 1>
            <tr style="color: red" title="${m.code_location!}">
            <#else>
            <tr title="${m.code_location!}">
        </#if>
        <td class="left">${m.name}</td>
        <td class="left break">
            <#if m.check_type == 0>
                <a href="/mot/service/check?s=${m.name}@${m.address}" target="_blank">
                    ${m.address}
                    <#if m.note?default("")?length gt 0>
                        - ${m.note}
                    </#if>
                </a>
            <#else>
                ${m.address}
                <#if m.note?default("")?length gt 0>
                    - ${m.note}
                </#if>
            </#if>
        </td>
        <td>
            <#if m.check_type == 0>
                被动
            </#if>
            <#if m.check_type == 1>
                主动
            </#if>
        </td>
        <td class="left">
            <#if m.check_url?default('')?length gt 0 >
                <a href="/mot/service/runcheck?s=${m.name}@${m.address}" target="_blank">
                    ${m.check_url!}
                </a>
            </#if>
        </td>
        <td style='${m.isAlarm()?string("color:red","")}'>${(m.check_last_time?string('MM-dd HH:mm:ss'))!}</td>
        <td>
            <#if m.check_last_state == 0>
                ok
            </#if>
            <#if m.check_last_state == 1>
                error
            </#if>
        </td>
        <td class="left">${m.check_last_note!}</td>
        <#if is_admin == 1>
            <td class="op">
                <a class="t2" onclick="deleteService('${m.service_id}')">删除</a> |
                <#if m.is_enabled == 1>
                    <a class="t2" onclick="disableService('${m.service_id}',0)">禁用</a>
                </#if>
                <#if m.is_enabled == 0>
                    <a class="t2" onclick="disableService('${m.service_id}',1)">启用</a>
                </#if>
                |
                <a href="/log/query/inner?tag_name=water&logger=water_log_sev&level=0&tagx=sev@${m.address}" class="t2">日志</a>
                |
                <a href="/mot/speed/charts?tag=service&name_md5=${m.service_md5()}&service=_waterchk" class="t2">监控</a>
            </td>
        </#if>
        </tr>
    </#list>
    </tbody>
</table>