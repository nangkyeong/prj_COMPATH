package com.controller;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.user.UserEntity;

//HttpServletRequest, logger 이름을 넘겨받고 로그 내용을 파일로 출력 
public class CPLogger {
	
	private Logger logger = null;
	private String ip, sessionId, userid;

	public CPLogger(String loggername) {//complogger, newslogger
		logger = LogManager.getLogger(loggername);
	}

	public void GetLogging(HttpServletRequest req, Map<String,String> tolog) { 
		
		// 해당 요청의 ip 
		ip = req.getHeader("X-FORWARDED-FOR");
		if (ip == null)
			ip = req.getRemoteAddr();
		
		// 세션 id
		sessionId = req.getRequestedSessionId();
		
		// 로그인하지 않은 경우 "nonuser"로 처리 
		if (req.getSession().getAttribute("userinfo") == null)
			userid = req.getRequestedSessionId();
		else
			userid = ((UserEntity) req.getSession().getAttribute("userinfo")).getId();
		
		// 로깅할 내용 추출해내고 로그 메세지 생성 
		Iterator<String> itr = tolog.keySet().iterator();
		String tolog_key = itr.next();
		logger.info(" \"ip\": \"{}\", \"sessionId\": \"{}\", \"userid\": \"{}\", \""+tolog_key+"\" : \"{}\"", ip,
				sessionId, userid, tolog.get(tolog_key));
	}

}
