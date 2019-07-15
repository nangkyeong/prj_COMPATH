package com.controller;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.user.UserEntity;

//HttpServletRequest, logger 이름을 넘겨받고 로그 파일 기록
public class CPLogger {
	private Logger logger = null;
	private String ip, sessionId, userid;

	public CPLogger(String loggername) {//complogger, newslogger
		logger = LogManager.getLogger(loggername);
	}

	public void GetLogging(HttpServletRequest req, Map<String,String> tolog) { // String 배열 : 추후 더 기록할 요소 존재 가능
		ip = req.getHeader("X-FORWARDED-FOR");
		if (ip == null)
			ip = req.getRemoteAddr();
		sessionId = req.getRequestedSessionId();
		if (req.getSession().getAttribute("userinfo") == null)
			userid = req.getRequestedSessionId();
		else
			userid = ((UserEntity) req.getSession().getAttribute("userinfo")).getId();
		
		//key 값 얻기 
		Iterator<String> itr = tolog.keySet().iterator();
		String tolog_key = itr.next();
		
		logger.info(" \"ip\": \"{}\", \"sessionId\": \"{}\", \"userid\": \"{}\", \""+tolog_key+"\" : \"{}\"", ip,
				sessionId, userid, tolog.get(tolog_key));
	}

}
