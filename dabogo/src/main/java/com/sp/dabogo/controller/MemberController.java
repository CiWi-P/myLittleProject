package com.sp.dabogo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sp.dabogo.domain.Member;
import com.sp.dabogo.domain.SessionInfo;
import com.sp.dabogo.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/member/*")
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	// login 폼은 GET으로 처리하며,
		// login 실패시 loginFailureHandler 에서 /member/login 으로 설정
		//     하여 POST로 다시 이 주소로 이동하므로 GET과 POST를 모두 처리하도록 주소를 매핑	
		@RequestMapping(value = "login", method = {RequestMethod.GET, RequestMethod.POST})
		public String loginForm() {
			return "member/login";
		}

		@GetMapping("member")
		public String memberForm(Model model) {
			model.addAttribute("mode", "member");
			return "member/member";
		}

		@PostMapping("member")
		public String memberSubmit(Member dto,
				final RedirectAttributes reAttr,
				Model model) {

			try {
				memberService.insertMember(dto);
			} catch (DuplicateKeyException e) {
				// 기본키 중복에 의한 제약 조건 위반
				model.addAttribute("mode", "member");
				model.addAttribute("message", "아이디 중복으로 회원가입이 실패했습니다.");
				return "member/member";
			} catch (DataIntegrityViolationException e) {
				// 데이터형식 오류, 참조키, NOT NULL 등의 제약조건 위반
				model.addAttribute("mode", "member");
				model.addAttribute("message", "제약 조건 위반으로 회원가입이 실패했습니다.");
				return "member/member";
			} catch (Exception e) {
				model.addAttribute("mode", "member");
				model.addAttribute("message", "회원가입이 실패했습니다.");
				return "member/member";
			}

			StringBuilder sb = new StringBuilder();
			sb.append(dto.getUserName() + "님의 회원 가입이 정상적으로 처리되었습니다.<br>");
			sb.append("메인화면으로 이동하여 로그인 하시기 바랍니다.<br>");

			// 리다이렉트된 페이지에 값 넘기기
			reAttr.addFlashAttribute("message", sb.toString());
			reAttr.addFlashAttribute("title", "회원 가입");

			return "redirect:/member/complete";
		}

		@GetMapping("complete")
		public String complete(@ModelAttribute("message") String message) throws Exception {
			if (message == null || message.length() == 0) { // F5를 누른 경우
				return "redirect:/";
			}
			
			return "member/complete";
		}


		@GetMapping("pwd")
		public String pwdForm(String dropout, Model model) {

			if (dropout == null) {
				model.addAttribute("mode", "update");
			} else {
				model.addAttribute("mode", "dropout");
			}

			return "member/pwd";
		}

		@PostMapping("pwd")
		public String pwdSubmit(@RequestParam String userPwd,
				@RequestParam String mode, 
				final RedirectAttributes reAttr,
				HttpSession session,
				Model model) {

			SessionInfo info = (SessionInfo) session.getAttribute("member");

			Member dto = memberService.findById(info.getUserId());
			if (dto == null) {
				session.invalidate();
				return "redirect:/";
			}

			// 패스워드 검사
			boolean bPwd = memberService.isPasswordCheck(info.getUserId(), userPwd);
			if ( ! bPwd ) {
				if (mode.equals("update")) {
					model.addAttribute("mode", "update");
				} else {
					model.addAttribute("mode", "dropout");
				}
				model.addAttribute("message", "패스워드가 일치하지 않습니다.");
				return "member/pwd";
			}

			if (mode.equals("dropout")) {
				// 세션 정보 삭제
				session.removeAttribute("member");
				session.invalidate();

				StringBuilder sb = new StringBuilder();
				sb.append(dto.getUserName() + "님의 회원 탈퇴 처리가 정상적으로 처리되었습니다.<br>");
				sb.append("메인화면으로 이동 하시기 바랍니다.<br>");

				reAttr.addFlashAttribute("title", "회원 탈퇴");
				reAttr.addFlashAttribute("message", sb.toString());

				return "redirect:/member/complete";
			}

			// 회원정보수정폼
			model.addAttribute("dto", dto);
			model.addAttribute("mode", "update");
			return "member/member";
		}

		@PostMapping("update")
		public String updateSubmit(Member dto,
				final RedirectAttributes reAttr,
				Model model) {

			try {
				memberService.updateMember(dto);
			} catch (Exception e) {
			}

			StringBuilder sb = new StringBuilder();
			sb.append(dto.getUserName() + "님의 회원정보가 정상적으로 변경되었습니다.<br>");
			sb.append("메인화면으로 이동 하시기 바랍니다.<br>");

			reAttr.addFlashAttribute("title", "회원 정보 수정");
			reAttr.addFlashAttribute("message", sb.toString());

			return "redirect:/member/complete";
		}

		@GetMapping(value = "noAuthorized")
		public String noAuthorized() {
			return "member/noAuthorized";
		}

		@GetMapping(value="expired")
		public String expired() {
			return "member/expired";
		}
	
	
}
