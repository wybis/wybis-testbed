package io.wybis.wybistestbed.service.impl

import groovy.util.logging.Slf4j
import io.wybis.wybistestbed.service.SessionService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import javax.annotation.PostConstruct
import javax.annotation.Resource
import java.util.regex.Matcher
import java.util.regex.Pattern

@Slf4j
class AbstracSessionService implements SessionService {

    protected String host

    protected String port

    protected String pingEndPoint

    protected String loginEndPoint

    protected String logoutEndPoint

    private Pattern sessionIdPattern = Pattern.compile("^JSESSIONID=(.*); Path=.*")

    private boolean loggedIn = false

    private String sessionId

    @Resource
    RestTemplate restTemplate;

    @Override
    boolean isLoggedIn() {
        return false
    }

    @Override
    String getSessionId() {
        return sessionId
    }

    @Override
    boolean ping() {
        HttpHeaders httpHeaders = new HttpHeaders()

        //this.addStandardHeaders(httpHeaders)
        //this.addSessionIdToHeader(httpHeaders)

        HttpEntity<Map<String, String>> reqEntity = new HttpEntity<Map<String, String>>(null, httpHeaders)

        ResponseEntity<String> entity = restTemplate.exchange(
                this.pingEndPoint, HttpMethod.GET, reqEntity, String.class)
        parseAndSetSessionId(entity.getHeaders())
        logEntity(this.loginEndPoint, entity)

        return entity.getStatusCode() == HttpStatus.OK
    }

    @Override
    boolean login() {
        HttpHeaders httpHeaders = new HttpHeaders()

        this.addStandardHeaders(httpHeaders)
        this.addSessionIdToHeader(httpHeaders)

        Map<String, String> user = this.getSessionUser()
        HttpEntity<Map<String, String>> reqEntity = new HttpEntity<Map<String, String>>(user, httpHeaders)

        ResponseEntity<String> entity = restTemplate.exchange(
                this.loginEndPoint, HttpMethod.POST, reqEntity, String.class)
        parseAndSetSessionId(entity.getHeaders())
        logEntity(this.loginEndPoint, entity)

        this.loggedIn = true

        return entity.getStatusCode() == HttpStatus.OK
    }

    @Override
    void execute() {
        log.info('Ping   End Point : {}', this.pingEndPoint)
        log.info('Login  End Point : {}', this.loginEndPoint)
        log.info('Logout End Point : {}', this.logoutEndPoint)
    }

    @Override
    boolean logout() {
        HttpHeaders httpHeaders = new HttpHeaders()

        this.addStandardHeaders(httpHeaders)
        this.addSessionIdToHeader(httpHeaders)

        HttpEntity<String> reqEntity = new HttpEntity<String>(null, httpHeaders)

        ResponseEntity<String> entity = restTemplate.exchange(
                this.logoutEndPoint, HttpMethod.GET, reqEntity, String.class)
        parseAndSetSessionId(entity.getHeaders())
        logEntity(this.logoutEndPoint, entity, false)

        this.loggedIn = false

        return entity.getStatusCode() == HttpStatus.OK
    }

    protected Map<String, String> getSessionUser() {
        Map<String, String> user = ['userId': 'vteial', 'password': 'wybis123']
    }

    protected void addSessionIdToHeader(HttpHeaders httpHeaders) {
        if (this.sessionId != null) {
            httpHeaders.add('Cookie', 'JSESSIONID=' + sessionId)
        }
    }
    protected void addStandardHeaders(HttpHeaders httpHeaders) {
        httpHeaders.add('Content-Type','application/json;charset=UTF-8');
        httpHeaders.add('Accept', 'application/json');
        httpHeaders.add('User-Agent', 'STANDALONE_CLIENT');
    }

    protected void parseAndSetSessionId(HttpHeaders httpHeaders) {
        List<String> s = httpHeaders.get(HttpHeaders.SET_COOKIE)
        if (s != null) {
            Matcher matcher = this.sessionIdPattern.matcher(s.get(0))
            if (matcher.find()) {
                this.sessionId = matcher.group(1)
            }
        }
    }

    protected void logEntity(String endPoint, ResponseEntity entity) {
        logEntity(endPoint, entity, true);
    }

    protected void logEntity(String endPoint, ResponseEntity entity, boolean flag) {
        if (log.isInfoEnabled()) {
            log.info('{} : {}', endPoint, entity.getStatusCode())
            if(flag) {
                log.info('{}', entity.getBody())
            }
        }
        if (log.isDebugEnabled()) {
            log.debug('{}', entity.getHeaders().entrySet())
        }
    }
}
