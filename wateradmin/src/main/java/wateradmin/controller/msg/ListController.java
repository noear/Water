package wateradmin.controller.msg;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.solon.core.XContext;
import org.noear.water.utils.DisttimeUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.Session;
import wateradmin.dso.db.DbWaterMsgApi;
import wateradmin.models.water_msg.DistributionModel;
import wateradmin.models.water_msg.MessageModel;
import wateradmin.models.water_msg.SubscriberModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@XController
public class ListController extends BaseController {

    //消息异常记录
    @XMapping("/msg/list")
    public ModelAndView list(XContext ctx, String key) throws SQLException {
        Integer _m = ctx.paramAsInt("_m", 0);

        List<MessageModel> list = DbWaterMsgApi.getMessageList(_m, key);


        viewModel.put("key",key);
        viewModel.put("_m",_m);
        viewModel.put("list",list);
        viewModel.put("currTime", DisttimeUtils.currTime());
        return view("msg/list");
    }

    //派发功能ajax
    @XMapping("/msg/ajax/distribute")
    public ViewModel distribute(String ids) throws SQLException {
        int is_admin = Session.current().getIsAdmin();
        if (is_admin == 1) {
            boolean result = DbWaterMsgApi.msgDistribute(idList(ids));
            if (result) {
                viewModel.code(1, "派发成功！");
            } else {
                viewModel.code(0, "派发失败！");
            }
        } else {
            viewModel.code(0, "没有权限！");
        }

        return viewModel;
    }

    //取消派发
    @XMapping("/msg/ajax/cancelSend")
    public ViewModel cancelSend(String ids) throws SQLException{

        int is_admin = Session.current().getIsAdmin();

        if (is_admin == 1) {
            boolean result = DbWaterMsgApi.cancelSend(idList(ids));

            if (result) {
                viewModel.code(1, "取消成功");
            }  else {
                viewModel.code(0, "取消失败");
            }
        } else {
            viewModel.code(0, "没有权限！");
        }

        return viewModel;
    }

    //异常记录中 修复订阅功能的ajax
    @XMapping("/msg/ajax/repair")
    public ViewModel repairSubs(String ids) throws SQLException {
        int is_admin = Session.current().getIsAdmin();
        if (is_admin == 1) {
            boolean error = false;

            List<DistributionModel> list = DbWaterMsgApi.repairSubs1(idList(ids));
            if (!list.isEmpty()) {
                for (DistributionModel dis : list) {
                    //查询subscriber的url
                    SubscriberModel subs = DbWaterMsgApi.repairSubs2(dis.subscriber_id);
                    boolean result = false;
                    if (subs.subscriber_id > 0) {
                        //更新distribution的url
                        result = DbWaterMsgApi.repairSubs3(dis.dist_id, subs.receive_url);
                    }

                    if (!result) {
                        error = true;
                    }
                }
            } else {
                error = true;
            }

            if (error == false) {
                viewModel.code(1, "修复成功！");
            } else {
                viewModel.code(0, "修复失败！");
            }
        } else {
            viewModel.code(0, "没有权限！");
        }

        return viewModel;
    }

    private List<Object> idList(String ids){
        return  Arrays.asList(ids.split(","))
                .stream()
                .map(s->Long.parseLong(s))
                .collect(Collectors.toList());
    }
}