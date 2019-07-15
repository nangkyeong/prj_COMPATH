package com.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.news.news;
import com.service.CPNewsService;

@Controller
public class CPNewsController {

	@Autowired
	private CPNewsService cpns;

	//log용 String 배열
	private Map<String, String> tolog = new HashMap<>();

	@RequestMapping("/news_body.do")
	public ModelAndView gonewsdetail(@RequestParam("news_number") String number) {
		ModelAndView mav = new ModelAndView("view/news/news_body");
		news newss = cpns.getnewsbynum(number);
		mav.addObject("newsdetail", newss);
		String url = newss.getUrl();
		try {
			Document doc = Jsoup.connect(url).get();
			mav.addObject("articleBodyContents", doc.select("#articleBodyContents"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// news log 남기기 
		HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		tolog.put("news_number", number);
		CPLogger newslogger = new CPLogger("NEWS");
		newslogger.mapLogVal(req, tolog);

		return mav;
	}
}