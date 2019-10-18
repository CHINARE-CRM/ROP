package cn.com.chinarecrm.rop.server;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
public interface RequestChecker {

    public boolean check(HttpServletRequest request);
}
