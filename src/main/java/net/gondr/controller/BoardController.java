package net.gondr.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.gondr.domain.BoardVO;
import net.gondr.domain.UploadResponse;
import net.gondr.util.FileUtil;
import net.gondr.util.MediaUtil;

@Controller
@RequestMapping("/board/")
public class BoardController {
	@Autowired
	private ServletContext context;
	
	@RequestMapping(value = "write", method = RequestMethod.GET)
	public String viewWritePage(Model model) {
		model.addAttribute("boardVO", new BoardVO());
		return "board/write";
	}
	
	@RequestMapping(value="upload", method=RequestMethod.POST)
	@ResponseBody
	public UploadResponse handleImageUpload(@RequestParam("file") MultipartFile file, HttpServletResponse res) {
		String uploadPath = context.getRealPath("/app/images");
		UploadResponse response = new UploadResponse();
		
		try {
			String originalName = file.getOriginalFilename();
			String ext = originalName.substring(originalName.lastIndexOf(".") + 1);
			if(MediaUtil.getMediaType(ext) == null) {
				throw new Exception("올바르지 않은 파일 형식");
			}
			String upFile = FileUtil.uploadFile(uploadPath, originalName, file.getBytes());
			
			// 썸네일 경로 셋팅
			response.setThumbImage("/app/images/" + upFile);
			// 실제파일 경로 셋팅
			response.setUploadImage("/app/images/" + upFile.substring(2, upFile.length()));
			response.setMsg("성공적으로 업로드 됨");			
		} catch (Exception e) {
			e.printStackTrace();
			response.setMsg(e.getMessage());
			response.setResult(false);
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		return response	;
	}
}
