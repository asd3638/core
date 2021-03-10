package hello.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//스프링부트로 프로젝트 생성하면 최상단 main 파일에 @SpringBootApplication 어노테이션 붙어 있는데 이 안에 ComponentScan에 대한 정보가 붙어 있어
//즉 자동으로 hello.core 패키지 내의 모든 패키지와 파일들을 ComponentScan하도록 설정되어 있다.
@SpringBootApplication
public class CoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

}
