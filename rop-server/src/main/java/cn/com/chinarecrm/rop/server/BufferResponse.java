package cn.com.chinarecrm.rop.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class BufferResponse extends HttpServletResponseWrapper{
	 private ByteArrayOutputStream bout = new ByteArrayOutputStream();
	    private PrintWriter pw;
	    private HttpServletResponse response;
	    public BufferResponse(HttpServletResponse response) {
	        super(response);
	        this.response = response;
	    }
	    @Override
	    public ServletOutputStream getOutputStream() throws IOException {
	        return new MyServletOutputStream(bout);
	    }
	    @Override
	    public PrintWriter getWriter() throws IOException {
	        pw = new PrintWriter(new OutputStreamWriter(bout,this.response.getCharacterEncoding()));
	        return pw;
	    }

	    public byte[] getBuffer(){
	        try{
	            if(pw!=null){
	                pw.close();
	            }
	            if(bout!=null){
	                bout.flush();
	                return bout.toByteArray();
	            }


	            return null;
	        }catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }
	    
	    class MyServletOutputStream extends ServletOutputStream{
	    	 private ByteArrayOutputStream bout;
	    	    public MyServletOutputStream(ByteArrayOutputStream bout){
	    	        this.bout = bout;
	    	    }

	    	    @Override
	    	    public void write(int b) throws IOException {
	    	        this.bout.write(b);
	    	    }

				@Override
				public boolean isReady() {
					return false;
				}

				@Override
				public void setWriteListener(WriteListener writeListener) {
					
				}
	    }
}
