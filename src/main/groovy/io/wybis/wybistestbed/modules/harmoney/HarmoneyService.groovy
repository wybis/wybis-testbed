package io.wybis.wybistestbed.modules.harmoney

import groovy.util.logging.Slf4j
import io.wybis.wybistestbed.service.impl.AbstracSessionService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct

@Service('harmoney')
//@Scope('prototype')
@Slf4j
class HarmoneyService extends AbstracSessionService {

//    @Value('localhost')
//    String host
//
//    @Value('8181')
//    String port
//
//    @Value('/ping')
//    String pingEndPoint
//
//    @Value('/sessions/singIn')
//    String loginEndPoint
//
//    @Value('/sessions/signOut')
//    String logoutEndPoint

    @PostConstruct
    void init() {
        host = 'localhost'
        port = '8181'
        pingEndPoint = '/harmoney2/ping'
        loginEndPoint = '/harmoney2/sessionService/authenticate'
        logoutEndPoint = '/harmoney2/sessionService/logout'

        pingEndPoint = "http://${this.host}:${this.port}${this.pingEndPoint}"
        loginEndPoint = "http://${this.host}:${this.port}${this.loginEndPoint}"
        logoutEndPoint = "http://${this.host}:${this.port}${this.logoutEndPoint}"

        log.info('Ping   End Point : {}', pingEndPoint)
        log.info('Login  End Point : {}', loginEndPoint)
        log.info('Logout End Point : {}', loginEndPoint)
    }

    @Override
    protected Map<String, String> getSessionUser() {
        Map<String, String> user = ['id' : 'vteial', 'password' : '123']
    }

    @Override
    void execute() {
        log.info('Need to be implement...');
    }
}
