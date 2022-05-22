package com.cloud.web.config.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 로그인 성공 후 항상 실행되는 로직 설정.
 *
 * TestPrincipalDetails user = (TestPrincipalDetails) authentication.getPrincipal();
 * 이렇게 생각한 이유 loadbyusername()에서 TestPrincipalDetails 반환하고 있어서...
 *
 * https://to-dy.tistory.com/94 -> handler 처리 , redirect 처리
 *
 *  spring security에서 제공하는 redirect url를 사용하지 않는 경우, 권한이 없어서 로그인 페이지로 넘어가기 직전 현 페이지의 url를 담을려고 한다.
 *  RequestCache 인터페이스를 이용해, 사용자 요청 정보들이 들어 있는 SavedRequest 클래스 객체를 세션에 저장하게 된다.
 *  그럼 우리는 RequestCache 객체를 생성해 SavedRequest 객체를 가져와서 로그인 화면을 보기 전에 방문했던 URL 정보를 가져오면 된다.
 *
 *  redirect 로 REFERER 헤더 값을 지정한다면, 로그인에 성공하게 되도 다시 로그인 화면을 보여주게 된다.
 *  그렇기 때문에 REFERER 헤더 값을 사용하는 것은 로그인 처리에서는 별로 좋지 못하다.
 *  따라서 우리는 지정된 화면으로 이동하게 만들어 줄 것이다. security 설정에서 defalut-target-url 을 설정한 적이 있다.
 *  로그인 성공시 이동할 url을 설정했는데, 이 url로 이동하게 해주면 된다.
 */
@Configuration
public class LoginSuccessHandler  implements AuthenticationSuccessHandler {


    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStratgy = new DefaultRedirectStrategy();
    private String defaultUrl = "/";


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        PrincipalDetails user = (PrincipalDetails) authentication.getPrincipal(); // 이렇게 생각한 이유 loadbyusername에서 TestPrincipalDetails 반환하고 있어서...

        HttpSession session = request.getSession();

        session.setAttribute("user", user.getUser()); // user dto 객체 그대로 저장


        // spring security에서 제공했던 기능을 직접 코드로 구현
        resultRedirectStrategy(request, response, authentication);

        // 로그인 실패하더라도 다시 로그인 성공하면 해당 실패 session 삭제하는 method
        clearAuthenticationAttributes(request);

        //response.sendRedirect("/");
        // 일반 로그인 하면 다시 main으로 돌아가는건 맞지만, user 권한이 필요한 시점에서
        // 접근할때 로그인 하면 해당 위치에서 계속 작업을 이어갈 수 있는게 아니라 다시 main으로 돌아오게 된다.

    }


    protected void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false); //  세션이 있다면 그 세션을 리턴하지만, 세션이 존재하지 않는다면 null을 리턴한다 .
        if(session==null) return; //세션이 null 즉, 세션에 에러가 없다면 그냥 return 된다.

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        //WebAttributes.AUTHENTICATION_EXCEPTION 이름 값으로 정의된 세션을 지운다.

    }

    protected void resultRedirectStrategy(HttpServletRequest request, HttpServletResponse response,
                                          Authentication authentication) throws IOException, ServletException {

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if(savedRequest!=null) {
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStratgy.sendRedirect(request, response, targetUrl);
        } else {
            redirectStratgy.sendRedirect(request, response, defaultUrl);
        }

    }








}
