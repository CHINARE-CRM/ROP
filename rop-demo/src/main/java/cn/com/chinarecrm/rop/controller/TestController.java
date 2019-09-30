package cn.com.chinarecrm.rop.controller;

import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
@RestController
public class TestController {

    @GetMapping("test1")
    public NutMap test() {
        return NutMap.NEW().addv("r", R.UU16());
    }
}
