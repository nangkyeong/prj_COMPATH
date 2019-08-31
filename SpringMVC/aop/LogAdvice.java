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

//HttpServletRequest, logger 이름을 넘겨받고 로그 파일 기록

//id,gender,Final_Education,Career_Duration
//	private String gender,finalEdu,careerDur;
//	private String time, ip, sessionId, userid, keytolog, valtolog;
//	Map<String, String> header = new HashMap<String, String>();

@Aspect
@Component
public class LogAdvice {
//	private static final Logger logger = LogManager.getLogger(LogAdvice.class);
	private static final Logger newslogger = LogManager.getLogger("newslogger");
	private static final Logger complogger = LogManager.getLogger("complogger");
//	private static final Marker newsloggermarker = MarkerManager.getMarker("newslogger");
	private static Map<String, String> logmsg = new TreeMap<String,String>();
//	private static ObjectMessage omsg = null;
//	private static MapMessage mapmsg = new MapMessage<, String>(logmsg);
	HttpServletRequest req = null;
	
	Map<String, String> logheader = new HashMap<String, String>();
	String keytolog,valtolog,userid = null; 
	@SuppressWarnings("rawtypes")
	@Around("execution(* com.controller.CPNewsController.gonewsdetail(..))")
	public ModelAndView NewsLogger(ProceedingJoinPoint pjp) {
		
		req = (HttpServletRequest) pjp.getArgs()[1];
		System.out.println(req.getSession().getId());
		if (req.getSession().getAttribute("userinfo") == null) {
			this.userid = "nonuser";
		} else {
			this.userid = ((UserEntity) req.getSession().getAttribute("userinfo")).getId();
			//getUserInfo(this.userid);
		}
		keytolog="news_number";
		valtolog=(String)pjp.getArgs()[0];
		newslogger.info(keytolog+",{},id,{}", valtolog, this.userid);
//		goFlume();
//		logmsg.put("news_number", (String) pjp.getArgs()[0]);
//		newslogger.info(new ObjectMessage(logmsg));
//		logmsg.put("news_number", (String) pjp.getArgs()[0]);
//		newslogger.info(new MapMessage().with("news_number", (String) pjp.getArgs()[0]).asString("JSON")); // this will be sub-document in MongoDB
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
		System.out.println(req.getSession().getId());
		if (req.getSession().getAttribute("userinfo") == null) {
			this.userid = "nonuser";
		} else {
			this.userid = ((UserEntity) req.getSession().getAttribute("userinfo")).getId();
			//getUserInfo(this.userid);
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
	public void LogSelector(String logselector) {// complogger, newslogger, recruitlogger, sbooklogger
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
