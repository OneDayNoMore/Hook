package cn.sanlicun.pay.web;

import org.eclipse.jetty.server.Request;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultHandler extends org.eclipse.jetty.server.handler.DefaultHandler {


	public DefaultHandler() {

	}

	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (response.isCommitted() || baseRequest.isHandled())
			return;

		baseRequest.setHandled(true);
		// String method = request.getMethod();//请求方式
		request.getRequestURI();// 请求路径

		System.out.println("URI" + request.getRequestURI());
		System.out.println("URL" + request.getRequestURI());
		response.setStatus(HttpServletResponse.SC_OK);
		// response.setContentType(MimeTypes.TEXT_JSON);
		String str = "我是返回内容";
		byte[] b = str.getBytes();
		response.setContentLength(b.length);
		OutputStream out = response.getOutputStream();
		out.write(b);
		out.close();
	}
}
