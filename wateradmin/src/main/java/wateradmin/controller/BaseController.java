package wateradmin.controller;


import org.noear.solon.annotation.Singleton;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.Config;
import wateradmin.dso.Session;
import wateradmin.utils.IPUtil;
import wateradmin.viewModels.ViewModel;


/**
 * Created by noear on 14-9-11.
 */
@Singleton(false)
public abstract class BaseController {

    //获取 StateSelectorTag 传来的值
    public int getState(Context request)
    {
        return getInt(request,"_state");
    }

    public int getInt(Context request, String key) {
        return request.paramAsInt(key,-1);
    }


    /*视图数据模型*/
    protected ViewModel viewModel = new ViewModel();

    /*分页默认长度(适合内容单行的列表)*/
    protected static int pageBigSize = 16;
    /*分页默认长度(适合内容两行的列表)*/
    protected static int pageSmlSize = 6;

    /*
    * @return 输出一个视图（自动放置viewModel）
    * @param viewName 视图名字(内部uri)
    * */
    public ModelAndView view(String viewName)
    {
        //设置必要参数
        viewModel.put("root", "");

        viewModel.put("app", Config.web_title);

        viewModel.put("css", "/_static/css");
        viewModel.put("js", "/_static/js");
        viewModel.put("img", "/_static/img");
        viewModel.put("title",Config.web_title);

        //当前用户信息(示例)
        viewModel.put("puid", Session.current().getPUID());
        viewModel.put("cn_name", Session.current().getUserName());
        viewModel.put("is_admin", Session.current().getIsAdmin());
        viewModel.put("is_operator", Session.current().getIsOperator());


        viewModel.put("ref_url", Context.current().header("referer"));
        viewModel.put("paas_uri",Config.paas_uri());
        viewModel.put("raas_uri",Config.raas_uri());

        return viewModel.view(viewName+".ftl");
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
