package com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.news.NewsEntity;
import com.service.CPMypageService;
import com.service.CPSbookService;
import com.user.CPSbookEntity;

@Controller
public class CPSbookController {

	@Autowired
	private CPSbookService sbservice;
	@Autowired
	private CPMypageService mservice;

	private Map<String, String> tolog = new HashMap<>();

	//전체리스트 
	@RequestMapping("/scrapbook_all.do")
	public ModelAndView sbook() {
		//첫 페이지
		List<CPSbookEntity> all = sbservice.getAllBull();
		
		List<CPSbookEntity> allcut = null;
		if(all.size()<10) {
			allcut = all;
		} else {
			allcut = all.subList(0, 10);
		}
		
		//System.out.println(all);
		ModelAndView mav = new ModelAndView("view/user/scrapbook/scrapbook_all", "all", allcut);
		mav.addObject("page", 1);
		int endPage = (sbservice.getAllBull().size()/10)+1; //마지막페이지
		mav.addObject("endPage",endPage);
		return mav;
	}
	
	@RequestMapping(value="/scrapbook_all.do", params= {"page"})
	public ModelAndView sbook(@RequestParam int page) {
		//해당 페이지에 보낼 10개 글 (2-1)*10 ~ (2-1)*10+9
		int startnum = (page-1)*10;
		List<CPSbookEntity> all = sbservice.getAllBull();
		List<CPSbookEntity> allcut = null;
		//System.out.println("all size bf= "+all.size());
		try{
			allcut = all.subList(startnum, startnum+10);
		} catch(IndexOutOfBoundsException e) {
			allcut = all.subList(startnum, all.size());
		}
		ModelAndView mav = new ModelAndView("view/user/scrapbook/scrapbook_all", "all", allcut);
		mav.addObject("page", page);
		int endPage = (all.size()/10)+1; //마지막페이지
		mav.addObject("endPage",endPage);
		return mav;
	}

	//해당 id가 작성한 스크랩북만 확인
	@RequestMapping("/sbook_selectbyid.do")
	public ModelAndView sbook_selectbyid(@RequestParam("id") String id) {
		List<CPSbookEntity> selected = sbservice.selectbyid(id);
		return new ModelAndView("view/user/scrapbook/scrapbook_all", "all", selected);
	}
	
	//새로운 작성할 스크랩북을 임시 스크랩북이라는 타이틀로 만들고, 에디터 페이지로 넘겨준다.
	@RequestMapping("/sbook_create.do")
	public String sbook_create(@RequestParam("id") String id) {
		CPSbookEntity newsb = sbservice.createsb(id);
		return "redirect:sbook_toedit.do?scrapbook_num="+newsb.getScrapbook_num()+"&id="+id;
	}
	
	//내가 수정할 스크랩북을 찾아서 에디터페이지에 넘겨준다 + 스크랩 뉴스 리스트도 보내준다
	@RequestMapping("/sbook_toedit.do")
	public ModelAndView findbysnum(@RequestParam("scrapbook_num") int scrapbook_num, @RequestParam("id") String id) {
		Map <String, Object> sbookmap = new HashMap<>();
		List<NewsEntity> mynewslist = mservice.mynewslist(id);
		CPSbookEntity found = sbservice.findbysnum(scrapbook_num);
		sbookmap.put("mynewslist", mynewslist);
		sbookmap.put("found", found);
		return new ModelAndView("view/user/scrapbook/scrapbook_editor", sbookmap);
	}
	
	//내가 작성한 스크랩북 삭제
	@RequestMapping("/sbook_delete.do")
	public ModelAndView sbook_delete(@RequestParam("scrapbook_num") int scrapbook_num, @RequestParam("id") String id) {
		int res = sbservice.delete(scrapbook_num);
		if (res > 0) {
			return new ModelAndView("view/user/mypage", "myownsblist", sbservice.selectbyid(id));
		} else {
			return new ModelAndView("error", "fail", "흠냐뤼..");
		}
	}

	//스크랩북 update
	@RequestMapping("/sbook_update.do")
	public ModelAndView sbook_update(HttpSession session, @ModelAttribute("toupdate") CPSbookEntity be) {
		int res = sbservice.update(be);
		
		if (res > 0) {
			return new ModelAndView("view/user/mypage");
		} else {
			return new ModelAndView("error", "fail", "흠냐뤼..");
		}
	}
	
	//스크랩북 자세히 보기
	@RequestMapping("/sbook_content.do")
	public ModelAndView sbook_content(@RequestParam("scrapbook_num") int scrapbook_num) {
		CPSbookEntity found = sbservice.findbysnum(scrapbook_num);
		HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		tolog.put("scrapbook_num", Integer.toString(scrapbook_num));
		CPLogger sbooklogger = new CPLogger("ETC");
		sbooklogger.mapLogVal(req, tolog);
		return new ModelAndView("view/user/scrapbook/scrapbook_content", "found", found);
	}
	
	//스크랩북 공유로 set sbook_check =1 한 후 전체 스크랩게시판으로 이동
	@RequestMapping("/sbook_share.do")
	public ModelAndView sbook_share(@RequestParam("scrapbook_num") int scrapbook_num) {
		System.out.println("scrapbook_num"+scrapbook_num);
		int res = sbservice.sbook_share(scrapbook_num);
		if(res>0) {
		return sbook();
		} else {
			return new ModelAndView("error", "fail", "흠냐뤼..");
		}
		
	}
	
}
