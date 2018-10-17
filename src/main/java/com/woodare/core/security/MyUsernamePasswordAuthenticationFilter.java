/******************************************************************************
 *                                                                             
 *                      Woodare PROPRIETARY INFORMATION                        
 *                                                                             
 *          The information contained herein is proprietary to Woodare         
 *           and shall not be reproduced or disclosed in whole or in part      
 *                    or used for any design or manufacture                    
 *              without direct written authorization from FengDa.              
 *                                                                             
 *            Copyright (c) 2013 by Woodare.  All rights reserved.             
 *                                                                             
 *****************************************************************************/
package com.woodare.core.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.woodare.framework.utils.StringUtils;
import com.woodare.framework.utils.SysProperties;

/**
 * @author lu_feng
 */
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		Authentication authentication = super.attemptAuthentication(request, response);

		if (authentication != null) {
			UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

			Date authBindDate = user.getUser().getAuthBindDate();
			if (authBindDate != null && !"dev".equals(SysProperties.getInstance().getProperty("deploy.mode"))) {
				String regcode = request.getParameter("regcode");
				if (StringUtils.isEmpty(regcode)) {
					throw new CustomMsgAuthenticationException("二次验证失败");
				}
				else {
					GoogleAuthenticator gAuth = new GoogleAuthenticator();
					if (!gAuth.authorize(user.getUser().getAuthKey(), Integer.parseInt(regcode))) {
						throw new CustomMsgAuthenticationException("二次验证失败");
					}
				}
			}
		}
		return authentication;
	}

}
