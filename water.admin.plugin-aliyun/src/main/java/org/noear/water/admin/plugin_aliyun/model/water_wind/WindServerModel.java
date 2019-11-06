package org.noear.water.admin.plugin_aliyun.model.water_wind;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

@Getter
public class WindServerModel implements IBinder
{
    public int server_id;
    public String tag;
    public String name;
    public String iaas_key;
    public int iaas_type;
    public String iaas_account;
    public String address;
    public String address_local;
    public String hosts_local;
    public String note;
    public int env_type;
    public int is_enabled;

    public long counts;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        server_id = s.get("server_id").value(0);
        tag = s.get("tag").value(null);
        name = s.get("name").value(null);
        iaas_key = s.get("iaas_key").value(null);
        iaas_type = s.get("iaas_type").value(0);
        iaas_account = s.get("iaas_account").value(null);
        address = s.get("address").value(null);
        address_local = s.get("address_local").value(null);
        hosts_local = s.get("hosts_local").value(null);
        note = s.get("note").value(null);
        env_type = s.get("env_type").value(0);
        is_enabled = s.get("is_enabled").value(0);
        counts = s.get("counts").value(0L);
	}
	
	public IBinder clone()
	{
		return new WindServerModel();
	}

	public String env_type_str() {
	    switch (env_type){
            case 1:return "预生产";
            case 2:return "生产";
           default:return "测试";
        }
    }

    public String iaas_type_str(){
	    switch (iaas_type){
            case 1:return "LBS";
            case 2:return "RDS";
            case 3:return "Redis";
            case 4:return "Memcached";
            case 5:return "DRDS";
            default:return "ECS";
        }
    }
}