package cn.com.chinarecrm.rop.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import cn.com.chinarecrm.rop.ROPConfig;
import cn.com.chinarecrm.rop.core.signer.AppsecretFetcher;
import cn.com.chinarecrm.rop.core.signer.DigestSigner;
import cn.com.chinarecrm.rop.core.signer.Signer;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.
 *
 *         import com.chinare.rop.core.signer.AppsecretFetcher; import
 *         com.chinare.rop.core.signer.DigestSigner;com.cn)
 */
@Aspect
public class ROPSignInterceptor {

	@Autowired
	RequestChecker checker;

	String digestName;

	@Autowired
	AppsecretFetcher fetcher;

	@Autowired
	HttpServletRequest request;

	@Autowired
	HttpServletResponse response;

	CacheMap<String, Boolean> cacheMap;
	
	boolean check;

	public ROPSignInterceptor(String digestName, long timeout, boolean check) {
		this.digestName = digestName;
		this.check = check;
		this.cacheMap = new CacheMap<>(timeout * 1000);
	}

	@Around("@within(com.chinare.rop.server.ROP)|| @annotation(com.chinare.rop.server.ROP)")
	public Object filter(ProceedingJoinPoint point) throws Throwable {
		String nonce = request.getHeader(ROPConfig.NONCE_KEY);
		if ( check && Boolean.TRUE.equals(cacheMap.get(nonce))) {
			throw new ROPException("replay check failed");
		}
		Signer signer = new DigestSigner(digestName);
		if (checker.check(request) && signer.check(request, fetcher)) {
			return point.proceed();
		} else {
			throw new ROPException("checkSign failed");
		}
	}

}
