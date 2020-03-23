package net.gondr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.gondr.dao.BoardDAO;
import net.gondr.domain.BoardVO;
import net.gondr.domain.CommentVO;
import net.gondr.domain.Criteria;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardDAO dao;

	@Override
	public void writeArticle(BoardVO board) {
		dao.write(board);
	}

	public BoardVO viewArticle(Integer id) {
		return dao.view(id);
	}
	
	public List<BoardVO> getArticleList(Criteria cri) {
		return dao.list(cri);
	}
	
	public void updateArticle(BoardVO board) {
		dao.update(board);
	}

	public void deleteArticle(Integer id) {
		dao.delete(id);
	}
 
	public Integer countArticle(Criteria cri) {
		return dao.getCnt(cri);
	}

	
	
	
	public List<CommentVO> getCommentList(Criteria cri) {
		return dao.comments(cri);
	}
	
	public void writeComment(CommentVO comment) {
		dao.write(comment);
	}
	
	public void deleteComment(Integer idx) {
		dao.deleteComment(idx);
	}
	
	public Integer countComment(Criteria cri) {
		return dao.getComment(cri);
	}
	
	
}
