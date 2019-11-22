package com.files.common;

import com.files.v1.remote.call.security.domain.Users;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.files.common.FilesConstants.EDGE_SERVICE_GET_CURRENT_ACCOUNT_URL;
import static com.files.common.FilesConstants.JSESSIONID;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityInterceptor implements HandlerInterceptor {

    @NonNull
    private final CookieParser cookieParser;

    @NonNull
    private final RestTemplate restTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("[SecurityInterceptor] checking current user ...");
        Optional<String> jSessionIdCookie = cookieParser.getCookie(JSESSIONID);

        if (jSessionIdCookie.isPresent()) {
            Users user = getCurrentAccount(jSessionIdCookie.get());
            if (user != null) {
                log.info("[SecurityInterceptor] User is logged: " + user.toString());
                return true;
            }
        }

        log.info("[SecurityInterceptor] checking current user end");

        return false;
    }

    private Users getCurrentAccount(String sessionId) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", JSESSIONID + "=" + sessionId);

        return restTemplate.exchange(EDGE_SERVICE_GET_CURRENT_ACCOUNT_URL,
                HttpMethod.GET,
                new HttpEntity<String>(requestHeaders),
                Users.class).getBody();
    }

}
