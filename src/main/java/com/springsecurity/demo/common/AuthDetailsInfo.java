package com.springsecurity.demo.common;

import java.io.Serializable;

/**
 * @author wanli zhou
 * @created 2017-10-29 6:06 PM.
 */

public class AuthDetailsInfo implements Serializable{
	
	private final String token;
	private final String ipAddress;
	private final String authFrom;

	public AuthDetailsInfo(String token, String ipAddress, String authFrom) {
		this.token = token;
		this.ipAddress = ipAddress;
		this.authFrom = authFrom;
	}

	@Override
	public String toString() {
		return "AuthDetailsInfo{" +
				"token='" + token + '\'' +
				", ipAddress='" + ipAddress + '\'' +
				", authFrom='" + authFrom + '\'' +
				'}';
	}

	public String getToken() {
		return token;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String getAuthFrom() {
		return authFrom;
	}
}
