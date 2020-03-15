package net.gondr.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.gondr.domain.Criteria;
import net.gondr.domain.FreeBoardVO;

@Repository
public class FreeBoardDAOImpl implements FreeBoardDAO{
	@Autowired
	private SqlSession session;
	
	private final String ns = "net.gondr.mappers.FreeBoardMapper";
	
	@Override
	public void write(FreeBoardVO data) {
		session.insert(ns + ".write", data);
	}
	
	@Override
	public FreeBoardVO view(Integer id) {
		return session.selectOne(ns + ".view", id);
	}
	
	@Override
	public List<FreeBoardVO> list(Criteria cri) {
		return session.selectList(ns + ".list", cri);
	}
	
	@Override
	public void delete(Integer id) {
		session.delete(ns + ".delete", id);
	}
	
	@Override
	public void update(FreeBoardVO data) {
		session.update(ns + ".update", data);
	}
	
	@Override
	public Integer getCnt(Criteria cri) {
		return session.selectOne(ns + ".cnt", cri);
	}
}
