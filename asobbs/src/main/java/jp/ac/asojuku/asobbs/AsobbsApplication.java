package jp.ac.asojuku.asobbs;

import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionTrackingMode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AsobbsApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(AsobbsApplication.class, args);
	}
	 
	/* (非 Javadoc)
	 * warファイルを作成するのに必要らしい
	 * @see org.springframework.boot.web.servlet.support.SpringBootServletInitializer#configure(org.springframework.boot.builder.SpringApplicationBuilder)
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	    return application.sources(AsobbsApplication.class);
	}
	
	@Bean
    public ServletContextInitializer servletContextInitializer(@Value("${secure.cookie}")boolean secure) {

        ServletContextInitializer servletContextInitializer = new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                servletContext.getSessionCookieConfig().setHttpOnly(true);
                servletContext.getSessionCookieConfig().setSecure(secure);
                servletContext.setSessionTrackingModes(
                        Collections.singleton(SessionTrackingMode.COOKIE)
                );
            }
        };
        return servletContextInitializer;
    }
}

