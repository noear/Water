package wateradmin.controller.dev;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.noear.snack.ONode;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.ConfigType;
import wateradmin.dso.EsUtil;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.utils.JsonFormatTool;
import wateradmin.models.water_cfg.ConfigModel;

import java.sql.SQLException;
import java.util.List;

//非单例
@Controller
@Mapping("/dev/query_es")
public class QueryEsController extends BaseController {
    @Mapping("")
    public ModelAndView query() throws SQLException {
        List<ConfigModel> list = DbWaterCfgApi.getConfigTagKeyByType(null, ConfigType.elasticsearch);

        viewModel.put("cfgs", list);

        return view("dev/query_es");
    }

    @Mapping(value = "ajax/do")
    public String query_do(String code) {
        String tmp = query_exec(code);
        if (tmp != null && tmp.startsWith("{")) {
            return JsonFormatTool.formatJson(tmp);
        } else {
            return tmp;
        }
    }

    public String query_exec(String code) {
        ONode node = new ONode();
        //1.对不良条件进行过滤
        if (TextUtils.isEmpty(code)) {
            return node.val("请输入代码").toJson();
        }

        if (code.indexOf("#") < 0)
            return node.val("请指定配置").toJson();

        String[] ss = code.trim().split("\n");

        if (ss.length < 3) {
            return node.val("请输入有效代码").toJson();
        }

        //1.检查配置
        String cfg_str = ss[0].trim();
        if (cfg_str.startsWith("#") == false || cfg_str.indexOf("/") < 0) {
            return node.val("请输入有效的配置").toJson();
        } else {
            cfg_str = cfg_str.substring(1).trim();
        }

        //2.检查 methon 和 path
        String methodAndPath = ss[1].trim().replaceAll("\\s+", " ");
        String[] ss2 = methodAndPath.split(" ");

        if (ss2.length != 2) {
            return node.val("请输入有效的Method和Path").toJson();
        }

        String method = ss2[0].trim().toUpperCase();
        String path = ss2[1].trim();

        if ((method.equals("GET") && path.endsWith("_search")) == false) {
            return node.val("只支持查询操作").toJson();
        }


        //3.检查 json
        StringBuilder json = new StringBuilder();
        for (int i = 2, len = ss.length; i < len; i++) {
            json.append(ss[i]);
        }

        if( ONode.loadStr(json.toString()).isObject()==false){
            return node.val("请输入有效的Json代码").toJson();
        }

        try {
            ConfigModel cfg = DbWaterCfgApi.getConfigByTagName(cfg_str);

            return EsUtil.search(cfg, method, path, json.toString());
        } catch (Exception ex) {
            return JSON.toJSONString(ex,
                    SerializerFeature.BrowserCompatible,
                    SerializerFeature.WriteClassName,
                    SerializerFeature.DisableCircularReferenceDetect);
        }
    }
}

