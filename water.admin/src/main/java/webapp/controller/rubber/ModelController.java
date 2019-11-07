package webapp.controller.rubber;

import com.alibaba.fastjson.JSONObject;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.tools.TextUtils;
import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.DbContext;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import webapp.Config;
import webapp.dao.BcfTagChecker;
import webapp.dao.db.DbRubberApi;
import webapp.dao.db.DbWaterApi;
import webapp.models.water.ConfigModel;
import webapp.models.water_rebber.ModelFieldModel;
import webapp.models.water_rebber.ModelModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@XController
@XMapping("/rubber/")
public class ModelController extends BaseController {

    //计算模型
    @XMapping("model")
    public ModelAndView paas(Integer model_id,Integer field_id,String tag_name,String name,String f) throws SQLException {
        List<ModelModel> tags = DbRubberApi.getModelTags();

        BcfTagChecker.filter(tags, m -> m.tag);

        viewModel.put("tags",tags);
        if (TextUtils.isEmpty(tag_name) == false) {
            viewModel.put("tag_name", tag_name);
        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag_name", tags.get(0).tag);
            } else {
                viewModel.put("tag_name", null);
            }
        }
        viewModel.put("name",name);
        viewModel.put("model_id",model_id);
        viewModel.put("field_id",field_id);
        viewModel.put("f",f);

        return view("rubber/model");
    }

    //数据模型右侧列表
    @XMapping("model/inner")
    public ModelAndView inner(Integer model_id, Integer field_id,String tag_name, String name,String f) throws SQLException {

        if(field_id!=null && field_id>0){
            return fieldEdit(model_id,field_id,f);
        }

        if (model_id != null && model_id > 0) {
            return edit(model_id,f);
        }

        List<ModelModel> models = DbRubberApi.getModelList(tag_name, name);
        viewModel.put("models", models);
        viewModel.put("tag_name", tag_name);
        viewModel.put("name", name);
        viewModel.put("f",f);

        viewModel.put("raas_uri", Config.raas_uri);

        return view("rubber/model_inner");
    }


    //修改数据模型
    @XMapping("model/edit")
    public ModelAndView edit(Integer model_id,String f) throws SQLException{
        if(model_id == null){
            model_id = 0;
        }

        List<ConfigModel> configs= DbWaterApi.getDbConfigs();
        List<String> option_sources = new ArrayList<>();
        for(ConfigModel config : configs){
            option_sources.add(config.tag+"/"+config.key);
        }
        viewModel.put("option_sources",option_sources);


        ModelModel model = DbRubberApi.getModelById(model_id);
        viewModel.put("model",model);
        viewModel.put("raas_uri", Config.raas_uri);
        viewModel.put("f", f);

        return view("rubber/model_edit");
    }

    //数据模型保存编辑
    @XMapping("model/edit/ajax/save")
    public JSONObject editSave(Integer model_id,String tag,String name,String name_display,String init_expr,String debug_args,String related_db) throws SQLException{
        JSONObject resp = new JSONObject();
        boolean result = DbRubberApi.setModel(model_id, tag, name, name_display, init_expr,debug_args,related_db) > 0;

        if (result) {
            resp.put("code",1);
            resp.put("msg","编辑成功");
        } else {
            resp.put("code",0);
            resp.put("msg","编辑失败");
        }

        return resp;
    }

    //数据模型删除
    @XMapping("model/edit/ajax/del")
    public ViewModel modelDel(Integer model_id) throws SQLException{
        boolean result = DbRubberApi.delModel(model_id);
        if (result) {
            viewModel.code(1, "删除成功！");
        } else {
            viewModel.code(0, "删除失败!");
        }

        return viewModel;
    }


    //数据模型字段列表
    @XMapping("model/field")
    public ModelAndView field(Integer model_id,String name,String f) throws SQLException{
        ModelModel model = DbRubberApi.getModelById(model_id);
        List<ModelFieldModel> fields = DbRubberApi.getFieldList(model_id, name);
        viewModel.put("model",model);
        viewModel.put("fields",fields);
        viewModel.put("name",name);

        viewModel.put("raas_uri", Config.raas_uri);
        viewModel.put("f",f);

        return view("rubber/model_field");
    }


    //数据模型字段编辑页面
    @XMapping("model/field/edit")
    public ModelAndView fieldEdit(Integer model_id,Integer field_id,String f) throws SQLException{
        if(field_id == null){
            field_id = 0;
        }

        ModelFieldModel field = DbRubberApi.getFieldById(field_id);
        ModelModel model = DbRubberApi.getModelById(model_id);

        viewModel.put("field",field);
        viewModel.put("model_id",model_id);
        viewModel.put("model_name",model.name_display);

        viewModel.put("model",model);

        viewModel.put("raas_uri", Config.raas_uri);
        viewModel.put("f",f);


        return view("rubber/model_field_edit");
    }



    //数据模型字段保存编辑
    @XMapping("model/field/edit/ajax/save")
    public JSONObject fieldEditSave(Integer model_id,Integer field_id,String name,String name_display,
                                    String expr,String note,Integer is_pk) throws SQLException{
        JSONObject resp = new JSONObject();
        boolean result = DbRubberApi.setModelField(model_id,field_id,name,name_display,expr,note,is_pk);

        if (result) {
            resp.put("code",1);
            resp.put("msg","编辑成功");
        } else {
            resp.put("code",0);
            resp.put("msg","编辑失败");
        }

        return resp;
    }

    //删除模型字段
    @XMapping("model/field/del/ajax/save")
    public JSONObject fieldDelSave(Integer field_id,Integer model_id) throws SQLException{
        JSONObject resp = new JSONObject();
        boolean result = DbRubberApi.delModelField(field_id,model_id);

        if (result) {
            resp.put("code",1);
            resp.put("msg","删除成功");
        } else {
            resp.put("code",0);
            resp.put("msg","删除失败");
        }

        return resp;
    }

    //数据模型字段另存为
    @XMapping("model/edit/ajax/saveAs")
    public ViewModel modelEditSaveAs(String tag,Integer model_id,String name,String name_display,String debug_args,String init_expr,String related_db) throws SQLException{
        boolean result = DbRubberApi.saveAsModel(tag,model_id,name,name_display,debug_args,init_expr,related_db);

        if (result) {
            viewModel.code(1, "操作成功！");
        } else {
            viewModel.code(0, "操作失败!");
        }

        return viewModel;
    }
}
