package johncervantes.springproject.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class WebSuccessHandler implements AuthenticationSuccessHandler {

	public final Integer SESSION_TIMEOUT_IN_SECONDS = 60 * 30;
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		request.getSession().setMaxInactiveInterval(SESSION_TIMEOUT_IN_SECONDS);
	}

}
