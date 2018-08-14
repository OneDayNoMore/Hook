package cn.sanlicun.pay.web;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import cn.sanlicun.pay.web.servlet.PayServlet;


public class ServlertConfig {
	public static void config(ServletContextHandler handler) {
		handler.addServlet(new ServletHolder(new PayServlet()), "/getpay");
	}
}
