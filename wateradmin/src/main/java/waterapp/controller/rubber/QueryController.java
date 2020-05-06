package waterapp.controller.rubber;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.utils.TextUtils;
import waterapp.controller.BaseController;
import waterapp.dso.db.DbRubberQueryApi;
import waterapp.models.water.CodeQueryModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@XController
@XMapping("/rubber/")
public class QueryController extends BaseController {
    //代码查询视图跳转
    @XMapping("query")
    public ModelAndView plan(Integer code_type,String code) throws SQLException {
        List<CodeQueryModel> list = new ArrayList<>();
        if (code_type != null && TextUtils.isEmpty(code) == false) {
            list = DbRubberQueryApi.codeQuery(code_type, code);
        }
        viewModel.put("list",list);
        return view("rubber/query");
    }

    //查看代码
    @XMapping("query/code")
    public ModelAndView code(Integer id,Integer code_type) throws SQLException{
        String code = DbRubberQueryApi.queryCode(code_type, id);
        code = code.replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
        viewModel.put("code",code);
        return view("rubber/query_code");
    }
}