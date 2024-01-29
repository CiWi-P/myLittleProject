package com.sp.dabogo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sp.dabogo.domain.Member;

@Controller
@RequestMapping("/member/*")
public class MemberController {
	
	@GetMapping("login")
	public String loginForm() {
		
		return "member/login";
	}
	@PostMapping("login")
	public String loginSubmit(Member dto) {
		System.out.println(dto.getUserId());
		return "redirect:/";
	}
	@GetMapping("member")
	public String memberForm(Model model) {
		
		model.addAttribute("mode", "member");
		return "member/member";
	}
	@PostMapping("member")
	public String memberSubmit(Member dto) {
		System.out.println(dto.getUserId());
		return "redirect:/";
	}
	
	
}
