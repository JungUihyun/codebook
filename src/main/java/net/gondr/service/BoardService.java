package net.gondr.service;

import java.util.List;

import net.gondr.domain.BoardVO;
import net.gondr.domain.CommentVO;
import net.gondr.domain.Criteria;

public interface BoardService {
	//글쓰기
	public void writeArticle(BoardVO board);
	//글보기
	public BoardVO viewArticle(Integer id);
	//글 리스트 보기
	public List<BoardVO> getArticleList(Criteria cri);
	//글 수정하기
	public void updateArticle(BoardVO board);
	//글 삭제하기
	public void deleteArticle(Integer id);
	//글 갯수 가져오기
	public Integer countArticle(Criteria cri);
	
	
	
	// 댓글 리스트 보기
	public List<CommentVO> getCommentList(Criteria cri);
	// 댓글 작성
	public void writeComment(CommentVO comment);
	// 댓글 삭제
	public void deleteComment(Integer idx);
	// 댓글 갯수 가져오기
	public Integer countComment(Criteria cri);
	
}