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

	//id,gender,Final_Education,Career_Duration
	private String gender,finalEdu,careerDur;
	private String time, ip, sessionId, userid, keytolog, valtolog;
	Map<String, String> header = new HashMap<String, String>();

	public CPLogger(String logselector) {// complogger, newslogger, recruitlogger, sbooklogger
		this.header.put("Loggers", logselector);
	}

	public void mapLogVal(HttpServletRequest req, Map<String, String> tolog) { // String 배열 : 추후 더 기록할 요소 존재 가능
		Date date = new Date();
		SimpleDateFormat now = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		this.time = now.format(date);
		this.ip = req.getHeader("X-FORWARDED-FOR");

		if (this.ip == null) 
			this.ip = req.getRemoteAddr();
		this.sessionId = req.getRequestedSessionId();
		if (req.getSession().getAttribute("userinfo") == null) {
			this.userid = "nonuser";
		} else {
			this.userid = ((UserEntity) req.getSession().getAttribute("userinfo")).getId();
			//getUserInfo(this.userid);
		}
		// log key값
		Iterator<String> itr = tolog.keySet().iterator();
		this.keytolog = itr.next();
		this.valtolog = tolog.get(keytolog);
		goFlume();
	}

	public void getUserInfo(String userid) {
		UserEntity userinfo = new UserEntity();
		userinfo.setId(userid);
		userinfo = new CPUserDAO().cp_usercheck(userinfo);
		this.gender = userinfo.getGender();
		this.finalEdu = userinfo.getFinal_education();
		this.careerDur = userinfo.getCareer_duration();
	}
	
	public void goFlume() {
		FlumeRpcClient client = new FlumeRpcClient();
		client.init("192.168.56.102", 41414);
		String logmsg = client.getLogMessage(time, ip, sessionId, userid, keytolog, valtolog);
		client.sendEventToFlume(logmsg, header);
		client.cleanUp();
	}
}
