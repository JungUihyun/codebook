package net.gondr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.gondr.dao.BoardDAO;
import net.gondr.domain.BoardVO;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardDAO dao;
	
	@Override
	public void writeArticle(BoardVO board) {
		dao.write(board);
	}
	
}
