package cn.com.chinarecrm.rop.server;

import javax.servlet.http.HttpServletResponse;

import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cn.com.chinarecrm.rop.core.ROPData;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
@RestController
@ControllerAdvice(annotations = ROP.class)
public class ROPExceptionHandler {

    Log log = Logs.get();

    @ExceptionHandler(value = ROPException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ROPData rop(HttpServletResponse response, ROPException e) {
        log.error("error=>", e);
        return ROPData.exception(e);
    }
}
