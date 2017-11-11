package com.springsecurity.demo.service;

import com.springsecurity.demo.common.UserInfo;
import com.springsecurity.demo.domain.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wanli zhou
 * @created 2017-10-30 10:23 AM.
 */
@Component
public class UserService {

    @PersistenceContext
    private EntityManager em;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    @Transactional
    public List<UserInfo> findAllUsers(){
        return em.createQuery("SELECT u FROM User u ", User.class)
                .getResultList().stream().map(UserInfo::new).collect(Collectors.toList());
    }

    @Transactional
    public UserInfo findUserByName(String email){
        return new UserInfo(em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Transactional
    public UserInfo updateUserByName(String email, UserInfo userInfo) {
        User user = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                     .setParameter("email", email)
                     .getSingleResult();
        user.setName(userInfo.getName());
        user.setEmail(userInfo.getEmail());
        user.setePwd(userInfo.getPassword());
        em.persist(user);
        return userInfo;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Transactional
    public void deleteUserByName(String email) {
        em.createQuery("DELETE FROM u User u WHERE u.email =:email")
                .setParameter("email", email).executeUpdate();
    }
}
