package net.gondr.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.gondr.domain.BoardVO;

@Repository
public class BoardDAOImpl implements BoardDAO {
	@Autowired
	private SqlSession session;
	
	private final String namespace = "net.gondr.mappers.BoardMapper";
	
	@Override
	public void write(BoardVO data) {
		session.insert(namespace + ".write", data);
	}
}
