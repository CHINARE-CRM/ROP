package cn.com.chinarecrm.rop.server;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.com.chinarecrm.rop.ROPConfig;
import cn.com.chinarecrm.rop.core.ROPData;

/**
 *
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
public class ROPServlet extends HttpServlet {

    static final Log log = Logs.get();
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public Date addSeconds(Date base, long seconds) {
        return Times.D(base.getTime() + seconds * 1000);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.
     * HttpServletRequest , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void service(final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**
         * 1. 时间戳校验 2.签名校验转移到nutz的filter中去处理,这样便于获取ioc中的对象 3.方法校验
         *
         */
        try {
            String method = request.getHeader(ROPConfig.METHOD_KEY);
            if (Strings.isBlank(method)) {// 空方法
                response.getWriter().write(Json.toJson(ROPData.exception("null method")));
                return;
            }
            String timeStemp = request.getHeader(ROPConfig.TS_KEY);
            if (Strings.isBlank(timeStemp)) {
                response.getWriter().write(Json.toJson(ROPData.exception("no timeStemp")));
                return;
            }
            long time = Long.parseLong(timeStemp);
            if (addSeconds(Times.D(time), Long.parseLong(getInitParameter("timeout"))).before(Times.now())) {
                response.getWriter().write(Json.toJson(ROPData.exception("request timeout")));
                return;
            }
            String gateWay = getInitParameter("gateWayUri");
            request.getRequestDispatcher(Strings.isBlank(gateWay) ? method : gateWay)
                   .forward(request, response);// 将请求转发给真实的函数入口
        }
        catch (Exception e) {
            response.getWriter().write(Json.toJson(ROPData.exception(e)));
        }
    }

}
