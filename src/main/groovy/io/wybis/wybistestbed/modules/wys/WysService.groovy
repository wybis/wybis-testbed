package io.wybis.wybistestbed.modules.wys

import groovy.util.logging.Slf4j
import io.wybis.wybistestbed.service.impl.AbstracSessionService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct

@Service('wys')
//@Scope('prototype')
@Slf4j
class WysService extends AbstracSessionService {

//    @Value('localhost')
//    String host
//
//    @Value('1111')
//    String port
//
//    @Value('/ping')
//    String pingEndPoint
//
//    @Value('/sessions/login')
//    String loginEndPoint
//
//    @Value('/sessions/logout')
//    String logoutEndPoint

    @PostConstruct
    void init() {
        host = 'localhost'
        port = '1111'
        pingEndPoint = '/ping'
        loginEndPoint = '/sessions/login'
        logoutEndPoint = 'sessions/logout'

        pingEndPoint = "http://${this.host}:${this.port}${this.pingEndPoint}"
        loginEndPoint = "http://${this.host}:${this.port}${this.loginEndPoint}"
        logoutEndPoint = "http://${this.host}:${this.port}${this.logoutEndPoint}"

        log.info('Ping   End Point : {}', pingEndPoint)
        log.info('Login  End Point : {}', loginEndPoint)
        log.info('Logout End Point : {}', loginEndPoint)
    }

    @Override
    protected Map<String, String> getSessionUser() {
        Map<String, String> user = ['userId' : 'vteial', 'password' : 'wybis123']
    }

    @Override
    void execute() {
        log.info('Need to be implement...');
    }
}
