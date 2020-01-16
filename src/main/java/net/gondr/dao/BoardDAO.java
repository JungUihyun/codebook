package net.gondr.dao;

import net.gondr.domain.BoardVO;

public interface BoardDAO {
	// 글을 쓰는 매서드
	public void write(BoardVO data);
}
