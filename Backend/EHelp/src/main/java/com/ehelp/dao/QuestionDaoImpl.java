package com.ehelp.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ehelp.entity.Answer;
import com.ehelp.entity.Contact;
import com.ehelp.entity.Question;
import com.ehelp.entity.QuestionResult;
import com.ehelp.util.DBSessionUtil;

@Repository
public class QuestionDaoImpl implements QuestionDao {
	
	//根据id获取问题
	public Question getQues(int id) {
		Session session = DBSessionUtil.getSession();
		Question q = (Question) session.get(Question.class, id);
		DBSessionUtil.closeSession(session);
		return q;
	}

	// 获取问题列表
	public List<QuestionResult> getAllQuestions() {
		List<QuestionResult> results = new ArrayList<QuestionResult>();
		Session session = DBSessionUtil.getSession();
		// 查询语句
		Query query = session.createQuery("select new com.ehelp.entity.QuestionResult(q.id, q.title, q.description, q.date, u.username, u.avatar) "
				+ "from Question q, User u where q.asker_id=u.id");
		query.setMaxResults(20);
		// 查询结果
		results = query.list();
		
		//排序
		Collections.sort(results, new Comparator<QuestionResult>() {

			public int compare(QuestionResult q1, QuestionResult q2) {
				Date d1 = q1.getAsk_date();
				Date d2 = q2.getAsk_date();
				if (d1.after(d2)) return -1;
				else return 1;
			}
			
		});		
		
		// 事务提交并关闭
		DBSessionUtil.closeSession(session);
		return results;
	}

	// 根据问题id获取问题
	public List<QuestionResult> getQuestion(int id) {
		List<QuestionResult> results = new ArrayList<QuestionResult>();
		Session session = DBSessionUtil.getSession();
		// 查询语句
		Query query = session.createQuery("select new com.ehelp.entity.QuestionResult(u.username, u.avatar, a.description, a.date) "
				+ "from Question q, Answer a, User u where q.id=a.question_id and a.answerer_id=u.id and q.id=:id");
		query.setParameter("id", id);
		// 查询结果
		results = query.list();
		// 事务提交并关闭
		DBSessionUtil.closeSession(session);
		return results;
	}

	//添加问题
	public boolean ask(Question q) {
		Session session = DBSessionUtil.getSession();
		session.save(q);
		DBSessionUtil.closeSession(session);
		return true;
	}

	//回答
	public boolean answer(Answer a) {
		Session session = DBSessionUtil.getSession();
		session.save(a);
		DBSessionUtil.closeSession(session);
		return true;
	}

	public static void main(String[] args) {
		QuestionDaoImpl questionDaoImpl = new QuestionDaoImpl();
//		List<QuestionResult> results = new ArrayList<QuestionResult>();
//		results = questionDaoImpl.getAllQuestions();
////		results = questionDaoImpl.getQuestion(1);
//		for (QuestionResult q : results) {
//			System.out.println(q.toString());
//		}
		System.out.println(questionDaoImpl.getQues(25));
	}
	
}



