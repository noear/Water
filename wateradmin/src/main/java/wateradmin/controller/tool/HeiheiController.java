package wateradmin.controller.tool;

import org.noear.snack.ONode;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.NoticeUtils;
import wateradmin.viewModels.ViewModel;

@Controller
@Mapping("/tool/")
public class HeiheiController extends BaseController {
    @Mapping("heihei")
    public ModelAndView index() {
        return view("tool/heihei");
    }

    //提交嘿嘿调试
    @Mapping("heihei/ajax/submit")
    public ViewModel submit(String msg, String target) throws Exception {

        String response = NoticeUtils.heihei(target, msg);
        ONode obj = ONode.load(response);

        return viewModel.code(obj.get("code").getInt(), obj.get("msg").getString());
    }
}
