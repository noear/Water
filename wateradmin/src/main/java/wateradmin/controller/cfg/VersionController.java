package wateradmin.controller.cfg;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;

import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.db.DbWaterVerApi;
import wateradmin.models.water.VersionModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;

@Mapping("/cfg/")
@Controller
public class VersionController extends BaseController {

    //IP白名单列表
    @Mapping("version/ajax/data")
    public ViewModel whitelist(Integer commit_id) throws SQLException {
        if (commit_id != null && commit_id > 0) {
            VersionModel m = DbWaterVerApi.getVersionByCommit(commit_id);

            if(TextUtils.isEmpty(m.data) == false){
                viewModel.code(1, "OK");
                viewModel.put("data", m.data);
            }
            else{
                viewModel.code(0, "no data!");
            }

        } else {
            viewModel.code(0, "params error");
        }

        return viewModel;
    }
}
