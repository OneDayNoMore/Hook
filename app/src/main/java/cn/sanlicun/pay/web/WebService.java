package cn.sanlicun.pay.web;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.io.File;

public class WebService extends Service {

	private Server server;

	private Handler handler = new Handler();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		startForeground(9999, new Notification());
		startServer();
	}

	@Override
	public void onDestroy() {
		stopServer();
		super.onDestroy();
	}

	private void startServer() {
		if (server != null) {
			Toast.makeText(this, "服务器已经开启", Toast.LENGTH_SHORT).show();
			return;
		}
		new Thread(new StartRunnable()).start();
	}

	private void stopServer() {
		if (server != null) {
			new Thread(new StopRunnable()).start();
		}
	}

	class StartRunnable implements Runnable {
		@Override
		public void run() {
			try {
				File JETTY_DIR = new File(
						Environment.getExternalStorageDirectory(), "jetty");
				// Set jetty.home
				System.setProperty("jetty.home", JETTY_DIR.getAbsolutePath());

				// ipv6 workaround for froyo
				System.setProperty("java.net.preferIPv6Addresses", "false");

				server = new Server(8080);
				// server.setHandler(new DefaultHandler());
				ServletContextHandler contextHandler = new ServletContextHandler(
						ServletContextHandler.SESSIONS);
				contextHandler.setContextPath("/");
				server.setHandler(contextHandler);
				ServlertConfig.config(contextHandler);

				server.start();
				server.join();
			} catch (Exception e) {
				server = null;
				toastOnUiThread("服务器启动失败");
			}
		}
	}

	class StopRunnable implements Runnable {
		@Override
		public void run() {
			try {
				server.stop();
				server = null;
				toastOnUiThread("服务器关闭");
			} catch (Exception e) {
				e.printStackTrace();
				toastOnUiThread("服务器关闭失败");
			}
		}
	}
	
	private void toastOnUiThread(final String toast) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(WebService.this, toast,
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
