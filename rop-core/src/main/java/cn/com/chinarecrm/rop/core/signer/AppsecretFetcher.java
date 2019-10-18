package cn.com.chinarecrm.rop.core.signer;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
public interface AppsecretFetcher {

    public String fetch(String key);
}
