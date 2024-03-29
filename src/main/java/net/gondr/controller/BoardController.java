package net.gondr.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nhncorp.lucy.security.xss.LucyXssFilter;
import com.nhncorp.lucy.security.xss.XssSaxFilter;

import net.gondr.domain.BoardVO;
import net.gondr.domain.CommentVO;
import net.gondr.domain.Criteria;
import net.gondr.domain.ExpData;
import net.gondr.domain.UploadResponse;
import net.gondr.domain.UserVO;
import net.gondr.service.BoardService;
import net.gondr.service.UserService;
import net.gondr.util.FileUtil;
import net.gondr.util.MediaUtil;
import net.gondr.validator.BoardValidator;

@Controller
@RequestMapping("/board/")
public class BoardController {
	@Autowired
	private ServletContext context;

	@Autowired
	private BoardService service;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "write", method = RequestMethod.GET)
	public String viewWritePage(Model model) {
		model.addAttribute("boardVO", new BoardVO());
		return "board/write";
	}

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public UploadResponse handleImageUpload(@RequestParam("file") MultipartFile file, HttpServletResponse res) {
		String uploadPath = context.getRealPath("/app/images");
		UploadResponse response = new UploadResponse();

		try {
			String originalName = file.getOriginalFilename();
			String ext = originalName.substring(originalName.lastIndexOf(".") + 1);
			if (MediaUtil.getMediaType(ext) == null) {
				throw new Exception("올바르지 않은 파일 형식");
			}
			String upFile = FileUtil.uploadFile(uploadPath, originalName, file.getBytes());

			// 썸네일 경로 셋팅
			response.setThumbImage("/app/images/" + upFile);
			// 실제파일 경로 셋팅
			response.setUploadImage("/app/images/" + upFile.substring(2, upFile.length()));
			response.setMsg("성공적으로 업로드 됨");
			response.setResult(true);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMsg(e.getMessage());
			response.setResult(false);
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		return response;

	}

	@RequestMapping(value = "write", method = RequestMethod.POST)
	public String writeProcess(BoardVO board, HttpSession session, Errors errors, RedirectAttributes rttr) {
		// 올바른 값인지 벨리데이팅
		new BoardValidator().validate(board, errors);
		if (errors.hasErrors()) {
			return "board/write"; // 에러가 존재하면 글쓰기 페이지로 보냄.
		}
		// 여기는 인터셉터에 의해서 로그인하지 않은 사용자는 막히게 될 것이기 때문에 그냥 에러처리 없이 user를 불러써도 된다.
		UserVO user = (UserVO) session.getAttribute("user");

		if (board.getId() != null) {
			BoardVO data = service.viewArticle(board.getId());
			if (data == null || !user.getUserid().equals(data.getWriter())) {
				rttr.addFlashAttribute("msg", "권한이 없습니다.");
				return "redirect:/board/list";
			}
		}

		// 로그인한 사용자의 아이디를 글쓴이로 등록하고
		board.setWriter(user.getUserid());

		LucyXssFilter filter = XssSaxFilter.getInstance("lucy-xss-sax.xml");
		String clean = filter.doFilter(board.getContent());
		board.setContent(clean);

		// 실제 DB에 글을 기록함.
		if (board.getId() != null) {
			// 글 수정
			service.updateArticle(board);
		} else {
			// 글 작성
			service.writeArticle(board);
			user = userService.addExp(user.getUserid(), ExpData.MEDIUM); // 글을 한번 쓸 때마다 5의 exp를 지급
			session.setAttribute("user", user); // 갱신 후 세션값 재설정
		}

		return "redirect:/board/list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewArticle(@PathVariable Integer id, Model model, Criteria criteria) {
		BoardVO board = service.viewArticle(id);
		model.addAttribute("board", board);
		
		model.addAttribute("commentVO", new CommentVO());
		List<CommentVO> comment_list = service.getCommentList((criteria));
		model.addAttribute("comments", comment_list);
		
		Integer cnt = service.countComment(criteria);
		criteria.calculate(cnt);
		
		return "board/view";
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String viewList(Criteria criteria, Model model) {
		List<BoardVO> list = service.getArticleList((criteria));
		model.addAttribute("list", list);

		Integer cnt = service.countArticle(criteria);
		criteria.calculate(cnt);

		return "board/list";
	}

	@RequestMapping(value = "write/{id}", method = RequestMethod.GET)
	public String viewModPage(Model model, @PathVariable("id") Integer id, HttpSession session,
			RedirectAttributes rttr) {

		BoardVO data = service.viewArticle(id);
		UserVO user = (UserVO) session.getAttribute("user");

		if (data == null || !data.getWriter().equals(user.getUserid())) {
			rttr.addFlashAttribute("msg", "수정할 권한이 없습니다.");
			return "redirect:/board/list";
		}

		model.addAttribute("boardVO", data);
		return "board/write";
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public String deleteArticle(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes rttr) {
		UserVO user = (UserVO) session.getAttribute("user");
		BoardVO data = service.viewArticle(id);

		if (!user.getUserid().equals(data.getWriter())) {
			rttr.addFlashAttribute("msg", "삭제 권한이 없습니다.");
			return "redirect:/board/view/" + data.getId();
		}

		service.deleteArticle(id);
		rttr.addFlashAttribute("msg", "성공적으로 삭제되었습니다.");
		return "redirect:/board/list";
	}
}
