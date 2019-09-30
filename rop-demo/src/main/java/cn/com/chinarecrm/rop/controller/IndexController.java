package cn.com.chinarecrm.rop.controller;

import org.nutz.lang.util.NutMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.com.chinarecrm.rop.server.ROP;

@RestController
@ROP
public class IndexController {

    @GetMapping("test")
    public NutMap test(@RequestParam long id) {
        return NutMap.NEW().addv("a", 1).addv("id", id);
    }
}
