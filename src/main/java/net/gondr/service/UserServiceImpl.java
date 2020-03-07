package net.gondr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.gondr.dao.UserDAO;
import net.gondr.domain.UserVO;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO dao;

	@Override
	public UserVO login(String userid, String password) {
		return dao.loginUser(userid, password);
	}

	@Override
	public void register(UserVO user) {
		dao.insertUser(user);
	}

	@Override
	public UserVO getUserInfo(String userid) {
		return dao.getUser(userid);
	}

	@Override
	public void fillLevelTable(Integer max) {
		dao.deleteLevelTable();	// 레벨데이터 삭제하고
		
		for(int i = 1; i <= max; i++) {
			Integer exp = (int)Math.floor(Math.pow( ((double)i - 1) * 50 / 49, 2.5) * 10);
			dao.insertLevelData(i,  exp);
		}
	}

	@Override
	public UserVO addExp(String userId, Integer exp) {
		UserVO user = dao.getUser(userId);
		user.setExp(user.getExp() + exp);
		Integer requireExp = dao.getRequireExp(user.getLevel() + 1);
		
		if(user.getExp() >= requireExp) {
			user.setExp(user.getExp() - requireExp);
			user.setLevel(user.getLevel() + 1);
			System.out.println("레벨업");
		}
		
		// 경험치 증가 처리후 DB에 저장
		dao.setLevelAndExp(user);
		
		return user;
	}	
}
