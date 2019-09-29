package cn.com.chinarecrm.rop.server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.nutz.lang.Streams;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * @author 王贵源(wangguiyuan@chinarecrm.com.cn)
 */
public class ResettableStreamHttpServletRequest extends HttpServletRequestWrapper {
	public class ResettableServletInputStream extends ServletInputStream {

		private ByteArrayInputStream inputStream;

		public ResettableServletInputStream(byte[] buffer) {
			this.inputStream = new ByteArrayInputStream(buffer);
		}

		@Override
		public int available() throws IOException {
			return inputStream.available();
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public int read() throws IOException {
			return inputStream.read();
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			return inputStream.read(b, off, len);
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			// Do nothing
		}
	}

	static final Log log = Logs.get();

	private byte[] buffer;

	private Map<String, String[]> cachedParamMap;

	public ResettableStreamHttpServletRequest(HttpServletRequest request) throws IOException {
		super(request);
		this.buffer = Streams.readBytes(request.getInputStream());
		this.cachedParamMap = request.getParameterMap();

	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new ResettableServletInputStream(this.buffer);
	}

	@Override
	public String getParameter(String name) {
		return cachedParamMap.get(name) == null ? null : Strings.join(",", cachedParamMap.get(name));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletRequestWrapper#getParameterMap()
	 */
	@Override
	public Map<String, String[]> getParameterMap() {
		return cachedParamMap;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletRequestWrapper#getParameterNames()
	 */
	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(cachedParamMap.keySet());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletRequestWrapper#getParameterValues(java.lang.
	 * String)
	 */
	@Override
	public String[] getParameterValues(String name) {
		return cachedParamMap.get(name);
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

}
