package com.woodare.core.util;

import com.warrenstrange.googleauth.GoogleAuthenticator;

/**
 * 谷歌验证器工具类
 * 
 * @author Luke
 */
public class GoogleVerifyHelper {

	public static void main(String[] args) {
		System.out.println(String.format("%06d", 124));
	}

	/**
	 * 生成验证Key
	 * 
	 * @return
	 */
	public static String createKey() {
		GoogleAuthenticator auth = new GoogleAuthenticator();
		return auth.createCredentials().getKey();
	}

	/**
	 * 生成验证码
	 * 
	 * @param secret
	 * @return
	 */
	public static String getCode(String secret) {
		GoogleAuthenticator gAuth = new GoogleAuthenticator();
		int code = gAuth.getTotpPassword(secret);
		return String.format("%06d", code);
	}

	/**
	 * 校验验证码
	 * 
	 * @param secret
	 * @param smsCode
	 * @return
	 */
	public static boolean authorize(String secret, String smsCode) {
		boolean flg = false;
		try {
			GoogleAuthenticator gAuth = new GoogleAuthenticator();
			flg = gAuth.authorize(secret, Integer.valueOf(smsCode));
		} catch (Exception e) {
			flg = false;
		}
		return flg;
	}
}
