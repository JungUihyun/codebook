package net.gondr.service;

import java.util.List;

import net.gondr.domain.Criteria;
import net.gondr.domain.FreeBoardVO;

public interface FreeBoardService {
	// 글쓰기
	public void writeArticle(FreeBoardVO board);

	// 글보기
	public FreeBoardVO viewArticle(Integer id);

	// 글 리스트 보기
	public List<FreeBoardVO> getArticleList(Criteria cri);

	// 글 수정하기
	public void updateArticle(FreeBoardVO board);

	// 글 삭제하기
	public void deleteArticle(Integer id);

	// 글 갯수 가져오기
	public Integer countArticle(Criteria cri);
}