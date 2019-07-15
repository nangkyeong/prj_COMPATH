package com.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.CPSbookDAO;
import com.user.CPSbookEntity;

@Service("CPSbookService")
public class CPSbookService {

	@Autowired
	private CPSbookDAO sbDao;

	// 전체리스트
	public List<CPSbookEntity> getAllBull() {

		return sbDao.getAllBull();
	}

	// 해당 id가 작성한 스크랩북만 확인
	public List<CPSbookEntity> selectbyid(String id) {
		return sbDao.selectbyid(id);
	}

	// 내가 작성한 스크랩북 삭제
	public CPSbookEntity findbysnum(int scrapbook_num) {
		return sbDao.findbysnum(scrapbook_num);
	}

	// 새로운 작성할 스크랩북을 임시 스크랩북이라는 타이틀로 만들고, 에디터 페이지로 넘겨준다.
	public CPSbookEntity createsb(String id) {
		CPSbookEntity nsbe = new CPSbookEntity();
		// 임시 타이틀에 넣을 해당 날짜
		Date date = new Date();
		SimpleDateFormat now = new SimpleDateFormat("yyyy년 MMM dd일 hh시 mm분 ss초");
		nsbe.setTitle(now.format(date) + "에 작성을 시작한 스크랩북");
		nsbe.setId(id);
		nsbe.setWrite_form("0");
		nsbe.setContents("정리하고 싶은 내용을 기록해두세요 :)");
		nsbe.setInput_URL1("기사 1");
		nsbe.setInput_URL2("기사 2");
		nsbe.setInput_URL3("기사 3");
		return sbDao.createsb(nsbe);
	}

	public int insert(CPSbookEntity be) {
		return sbDao.insert(be);
	}

	public int delete(int scrapbook_num) {
		CPSbookEntity be = new CPSbookEntity();
		be.setScrapbook_num(scrapbook_num);
		return sbDao.delete(be);
	}

	public int update(CPSbookEntity be) {
		return sbDao.update(be);
	}

	// 스크랩북 공유로 set sbook_check =1 한 후 전체 스크랩게시판으로 이동
	public int sbook_share(int scrapbook_num) {
		CPSbookEntity be = new CPSbookEntity();
		be.setScrapbook_num(scrapbook_num);
		System.out.println("service "+be.getScrapbook_num());
		return sbDao.sbook_share(be);
	}

}
