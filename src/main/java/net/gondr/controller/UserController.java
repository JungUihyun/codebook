package net.gondr.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.gondr.domain.LoginDTO;
import net.gondr.domain.RegisterDTO;
import net.gondr.domain.UserVO;
import net.gondr.service.UserService;
import net.gondr.util.FileUtil;
import net.gondr.validator.RegisterValidator;

@Controller
@RequestMapping("/user/")
public class UserController {
	@Autowired
	private ServletContext context;

	@Autowired
	private UserService service;

	private RegisterValidator validator = new RegisterValidator();

	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String viewRegisterPage(Model model) {
		model.addAttribute("registerDTO", new RegisterDTO());
		return "user/registerPage";
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String userRegist(RegisterDTO dto, Errors errors) throws Exception {
		validator.validate(dto, errors);
		if (errors.hasErrors()) {
			return "user/registerPage";
		}

		String uploadPath = context.getRealPath("/WEB-INF/upload");
		MultipartFile file = dto.getProfileImg();
		String upFile = FileUtil.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());

		// System.out.println(uploadPath + "에 " + upFile + "로 업로드 됨.");

		UserVO user = new UserVO();
		user.setImg(upFile);
		user.setName(dto.getUsername());
		user.setPassword(dto.getPassword());
		user.setUserid(dto.getUserid());

		service.register(user);

		return "redirect:/"; // 회원가입완료후 메인 페이지로 이동
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String userLogin(LoginDTO loginDTO, HttpSession session, Model model) {
		String msg = "로그인 실패, 아이디와 비밀번호를 확인하세요";
		if (loginDTO.getUserid() == "" || loginDTO.getPassword() == "") {
			model.addAttribute("msg", msg);
			return "user/login";
		}

		UserVO user = service.login(loginDTO.getUserid(), loginDTO.getPassword());

		if (user == null) {
			model.addAttribute("msg", msg);
			return "user/login";
		}

		session.setAttribute("user", user);
		return "redirect:/"; // 로그인 성공시 메인페이지로 리다이렉트
	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String viewLoginPage(Model model) {
		model.addAttribute("loginDTO", new LoginDTO());
		return "user/login";
	}

	@RequestMapping(value = "profile/{file:.+}")
	@ResponseBody
	public byte[] getUserProfile(@PathVariable Optional<String> file) throws IOException {
		String uploadPath = context.getRealPath("/WEB-INF/upload");
		String imgFile = "default.png";
		
		if(file.isPresent()) {
			imgFile = file.get();
		}
		try {
			File profile = new File(uploadPath + File.separator + imgFile);
			FileInputStream in = new FileInputStream(profile);
			return IOUtils.toByteArray(in);
		} catch (FileNotFoundException e) {
			File profile = new File(uploadPath + File.separator + "default.png");
			FileInputStream in = new FileInputStream(profile);
			return IOUtils.toByteArray(in);
		}
	}
}
