package com.datalex.tdp.ui.demo.wrapper;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datalex.tdp.analyzer.api.Factory;
import com.datalex.tdp.analyzer.collect.TdpServiceInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class DemoOne
 */
@WebServlet(urlPatterns = "/api/bp", loadOnStartup = 0, initParams = {
		@WebInitParam(name = "appProp", value = "app.properties"), })
public class DemoOne extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(AbstractController.class);

	private static final long serialVersionUID = 1L;

	private static List<TdpServiceInfo> list = null;

	private List<TdpServiceInfo> getInfo() {
		return list;
	}

	@Override
	public void init() throws ServletException {
		super.init();
		String initProp = getInitParameter("appProp");
		if (Factory.getInstance().applyConfig(initProp)) {
			list = Factory.getInstance().getInfo();
		};
	}

	@Override
	protected void performTask(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			String json = getJson(getInfo());
			if (json != null) {
				logger.debug("successful");
			}
			response.setContentType("application/x-json");
			response.getWriter().write(json);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private ObjectMapper mapper = new ObjectMapper();

	public String getJson(Object obj) {
		String jsonStr = null;
		try {
			jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
		}
		return jsonStr;
	}

}
