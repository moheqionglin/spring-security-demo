package com.springsecurity.demo.service;

import com.springsecurity.demo.common.AuthResponse;
import com.springsecurity.demo.common.UserInfo;
import com.springsecurity.demo.domain.AuthToken;
import com.springsecurity.demo.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

/**
 * @author wanli zhou
 * @created 2017-10-29 10:23 PM.
 */
@Component
public class AuthService {
    static int TOKEN_HOURS_TO_LIVE = 72;
    static int MIN_REFRESH_TOKEN_HOURS = 1;
    Logger log = LoggerFactory.getLogger(this.getClass());
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public AuthResponse verifyPasswordFromDB(String email, String password){
        AuthResponse rsp = new AuthResponse();
        rsp.setUser(null);
        rsp.setToken(null);
        rsp.setAuthenticate(false);

        try {
            User authUser = em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.ePwd = :epwd", User.class)
                    .setParameter("email", email)
                    .setParameter("epwd", password)
                    .getSingleResult();

            if (authUser != null) {
                UserInfo u = new UserInfo(authUser);
                rsp.setUser(u);
                rsp.setToken(UUID.randomUUID().toString());
                rsp.setAuthenticate(true);

                AuthToken authToken = new AuthToken();
                authToken.setIp("ip");
                authToken.setExpiredAt(getExpireTime());
                authToken.setToken(rsp.getToken());
                authToken.setUser(authUser);
                em.persist(authToken);

            }
        }catch (Exception e){
            log.error("Login service error, password or username error ");
        }
        return rsp;
    }

    @Transactional
    public void removeTokenByEmail(Integer id){
        try {
            em.createQuery("delete from AuthToken at where at.user.id = :id")
                    .setParameter("id", id).executeUpdate();
        }catch (Exception e){
            log.error("delete token error ");
        }
    }
    private Date getExpireTime() {

        ZoneId zone = ZoneId.systemDefault();
        Instant instant = LocalDateTime.now().plusHours(TOKEN_HOURS_TO_LIVE).atZone(zone).toInstant();
        return Date.from(instant);
    }

    @Transactional
    public AuthResponse preAuthVerify(String ip, String token){
        AuthResponse rsp = new AuthResponse();
        rsp.setUser(null);
        rsp.setToken(null);
        rsp.setAuthenticate(false);
        try {
            AuthToken authToken = em.createQuery("SELECT authToken FROM AuthToken authToken WHERE authToken.token = :token", AuthToken.class)
                    .setParameter("token", token)
                    .getSingleResult();
            if(authToken.isNotExpired()){
                attemptUpdateExpiredTime(authToken);
                rsp.setUser(new UserInfo(authToken.getUser()));
                rsp.setToken(authToken.getToken());
                rsp.setAuthenticate(true);
            }else{
                log.debug("Removing expired token.");
                em.remove(authToken);
            }

        }catch (Exception e){
            log.error("Pre Auth verify no token found");

        }
        return rsp;
    }

    private void attemptUpdateExpiredTime(AuthToken authToken) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiredAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(authToken.getExpiredAt().getTime()), ZoneId.systemDefault()) ;
        if(now.plusHours(MIN_REFRESH_TOKEN_HOURS).compareTo(expiredAt) > 0){
            Date newExpireAt = getExpireTime();
            em.createQuery("UPDATE AuthenticationToken t SET t.expiresAt = :expiresAt WHERE t.id = :id ")
                    .setParameter("id", authToken.getId())
                    .setParameter("expiresAt", newExpireAt)
                    .executeUpdate();
        }
    }

}
