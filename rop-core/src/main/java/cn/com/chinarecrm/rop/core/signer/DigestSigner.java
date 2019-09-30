package cn.com.chinarecrm.rop.core.signer;

import java.util.Arrays;

import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
public class DigestSigner extends AbstractSigner {

    private String name;

    /**
     *
     */
    public DigestSigner() {
        this.name = "MD5";
    }

    public DigestSigner(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String sign(String appSecret, String timestamp, String gateway, String nonce, String dataMate) {
        String[] temp = Lang.array(appSecret, timestamp, gateway, nonce, dataMate);
        Arrays.sort(temp);
        log.debugf("sign with %s args %s", name(), Json.toJson(temp));
        return Lang.digest(name(), Strings.join("", temp));
    }
}
