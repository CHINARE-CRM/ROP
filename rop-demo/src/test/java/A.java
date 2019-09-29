import org.nutz.http.Response;
import org.nutz.http.Request.METHOD;
import org.nutz.lang.Lang;

import cn.com.chinarecrm.rop.client.ROPClient;
import cn.com.chinarecrm.rop.client.ROPRequest;

public class A {
	public static void main(String[] args) {
		ROPClient client = ROPClient.create("test", Lang.md5("test"), "http://127.0.0.1:8080/rop.endpoint", "SHA1");
		Response response=	client.send(ROPRequest.create("/test", METHOD.GET));
		System.err.println(response.getContent());
	}
}
