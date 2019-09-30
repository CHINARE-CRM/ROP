import org.nutz.http.Request.METHOD;
import org.nutz.http.Response;
import org.nutz.lang.Lang;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;

import cn.com.chinarecrm.rop.client.ROPClient;
import cn.com.chinarecrm.rop.client.ROPRequest;

public class A {
    public static void main(String[] args) {
        ROPClient client = ROPClient.create("test", Lang.md5("test"), "http://127.0.0.1:8080/rop.endpoint", "SHA1");
        // client.setProxy(new Proxy(Type.HTTP, new
        // InetSocketAddress("127.0.0.1", 8888)));
        Response response = client.send(ROPRequest.create("/test", METHOD.GET, NutMap.NEW().addv("id", R.random(0, 100))));
        System.err.println(response.getContent());
    }
}
