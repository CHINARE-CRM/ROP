package cn.com.chinarecrm.rop.server;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
public class NullRequestChecker implements RequestChecker {

    @Override
    public boolean check(HttpServletRequest request) {
        return true;
    }

}
