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

	@RequestMapping("/comp_all.do") // search 버튼의 crp_nm 파람을 받아서 해당 기업의 객체들을 불러올거임.
	public ModelAndView compalllist() {
		List<CompanyEntity> compinfo = compservice.complist();
		List<RecruitEntity> comprecruit = recservice.recruitall();
		// "view/comp/comp_all", "compinfo", compinfo

		ModelAndView mav = new ModelAndView("view/comp/comp_all");
		mav.addObject("compinfo", compinfo);
		mav.addObject("comprecruit", comprecruit);

		return mav;
	}

	//
	@RequestMapping("/comp_list.do") // search 버튼의 crp_nm 파람을 받아서 해당 기업의 객체들을 불러올거임.
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
			// System.out.println(6);
			mv.addObject("compinfo", compservice.complist());
			mv.addObject("cpage", 1);
			// System.out.println(7);
			return mv;

		}

		if ((compinfo.size() - p) < 10) {
			// System.out.println(8);
			newcomplist = compinfo.subList(p, compinfo.size());
			// System.out.println(9);
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
			// System.out.println(6);
			mv.addObject("compinfo", compservice.complist());
			mv.addObject("cpage", 1);
			// System.out.println(7);
			return mv;

		}

		if ((compinfo.size() - p) < 10) {
			// System.out.println(8);
			newcomplist = compinfo.subList(p, compinfo.size());
			// System.out.println(9);
		} else {

			newcomplist = compinfo.subList(p, p + 9);

		}
		int returnpage = Integer.parseInt(prepage) - 1;
		mav.addObject("compinfo", newcomplist);
		mav.addObject("cpage", returnpage);
		return mav;
	}

	@RequestMapping("/comp_search.do") // search 버튼의 crp_nm 파람을 받아서 해당 기업의 객체들을 불러올거임.
	public ModelAndView complist(@RequestParam("crp_nm_i") String crp_nm_i) {
		List<CompanyEntity> compinfo = compservice.compsearchlist(crp_nm_i);

		return new ModelAndView("view/comp/comp_all", "compinfo", compinfo);
	}

	@RequestMapping("/comp_info.do") // 1.comp_detail.jsp에 기업의 title, adr, ceo_nm, phn_no, hm_url 등을 뿌림
	public ModelAndView compinfo(@RequestParam("crp_nm_i") String crp_nm_i) {

		ModelAndView compinfoo = new ModelAndView("view/comp/comp_detail");
		List<CompanyEntity> compinfo = compservice.compinfo(crp_nm_i);
		// HttpServletRequest, crp_nm_i를 map에 담기
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		tolog.put("crp_no", compinfo.get(0).getCrp_no());
		// logger 생성하고 파일에 기록
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
	/*
	 * //재무제표
	 * 
	 * @RequestMapping("/go_finstate.do") public ModelAndView
	 * finstate(@RequestParam("crp_nm_i") String crp_nm_i ) { List<CompanyEntity>
	 * finstate = compservice.finstate(crp_nm_i);
	 * 
	 * ModelAndView mav = new ModelAndView("view/comp/comp_detail");
	 * mav.addObject("finstate", finstate);
	 * 
	 * return mav; }
	 */

}
