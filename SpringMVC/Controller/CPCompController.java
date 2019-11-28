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

	// crp_no, 기업번호를 담을 맵 
	private Map<String, String> tolog = new HashMap<>();

	@RequestMapping("/comp_all.do") 
	public ModelAndView compalllist() {
		
		List<CompanyEntity> compinfo = compservice.complist();
		List<RecruitEntity> comprecruit = recservice.recruitall();

		ModelAndView mav = new ModelAndView("view/comp/comp_all");
		mav.addObject("compinfo", compinfo);
		mav.addObject("comprecruit", comprecruit);

		return mav;
	}

	@RequestMapping("/comp_list.do") 
	public ModelAndView comppagelist(@RequestParam("page") String page, @RequestParam("crp_nm_i") String crp_nm_i,
			HttpServletRequest req) {
		List<CompanyEntity> compinfo = null;

		if (crp_nm_i.equals("")) {
			try {
				compinfo = compservice.complist();

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			try {
				compinfo = compservice.compinfo(crp_nm_i);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		int p = (Integer.parseInt(page) - 1) * 10;

		List<CompanyEntity> newcomplist = null;

		if (p > compinfo.size()) {
			ModelAndView mv = new ModelAndView("view/comp/comp_all");
			mv.addObject("compinfo", compservice.complist());
			mv.addObject("cpage", 1);
			return mv;
		}

		if ((compinfo.size() - p) < 10) {
			newcomplist = compinfo.subList(p, compinfo.size());
		} else {
			newcomplist = compinfo.subList(p, p + 9);
		}
		
		ModelAndView mav = new ModelAndView("view/comp/comp_all");
		int returnpage = Integer.parseInt(page);
		req.removeAttribute("compinfo");
		mav.addObject("compinfo", newcomplist);
		mav.addObject("cpage", returnpage);
		
		return mav;
	}

	@RequestMapping("/comp_pre.do")
	public ModelAndView newspre(@RequestParam("page") String prepage, @RequestParam("crp_nm_i") String crp_nm_i,
			HttpServletRequest req) {
		ModelAndView mav = new ModelAndView("view/comp/comp_all");
		List<CompanyEntity> compinfo = null;

		if (crp_nm_i.equals("")) {
			try {
				compinfo = compservice.complist();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				compinfo = compservice.compinfo(crp_nm_i);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		int p = (Integer.parseInt(prepage) - 1) * 10;

		List<CompanyEntity> newcomplist = null;

		if (p > compinfo.size()) {
			ModelAndView mv = new ModelAndView("view/comp/comp_all");
			mv.addObject("compinfo", compservice.complist());
			mv.addObject("cpage", 1);
			return mv;

		}

		if ((compinfo.size() - p) < 10) {
			newcomplist = compinfo.subList(p, compinfo.size());
		} else {
			newcomplist = compinfo.subList(p, p + 9);
		}
		
		int returnpage = Integer.parseInt(prepage) - 1;
		mav.addObject("compinfo", newcomplist);
		mav.addObject("cpage", returnpage);
		return mav;
	}

	@RequestMapping("/comp_search.do") 
	public ModelAndView complist(@RequestParam("crp_nm_i") String crp_nm_i) {
		List<CompanyEntity> compinfo = compservice.compsearchlist(crp_nm_i);

		return new ModelAndView("view/comp/comp_all", "compinfo", compinfo);
	}

	@RequestMapping("/comp_info.do") 
	public ModelAndView compinfo(@RequestParam("crp_nm_i") String crp_nm_i) {

		ModelAndView compinfoo = new ModelAndView("view/comp/comp_detail");
		List<CompanyEntity> compinfo = compservice.compinfo(crp_nm_i);
		
		// logging part
		// HttpServletRequest, 조회한 기업번호를 tolog에 담기
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		tolog.put("crp_no", compinfo.get(0).getCrp_no());
		
		// 기업 번호 로깅 객체 생성 후 로그 내용 매핑 
		CPLogger complogger = new CPLogger("COMP");
		complogger.mapLogVal(req, tolog);

		List<RecruitEntity> comprecruit = compservice.comprecruit(crp_nm_i);
		compinfoo.addObject("compinfo", compinfo);
		compinfoo.addObject("comprecruit", comprecruit);
		return compinfoo;
	}

	@ResponseBody
	@RequestMapping("/comp_find.do") // 1.comp_detail.jsp에 기업의 title, adr, ceo_nm, phn_no, hm_url 등을 뿌림
	public String compfind(@RequestParam("comp_name") String crp_nm) {
		List<CompanyEntity> compinfo = compservice.compinfo(crp_nm);
		return compinfo.get(0).getCrp_no();
	}

	// @ResponseBody
	@RequestMapping("/comp_recruit.do") // ajax 2.기업의 관련 채용공고를 뿌려준다. crp_nm = coClcdNm
	public ModelAndView comprecruit(@RequestParam("crp_nm_i") String crp_nm) {
		List<RecruitEntity> comprecruit = compservice.comprecruit(crp_nm);
		return new ModelAndView("view/comp/comp_detail", "comprecruit", comprecruit);
	}

}
