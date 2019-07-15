package com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.comp.CompanyEntity;
import com.comp.recruit.RecruitEntity;
import com.service.CPCompService;
import com.service.CPRecruitService;

@Controller("CPCompController")
public class CPCompController {

	@Autowired
	private CPCompService compservice;
	@Autowired
	private CPRecruitService recservice;

	// log용 String 배열
	private Map<String, String> tolog = new HashMap<>();


	@RequestMapping("/comp_info.do") // 1.comp_detail.jsp에 기업의 title, adr, ceo_nm, phn_no, hm_url 등을 뿌림
	public ModelAndView compinfo(@RequestParam("crp_nm_i") String crp_nm_i) {

		ModelAndView compinfoo = new ModelAndView("view/comp/comp_detail");
		List<CompanyEntity> compinfo = compservice.compinfo(crp_nm_i);
		// HttpServletRequest, crp_nm_i를 map에 담기
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		// logger 생성하고 파일에 기록
		tolog.put("crp_no", compinfo.get(0).getCrp_no());
		CPLogger complogger = new CPLogger("COMP");
		complogger.mapLogVal(req, tolog);

		List<RecruitEntity> comprecruit = compservice.comprecruit(crp_nm_i);
		compinfoo.addObject("compinfo", compinfo);
		compinfoo.addObject("comprecruit", comprecruit);
		return compinfoo;
	}
