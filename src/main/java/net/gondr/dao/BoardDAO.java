package net.gondr.dao;

import java.util.List;

import net.gondr.domain.BoardVO;
import net.gondr.domain.Criteria;

public interface BoardDAO {
	// 글을 쓰는 매서드
	public void write(BoardVO data);
	// 글보기 메서드
	public BoardVO view(Integer id);
	// 글리스트 보기
	public List<BoardVO> list(Criteria cri);
	// 글삭제
	public void delete(Integer id);
	// 글 수정
	public void update(BoardVO data);
	// 현재 글의 개수
	public Integer getCnt(Criteria cri);
}