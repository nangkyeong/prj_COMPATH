package com.aop;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.user.UserEntity;

import flume.FlumeRpcClient;


@Aspect
@Component
public class LogAdvice {
	
	private static final Logger newslogger = LogManager.getLogger("newslogger");
	private static final Logger complogger = LogManager.getLogger("complogger");
	private static Map<String, String> logmsg = new HashMap<String,String>();
	HttpServletRequest req = null;
	
	Map<String, String> logheader = new HashMap<String, String>();
	String keytolog, valtolog, userid = null; 
	
	@SuppressWarnings("rawtypes")
	@Around("execution(* com.controller.CPNewsController.gonewsdetail(..))")
	public ModelAndView NewsLogger(ProceedingJoinPoint pjp) {
		
		req = (HttpServletRequest) pjp.getArgs()[1];
		
		if (req.getSession().getAttribute("userinfo") == null) {
			this.userid = "nonuser";
		} else {
			this.userid = ((UserEntity) req.getSession().getAttribute("userinfo")).getId();
			// getUserInfo(this.userid);
		}
		
		keytolog="news_number";
		valtolog=(String)pjp.getArgs()[0];
		newslogger.info(keytolog+",{},id,{}", valtolog, this.userid);
		
//		goFlume();
		
		ModelAndView result = null;
		
		try {
			result = (ModelAndView) pjp.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Around("execution(* com.controller.CPCompController.compinfo(..))")
	public ModelAndView CompLogger(ProceedingJoinPoint pjp) {
		
		req = (HttpServletRequest) pjp.getArgs()[1];

		if (req.getSession().getAttribute("userinfo") == null) {
			this.userid = "nonuser";
		} else {
			this.userid = ((UserEntity) req.getSession().getAttribute("userinfo")).getId();
			// getUserInfo(this.userid);
		}
		
		keytolog="crp_no";
		valtolog=(String)pjp.getArgs()[0];
		newslogger.info(keytolog+",{},id,{}", valtolog, this.userid);
		
//		goFlume();
		
		ModelAndView result = null;
		
		try {
			result = (ModelAndView) pjp.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public void LogSelector(String logselector) { // complogger, newslogger
		this.logheader.put("Loggers", logselector);
	}
	
	public void goFlume() {
		FlumeRpcClient client = new FlumeRpcClient();
		client.init("192.168.56.102", 41414);
		String logmsg = client.getLogMessage(keytolog, valtolog, userid);
		client.sendEventToFlume(logmsg, logheader);
		client.cleanUp();
	}

}
