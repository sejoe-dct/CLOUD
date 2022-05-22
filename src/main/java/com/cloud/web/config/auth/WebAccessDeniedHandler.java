package com.cloud.web.config.auth;

import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 인증, 인가 거부된 요청시 이동될 페이지는 /err/denied-page 입니다.
 * msg와 nextPage attribute를 이용하여 alert를 띄우며 다음 페이지도 이동됩니다.
 *
 * 로그인 권한이 없는(=인증되지 않은) 사용자의 경우 401 http status code를 (ln 20) 응답하며
 * 강제로 로그아웃 처리되도록 SecurityContextHolder를 clear한 후(ln 21), /login페이지로 redirect 합니다. (ln 19)
 *
 * 접근 권한이 없는(=인가되지 않은) 사용자의 경우 403 http status code를 응답하면서 (ln 9)
 * /v 페이지로 redirect 합니다.
 * (ROLE_ADMIN 권한을 가지고 있지 않은 사용자가 회원 메뉴에 접속했을 때에 관한 처리를 해주는 부분입니다.)
 *
 * forward 방식으로 /err/denied-page로 이동시키고, 그 곳에서 javascript로 msg를 alert를 띄운 후,
 * nextPage에 담긴 페이지로 redirect 시킵니다.
 */
@Configuration
public class WebAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res,
                       AccessDeniedException ade) throws IOException, ServletException {

        res.setStatus(HttpStatus.FORBIDDEN.value());

        if(ade instanceof AccessDeniedException){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(authentication != null ){ // user 인 사용자가 festivals POST 요청 함으로써 행사 등록을 원할때!!
                req.setAttribute("msg","접근 권한 없는 사용자입니다.");
                req.setAttribute("nextPage","/festivals"); // 권한이 없는 사용자가 행사 등록 원할시, 축제 게시글 전체 목록으로 리다이렉트
            }

            /* spring security가 먼저 로그인 인증조차 하지 않는 요청에서 가로채간다. 그래서 주석처리 2022/05/13
            else { // 로그인 조차 X
                req.setAttribute("msg","로그인이 필요한 페이지 입니다.");
                req.setAttribute("nextPage","/login"); // 로그인 폼으로
                res.setStatus(HttpStatus.UNAUTHORIZED.value());
                SecurityContextHolder.clearContext();
            }
            */
        }
        //req.getRequestDispatcher("/err/denied-page").forward(req,res); // /err/denied-page 경로로 req와 res 담아서 보낸다.
        req.getRequestDispatcher("/festivals").forward(req,res);
    }
}
