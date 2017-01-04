package com.nsb.enms.restful.adapterserver.api;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.nsb.enms.adapter.server.common.app.ping.PingApp;
import com.nsb.enms.adapter.server.common.app.register.RegisterManager;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.constants.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.notification.NotificationSender;
import com.nsb.enms.adapter.server.filter.AccessControlFilter;
import com.nsb.enms.adapter.server.statemachine.app.NeStateMachineApp;
import com.nsb.enms.adapter.server.wdm.notification.AdpSnmpTrapHandler;
import com.nsb.enms.common.utils.snmp.SnmpTrap;

public class Main {
	private static Logger log;

	public static void main(String[] args) throws AdapterException {
		String confPath = loadConf();

		System.setProperty("log4j.configurationFile", confPath + "/log4j2.xml");
		LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
		loggerContext.reconfigure();

		log = LogManager.getLogger(Main.class);

		String ctrlUrl = ConfLoader.getInstance().getConf(ConfigKey.CTRL_URL);
		log.debug("The ctrlUrl is " + ctrlUrl);
		initControllerApiClient(ctrlUrl);

		String[] packages = new String[] { "io.swagger.jaxrs.listing", "io.swagger.sample.resource",
				"com.nsb.enms.restful.adapterserver.api" };

		ResourceConfig config = new ResourceConfig().packages(packages).register(JacksonFeature.class)
				.register(AccessControlFilter.class);

		ServletHolder servlet = new ServletHolder(new ServletContainer(config));

		int port = ConfLoader.getInstance().getInt(ConfigKey.ADP_PORT, ConfigKey.DEFAULT_ADP_PORT);
		final Server server = new Server(port);
		ServletContextHandler context = new ServletContextHandler(server, "/Adapter/*");
		context.addServlet(servlet, "/*");
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					try {
						server.start();
						server.join();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} finally {
					server.destroy();
				}

			}
		}).start();

		register2Controller();

		// String q3WSServerUri =
		// ConfLoader.getInstance().getConf(ConfigKey.Q3_WS_SERVER_URI);
		// NotificationClient client = new NotificationClient(q3WSServerUri);
		// client.start();

		// The real groupId should be set
		// Q3EmlImMgr.instance().init(100);

		NeStateMachineApp.instance().init();
		NotificationSender.instance().init();

		PingApp pingApp = new PingApp();
		pingApp.checkPing();

		startSnmpTrap();
	}

	/**
	 * 监听SNMP网元的通知
	 */
	private static void startSnmpTrap() {
		SnmpTrap trap = new SnmpTrap();
		trap.setTrapCaller(AdpSnmpTrapHandler.getInstance());
		String ip = ConfLoader.getInstance().getConf(ConfigKey.ADP_IP, "127.0.0.1");
		trap.run(ip, ConfigKey.DEFAULT_SNMP_PORT);
	}

	private static String loadConf() {
		String confPath = System.getenv("ADPCONFPATH");
		try {
			ConfLoader.getInstance().loadConf(confPath + "/conf.properties");
		} catch (AdapterException e) {
			e.printStackTrace();
		}

		return confPath;
	}

	private static void initControllerApiClient(String ctrlUrl) {
		com.nsb.enms.restful.controllerclient.Configuration
				.setDefaultApiClient(new com.nsb.enms.restful.controllerclient.ApiClient().setBasePath(ctrlUrl));
	}

	private static void register2Controller() {
		long period = ConfLoader.getInstance().getInt(ConfigKey.REG_PERIOD, ConfigKey.DEFAULT_REG_PERIOD);
		if(period<=0) return;
		final Timer timer = new Timer();
		final RegisterManager register = new RegisterManager();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				log.debug("start to register to Controller");
				boolean isOk = register.register2Controller();
				log.debug("the result of registering to Controller is :" + isOk);
				// if( isOk )
				// {
				// timer.cancel();
				// }
			}
		}, 0, period);
	}
}
