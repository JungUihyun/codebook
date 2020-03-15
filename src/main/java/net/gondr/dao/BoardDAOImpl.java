package net.gondr.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.gondr.domain.BoardVO;
import net.gondr.domain.Criteria;

@Repository
public class BoardDAOImpl implements BoardDAO{
	@Autowired
	private SqlSession session;
	
	private final String ns = "net.gondr.mappers.BoardMapper";
	
	@Override
	public void write(BoardVO data) {
		session.insert(ns + ".write", data);
	}
	
	@Override
	public BoardVO view(Integer id) {
		return session.selectOne(ns + ".view", id);
	}
	
	@Override
	public List<BoardVO> list(Criteria cri) {
		return session.selectList(ns + ".list", cri);
	}
	
	@Override
	public void delete(Integer id) {
		session.delete(ns + ".delete", id);
	}
	
	@Override
	public void update(BoardVO data) {
		session.update(ns + ".update", data);
	}
	
	@Override
	public Integer getCnt(Criteria cri) {
		return session.selectOne(ns + ".cnt", cri);
	}
}
