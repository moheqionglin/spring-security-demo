package com.springsecurity.demo.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author wanli zhou
 * @created 2017-10-09 11:02 PM.
 */
@Entity
@Table(name = "auth_tokens")
public class AuthToken implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	private String token;
	private Date expiredAt;
	private String ip;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(Date expiredAt) {
		this.expiredAt = expiredAt;
	}

	public String getIp() {
		return ip;
	}
	public boolean isNotExpired() {
		return getExpiredAt().after(new Date());
	}
	@Override
	public String toString() {
		return "AuthToken{" +
				"id=" + id +
				", user=" + user +
				", token='" + token + '\'' +
				", expiredAt=" + expiredAt +
				", ip='" + ip + '\'' +
				'}';
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
