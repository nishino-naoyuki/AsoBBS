package jp.ac.asojuku.asobbs.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.ac.asojuku.asobbs.dto.LoginInfoDto;
import jp.ac.asojuku.asobbs.exception.AsoBbsSystemErrException;
import jp.ac.asojuku.asobbs.param.RoleId;
import jp.ac.asojuku.asobbs.param.SessionConst;

@Component
public class PermitCheckFilter  implements Filter{
	private static final Logger logger = LoggerFactory.getLogger(PermitCheckFilter.class);

	@Autowired
	HttpSession session;
	
	//管理者のみアクセス可能
	String[] permitMangerOnly = {
			"/user","/room/input","/room/confirm","/room/insert"
	};
	
	//教員・管理者アクセス可能
	String[] permitKyoin = {
			"/pwd/reset_input","/pwd/reset",
			"/room/edit","/room/confirm_edit","/room/update","/room/complete",
			"bbs/input", "bbs/insert", "bbs/comfirm", "bbs/complete"
	};

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		//リクエストのサーブレットパスを取得
		String servletPath = ((HttpServletRequest)request).getServletPath();
		//セッションからユーザー情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		if( loginInfo == null ) {
			chain.doFilter(request, response);
			return;
		}
		
		logger.trace("PermitCheckFilter:"+servletPath);
		
		//管理者のみ
		if( loginInfo.getRole() != RoleId.MANAGER ) {
			if( Arrays.asList(permitMangerOnly).contains(servletPath) ) {
				//アクセス不可能
				logger.warn("Filter!!! [permit deny1] mail= "+loginInfo.getMail()+" url="+servletPath);
				((HttpServletResponse)response).sendRedirect("/error/accessdeny");
			}
		}

		//管理者・教員のみ
		if( loginInfo.getRole() == RoleId.STUDENT ) {
			if( Arrays.asList(permitKyoin).contains(servletPath) ) {
				//アクセス不可能
				logger.warn("Filter!!! [permit deny2] mail= "+loginInfo.getMail()+" url="+servletPath);
				((HttpServletResponse)response).sendRedirect("/error/error");
			}
		}
		
		chain.doFilter(request, response);
		
	}

}
