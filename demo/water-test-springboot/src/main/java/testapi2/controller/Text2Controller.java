package testapi2.controller;

import org.noear.solon.Utils;
import org.noear.water.WaterClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author noear 2020/12/28 created
 */
@RestController
public class Text2Controller {
    @RequestMapping("/test2")
    public String home(String msg) throws Exception {
        if (Utils.isNotEmpty(msg)) {
            WaterClient.Message.sendMessage("test.hello", "test2-"+msg);
            return "OK: *" + WaterClient.waterTraceId();
        }else{
            return "NO";
        }
    }
}