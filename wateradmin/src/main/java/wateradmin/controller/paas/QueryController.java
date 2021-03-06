package wateradmin.controller.paas;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Context;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.db.DbPaaSApi;
import wateradmin.models.water_paas.PaasFileModel;
import wateradmin.models.water_paas.PaasFileType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Mapping("/paas/query")
public class QueryController extends BaseController {
    @Mapping("")
    public ModelAndView list(Context ctx) throws SQLException {
        String key = ctx.param("key", "");
        int act = ctx.paramAsInt("act",11);

        key = Base64Utils.decode(key);

        List<PaasFileModel> list = null;
        if(TextUtils.isEmpty(key)){
            list = new ArrayList<>();
        }else{
            list = DbPaaSApi.getFileList(null, PaasFileType.all, false, key, act);
        }


        viewModel.put("key", key);


        viewModel.put("mlist", list);

        return view("paas/query");
    }
}
