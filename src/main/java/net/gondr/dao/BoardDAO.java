package net.gondr.dao;

import java.util.List;

import net.gondr.domain.BoardVO;
import net.gondr.domain.CommentVO;
import net.gondr.domain.Criteria;

public interface BoardDAO {
	public void write(BoardVO data);
	//글보기
	public BoardVO view(Integer id);
	//글 리스트 보기(몇번부터 몇개를 볼 것인지를 보내준다.)
	public List<BoardVO> list(Criteria cri);
	//글 삭제
	public void delete(Integer id);
	//글 수정
	public void update(BoardVO data);
	//현재 글의 갯수를 가져오는 것
	public Integer getCnt(Criteria cri);
	
	
	// 댓글 리스트 보기
	public List<CommentVO> comments(Criteria cri);
	public void write(CommentVO data);
	public void deleteComment(Integer idx);
	public Integer getComment(Criteria cri);
}
