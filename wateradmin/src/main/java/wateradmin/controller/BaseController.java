package wateradmin.controller;


import org.noear.solon.Solon;
import org.noear.solon.annotation.Singleton;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.validation.annotation.Valid;
import org.noear.water.utils.Datetime;
import wateradmin.Config;
import wateradmin.dso.Session;
import wateradmin.viewModels.ViewModel;

import java.time.Instant;
import java.time.LocalDateTime;


/**
 * Created by noear on 14-9-11.
 */
@Valid
@Singleton(false)
public class BaseController {
    /*视图数据模型*/
    protected ViewModel viewModel = new ViewModel();

    /*
    * @return 输出一个视图（自动放置viewModel）
    * @param viewName 视图名字(内部uri)
    * */
    public ModelAndView view(String viewName) {
        //设置必要参数
        viewModel.put("root", "");

        viewModel.put("app", Solon.cfg().appTitle());

        viewModel.put("css", "/_static/css");
        viewModel.put("js", "/_static/js");
        viewModel.put("img", "/_static/img");
        viewModel.put("title", Solon.cfg().appTitle());

        //当前用户信息(示例)
        viewModel.put("puid", Session.current().getPUID());
        viewModel.put("cn_name", Session.current().getUserName());

        viewModel.put("is_setup", Solon.cfg().isSetupMode() ? 1 : 0);

        if (Solon.cfg().isSetupMode()) {
            //支持设置模式
            viewModel.put("is_admin", 1);
            viewModel.put("is_operator", 1);
        } else {
            int is_admin = Session.current().getIsAdmin();
            int is_operator = Session.current().getIsOperator();
            if (is_admin == 1) {
                is_operator = 1;
            }

            viewModel.put("is_admin", is_admin);
            viewModel.put("is_operator", is_operator);

            viewModel.put("paas_uri", Config.paas_uri());
            viewModel.put("raas_uri", Config.raas_uri());
        }


        viewModel.put("currenttime", Datetime.Now().toString("(yyyy-MM-dd HH:mm Z)"));
        viewModel.put("ref_url", Context.current().header("referer"));

        return viewModel.view(viewName + ".ftl");
    }

    /*
    * @return 输出一个跳转视图
    * @prarm  url 可以是任何URL地址
    * */
    public void redirect(String url) {
        try {
            Context.current().redirect(url);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
