package cn.com.chinarecrm.rop.core.signer;

import org.nutz.lang.Lang;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
public class DefaultMD5Fetcher implements AppsecretFetcher {

    @Override
    public String fetch(String key) {
        return Lang.md5(key);
    }

}
