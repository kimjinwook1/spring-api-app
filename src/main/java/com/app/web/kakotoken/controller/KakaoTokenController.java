package com.app.web.kakotoken.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class KakaoTokenController {

	@GetMapping("/login")
	public String login() {
		return "loginForm";
	}

	@GetMapping("/oauth/kakao/callback")
	public @ResponseBody String loginCallback(String code) {
		//authorization code 발급
		return code;
	}
}
