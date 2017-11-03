package com.springsecurity.demo.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.regex.Pattern;
/**
 * @author wanli zhou
 * @created 2017-10-24 11:06 PM.
 */
@Component
public class ClientIpFinder {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private final String regex = "^((10\\.\\d{1,3})|(172\\.((1[6-9])|(2[0-9])|3[0-1]))|(192\\.168))\\.\\d{1,3}\\.\\d{1,3}$";
	private final Pattern localAreaNetworkRegex;

	public ClientIpFinder(){
		localAreaNetworkRegex = Pattern.compile(regex);
	}

	public String findIp(HttpServletRequest request){
//		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//		HttpServletRequest httpReq = sra.getRequest();
		String ipAddress = request.getRemoteAddr();
		for (final Enumeration<String> headers = request.getHeaderNames(); headers.hasMoreElements(); ) {
			String header = headers.nextElement();
			if (header.equalsIgnoreCase("x-forwarded-for")) {
				ipAddress = request.getHeader(header);
				break;
			}
		}
		String ipAddressWithoutLat = removeLocalAreaNetworkIp(ipAddress);
		log.debug("We find the X-Forwaded-For header of: {}/{}", ipAddress, ipAddressWithoutLat);
		return ipAddressWithoutLat;
	}

	private String removeLocalAreaNetworkIp(String ipAddress) {
		if(ipAddress == null){
			return null;
		}
		ipAddress = ipAddress.replaceAll("\\[|\\]", "");
		String resultIp = "";
		for(String ip : ipAddress.split(",")){
			ip = ip.trim();
			if(this.localAreaNetworkRegex.matcher(ip).matches()){
				continue;
			}
			if(!resultIp.equals("")){
				resultIp += ", ";
			}
			resultIp += ip;
		}
		return resultIp;
	}

}
