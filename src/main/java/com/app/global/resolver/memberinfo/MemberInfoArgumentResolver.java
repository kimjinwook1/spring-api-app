package com.app.global.resolver.memberinfo;

import com.app.domain.member.constant.Role;
import com.app.global.jwt.service.TokenManager;
import io.jsonwebtoken.Claims;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class MemberInfoArgumentResolver implements HandlerMethodArgumentResolver {

	private final TokenManager tokenManager;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// 리턴 값이 true이면 resolveArgument 로직이 실행, false면 resolveArgument 로직이 실행 X
		boolean hasMemberInfoAnnotation = parameter.hasParameterAnnotation(MemberInfo.class);
		boolean hasMemberInfoDto = MemberInfoDto.class.isAssignableFrom(parameter.getParameterType());
		return hasMemberInfoAnnotation && hasMemberInfoDto;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		String authorizationHeader = request.getHeader("Authorization");

		String token = authorizationHeader.split(" ")[1];
		Claims tokenClaims = tokenManager.getTokenClaims(token);

		Long memberId = Long.valueOf((Integer) tokenClaims.get("memberId"));
		String role = (String) tokenClaims.get("role");

		return MemberInfoDto.builder()
				.memberId(memberId)
				.role(Role.from(role))
				.build();

	}
}
