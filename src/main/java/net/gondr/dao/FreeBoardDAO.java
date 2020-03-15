package net.gondr.dao;

import java.util.List;

import net.gondr.domain.Criteria;
import net.gondr.domain.FreeBoardVO;

public interface FreeBoardDAO {
	public void write(FreeBoardVO data);
	//글보기
	public FreeBoardVO view(Integer id);
	//글 리스트 보기(몇번부터 몇개를 볼 것인지를 보내준다.)
	public List<FreeBoardVO> list(Criteria cri);
	//글 삭제
	public void delete(Integer id);
	//글 수정
	public void update(FreeBoardVO data);
	//현재 글의 갯수를 가져오는 것
	public Integer getCnt(Criteria cri);
}
