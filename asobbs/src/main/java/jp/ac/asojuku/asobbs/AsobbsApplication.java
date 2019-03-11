package jp.ac.asojuku.asobbs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

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
}

