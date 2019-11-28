package com.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.user.CPSbookEntity; //List<Product> 占쏙옙 占쏙옙占쏙옙 importing

import mybatis.mybatisutil;


@Repository("CPSbookDAO")
public class CPSbookDAO {

	//전체리스트 
	public List<CPSbookEntity> getAllBull() {
		SqlSession session = mybatisutil.factory.openSession();
		List<CPSbookEntity> all = null;
		try {
			all = session.selectList("sbookMapper.selectall");
		} finally {
			session.close();
		}
		return all;

	}

	//해당 id가 작성한 스크랩북만 확인
	public List<CPSbookEntity> selectbyid(String id) {
		SqlSession session = mybatisutil.factory.openSession();
		List<CPSbookEntity> selected = null;
		try {
			selected = session.selectList("sbookMapper.selectbyid", id);
		} finally {
			session.close();
		}
		return selected;

	}

	//내가 작성한 스크랩북 삭제
	public CPSbookEntity findbysnum(int scrapbook_num) {
		SqlSession session = mybatisutil.factory.openSession();
		CPSbookEntity found = null;
		try {
			found = session.selectOne("sbookMapper.findbysnum", scrapbook_num);
		} finally {
			session.close();
		}
		return found;

	}

	// insert
	public int insert(CPSbookEntity be) {
		SqlSession session = mybatisutil.factory.openSession();
		int res = 0;
		try {
			res = session.insert("sbookMapper.insert", be);
			if (res > 0) {
				session.commit();
				System.out.println("commit");
			}
		} catch (Exception e) {
			session.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return res;
	}

	// 새로운 작성할 스크랩북을 임시 스크랩북이라는 타이틀로 만들고, 에디터 페이지로 넘겨준다.
	public CPSbookEntity createsb(CPSbookEntity nsbe) {
		
		// 임시 스크랩북이라는 타이틀의 sbook 엔터티를 insert한다
		int res = insert(nsbe);
		
		// 임시 객체를 삽입 성공했을 때, 해당 id의 임시 스크랩북이라는 타이틀의 sbook 객체를 받아온다.
		SqlSession session = mybatisutil.factory.openSession();
		CPSbookEntity newsb = new CPSbookEntity();
		
		if (res > 0) {
			try {
				newsb = session.selectOne("sbookMapper.findnewsb",nsbe);
			} catch (Exception e) {
				session.rollback();
			} finally {
				session.close();
			}
		}
		return newsb;
	}

	// delete
	public int delete(CPSbookEntity be) {
		SqlSession session = mybatisutil.factory.openSession();
		int res = 0;
		try {
			res = session.delete("sbookMapper.delete", be);
			if (res > 0) {
				session.commit();
			}
		} catch (Exception e) {
			session.rollback();
		} finally {
			session.close();
		}
		return res;
	}

	public int update(CPSbookEntity be) {
		SqlSession session = mybatisutil.factory.openSession();
		int res = 0;
		try {
			res = session.update("sbookMapper.update", be);
			if (res > 0) {
				session.commit();
			}
		} catch (Exception e) {
			session.rollback();
		} finally {
			session.close();
		}
		return res;
	}

	//공유하기 
	public int sbook_share(CPSbookEntity be) {
		SqlSession session = mybatisutil.factory.openSession();
		int res = 0;
		try {
			res = session.update("sbookMapper.share", be);
			if (res > 0) {
				System.out.println("commit");
				session.commit();
			}
		} catch (Exception e) {
			session.rollback();
		} finally {
			session.close();
		}
		return res;
	}

}
