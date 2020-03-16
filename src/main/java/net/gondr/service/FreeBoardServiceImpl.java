package net.gondr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.gondr.dao.FreeBoardDAO;
import net.gondr.domain.CommentVO;
import net.gondr.domain.Criteria;
import net.gondr.domain.FreeBoardVO;

@Service
public class FreeBoardServiceImpl implements FreeBoardService {
	@Autowired
	private FreeBoardDAO dao;

	@Override
	public void writeArticle(FreeBoardVO board) {
		dao.write(board);
	}

	public FreeBoardVO viewArticle(Integer id) {
		return dao.view(id);
	}
	
	public List<FreeBoardVO> getArticleList(Criteria cri) {
		return dao.list(cri);
	}
	
	public void updateArticle(FreeBoardVO board) {
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
}
