package com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.comp.CompanyEntity;
import com.comp.recruit.RecruitEntity;
import com.news.NewsEntity;
import com.service.CPMypageService;
import com.user.CPSbookEntity;

@Controller("CPMypageController")
public class CPMypageController {

   @Autowired
   private CPMypageService mservice;

   //마이페이지 메인으로 간다
   @RequestMapping("/mypage.do")
   public ModelAndView main(@RequestParam("id") String id, HttpSession session) {
      session.setAttribute("id", id);
      Map<String, Object> mypage = new HashMap<>();
      mypage.put("mynewslist", mynewslist(id));
      mypage.put("mycomplist", mycomplist(id));
      mypage.put("myrecruitlist", myrecruitlist(id));
      mypage.put("mysbooklike", mysbooklike(id));
      mypage.put("myownsblist", myownsblist(id));
      return new ModelAndView("view/user/mypage", mypage);
   }

   // 마이페이지 메인에 표시할 '내가 스크랩한 뉴스 리스트'를 불러온다
   @ResponseBody
   @RequestMapping("/mynews_getall.do") 
   public List<NewsEntity> mynewslist(@RequestParam("id") String id) {
      List<NewsEntity> mynewslist = mservice.mynewslist(id);
      return mynewslist;
   }
   
   // 마이페이지 메인에 표시할 '내가 스크랩한 채용공고 리스트'를 불러온다
   
   
   @ResponseBody
   @RequestMapping("/mycomp_getall.do") 
   public List<CompanyEntity> mycomplist(@RequestParam("id") String id) {
      List<CompanyEntity> mycomplist = mservice.mycomplist(id);
      return mycomplist;
   }

   // 마이페이지 메인에 표시할 '내가 스크랩한 채용공고 리스트'를 불러온다
   @ResponseBody
   @RequestMapping("/myrecruit_getall.do") 
   public List<RecruitEntity> myrecruitlist(@RequestParam("id") String id) {
      List<RecruitEntity> myrecruitlist = mservice.myrecruitlist(id);
      return myrecruitlist;
   }
   //마이페이지 메인에 표시할 내 관심 스크랩북 리스트를 불러온다.
   @ResponseBody
   @RequestMapping("/mysbooklike_getall.do")
   public List<CPSbookEntity> mysbooklike(@RequestParam("id") String id) {
      List<CPSbookEntity> mysbooklike = mservice.mysbooklike(id.trim());
      return mysbooklike;
   }
   //마이페이지 메인에 표시할 내가 만든 스크랩북 리스트를 불러온다.
   @ResponseBody
   @RequestMapping("/myownsblist_getall.do")
   public List<CPSbookEntity> myownsblist(@RequestParam("id") String id) {
      List<CPSbookEntity> myownsblist = mservice.myownsblist(id.trim());
      return myownsblist;
   }
}