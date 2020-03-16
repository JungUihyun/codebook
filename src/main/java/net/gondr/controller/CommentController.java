package net.gondr.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nhncorp.lucy.security.xss.LucyXssFilter;
import com.nhncorp.lucy.security.xss.XssSaxFilter;

import net.gondr.domain.BoardVO;
import net.gondr.domain.CommentVO;
import net.gondr.domain.ExpData;
import net.gondr.domain.UserVO;
import net.gondr.service.BoardService;
import net.gondr.service.FreeBoardService;
import net.gondr.service.UserService;

@Controller
@RequestMapping("/comment/")
public class CommentController {
	@Autowired
	private ServletContext context;

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private FreeBoardService freeService;

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/{flag}/write/{board_idx}", method = RequestMethod.POST)
	public String comment_write(@PathVariable("flag") Integer flag, @PathVariable("board_idx") Integer board_idx, CommentVO comment, HttpSession session, Errors errors, RedirectAttributes rttr) {
		// 여기는 인터셉터에 의해서 로그인하지 않은 사용자는 막히게 될 것이기 때문에 그냥 에러처리 없이 user를 불러써도 된다.
		UserVO user = (UserVO) session.getAttribute("user");

		// 로그인한 사용자의 아이디를 글쓴이로 등록하고
		comment.setWriter(user.getName());
		
		// 댓글 내용
		LucyXssFilter filter = XssSaxFilter.getInstance("lucy-xss-sax.xml");
		String clean = filter.doFilter(comment.getContent());
		comment.setContent(clean);
		
		// 글의 인덱스
		comment.setBoard_idx(board_idx);
		// 실제 DB에 글을 기록함.
		
		
		user = userService.addExp(user.getUserid(), ExpData.SMALL); // 댓글을 한번 쓸 때마다 3의 exp를 지급
		session.setAttribute("user", user); // 갱신 후 세션값 재설정
		
		if( flag == 0 ) {
			boardService.writeComment(comment);
			return "redirect:/board/view/" + board_idx;
		} else if( flag == 1 ) {
			freeService.writeComment(comment);
			return "redirect:/free/view/" + board_idx;
		}
		
		return null;
	}
	
	@RequestMapping(value = "/{flag}/delete/{board_idx}/{idx}", method = RequestMethod.GET)
	public String deleteArticle(@PathVariable("flag") Integer flag, @PathVariable("board_idx") Integer board_idx, @PathVariable("idx") Integer idx, HttpSession session, RedirectAttributes rttr) {
		UserVO user = (UserVO) session.getAttribute("user");
		
		if( flag == 0 ) {
			boardService.deleteComment(idx);
			rttr.addFlashAttribute("msg", "성공적으로 삭제되었습니다.");
			return "redirect:/board/view/" + board_idx;
		} else if( flag == 1 ) {
			freeService.deleteComment(idx);
			rttr.addFlashAttribute("msg", "성공적으로 삭제되었습니다.");
			return "redirect:/free/view/" + board_idx;
		}
		
		return null;
	}
}
