package net.gondr.service;

import net.gondr.domain.BoardVO;

public interface BoardService {
	// 글쓰기
	public void writeArticle(BoardVO board);
}
