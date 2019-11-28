package com.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dao.CPUserDAO;
import com.user.UserEntity;

import flume.FlumeRpcClient;

//HttpServletRequest, logger 이름을 넘겨받고 로그 파일 기록
public class CPLogger {

	//private String gender, finalEdu, careerDur; //회원 상세 정보 
	private String time, ip, sessionId, userid, keytolog, valtolog; //회원 식별 내용과 로그 내용 
	
	//flume에서 로그 분류를 위한 로그 메세지 헤더 설정  
	Map<String, String> header = new HashMap<String, String>();

	public CPLogger(String logselector) {// complogger, newslogger 
		this.header.put("Loggers", logselector);
	}

	public void mapLogVal(HttpServletRequest req, Map<String, String> tolog) { 
		
		// 발생 시각 기록 
		Date date = new Date();
		SimpleDateFormat now = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		this.time = now.format(date);
		
		// 해당 요청의 ip 
		this.ip = req.getHeader("X-FORWARDED-FOR");
		if (this.ip == null) 
			this.ip = req.getRemoteAddr();
		
		// 세션 id
		this.sessionId = req.getRequestedSessionId();
		
		// 로그인하지 않은 경우 "nonuser"로 처리  
		if (req.getSession().getAttribute("userinfo") == null) {
			this.userid = "nonuser";
		} else {
			this.userid = ((UserEntity) req.getSession().getAttribute("userinfo")).getId();
			//getUserInfo(this.userid);
		}
		
		// 로깅할 내용 추출
		Iterator<String> itr = tolog.keySet().iterator();
		this.keytolog = itr.next();
		this.valtolog = tolog.get(keytolog);
		goFlume();
	}

	public void getUserInfo(String userid) {
		// 회원의 다른 상세 정보도 함께 로깅할 경우 사용  
		UserEntity userinfo = new UserEntity();
		userinfo.setId(userid);
		userinfo = new CPUserDAO().cp_usercheck(userinfo);
		this.gender = userinfo.getGender();
		this.finalEdu = userinfo.getFinal_education();
		this.careerDur = userinfo.getCareer_duration();
	}
	
	public void goFlume() {
		// flume 서버에 로그 메세지 전송 
		FlumeRpcClient client = new FlumeRpcClient();
		client.init("192.168.56.102", 41414); 
		String logmsg = client.getLogMessage(time, ip, sessionId, userid, keytolog, valtolog);
		client.sendEventToFlume(logmsg, header);
		client.cleanUp();
	}
}
