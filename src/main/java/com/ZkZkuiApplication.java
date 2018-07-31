package com;

import com.deem.zkui.dao.Dao;
import com.huang.service.ZookeeperMain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

@Slf4j
// 通过@ServletComponentScan自动扫描@WebServlet, @WebFilter, @WebListener
@ServletComponentScan
@SpringBootApplication
public class ZkZkuiApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) throws IOException {
		SpringApplicationBuilder sab = new SpringApplicationBuilder(ZkZkuiApplication.class);
		// if not set, auto detected by classpath
		sab.web(true);
		WebApplicationContext webApplicationContext = (WebApplicationContext) sab.run(args);
//		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		ServletContext servletContext = webApplicationContext.getServletContext();
		log.info("Starting ZKUI!");
		Properties globalProps = new Properties();
		File configFile = new File("conf/zkui.cfg");
		if (configFile.exists()) {
			globalProps.load(new FileInputStream(configFile));
		} else {
			System.out.println("Please create zkui.cfg properties file and then execute the program!");
			System.exit(1);
		}

		globalProps.setProperty("uptime", new Date().toString());
		new Dao(globalProps).checkNCreate();
		servletContext.setAttribute("globalProps", globalProps);
		log.info("Starting ZK!");
		ZookeeperMain zookeeperMain = new ZookeeperMain("conf/zk.cfg");
		zookeeperMain.start();
	}

	/**
	 1.精确匹配：即exact.do，明确的路径。
	 2.通配符匹配：即”/*”,
	 3.扩展名匹配：.jsp, .do之类的
	 4.默认的servlet，也即是“/”. defaultwapper。**
     */
/*
	@Bean
	public ServletRegistrationBean defaultServlet() {
		ServletRegistrationBean defaultServlet = new ServletRegistrationBean(new DefaultServlet(),
				"/js/home.js", "/js*//*", "/css*//*", "/images*//*", "/font*//*");
		defaultServlet.setLoadOnStartup(1);
		return defaultServlet;
	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/js*//**", "/css*//**", "/images*//**", "/font*//**").addResourceLocations("classpath:/static*//**");
		super.addResourceHandlers(registry);
	}
	*/
}
