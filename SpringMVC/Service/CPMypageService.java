package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comp.CompanyEntity;
import com.comp.recruit.RecruitEntity;
import com.dao.CPMypageDAO;
import com.news.NewsEntity;
import com.user.*;

@Service("CPMypageService")
public class CPMypageService {

	@Autowired
	private CPMypageDAO mdao;

	//관심 목록 찾아오기
	public List<CPSbookEntity> mysbooklike(String id) {
		CPSbookEntity sb = new CPSbookEntity();
		sb.setId(id);
		return mdao.mysbooklike(sb);
	}
	
	public List<CPSbookEntity> myownsblist(String id) {
		CPSbookEntity sb = new CPSbookEntity();
		sb.setId(id);
		return mdao.myownsblist(sb);
	}

	public List<NewsEntity> mynewslist(String id) {
		return mdao.mynewslist(id);
	}

	public List<CompanyEntity> mycomplist(String id) {
		// TODO Auto-generated method stub
		return mdao.mycomplist(id);
	}

	public List<RecruitEntity> myrecruitlist(String id) {
		return mdao.myrecruitlist(id);
	}

}
