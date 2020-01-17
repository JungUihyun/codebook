package net.gondr.service;

import net.gondr.domain.UserVO;

public interface UserService {
	public UserVO login(String userid, String password);
	
	public void register(UserVO user);
	
	public UserVO getUserInfo(String userid);
	
	// 레벨테이블 채워주기
	public void fillLevelTable(Integer max);
	
	// 회원에게 경험치 추가하기
	public UserVO addExp(String userId, Integer exp);
	
	
}
