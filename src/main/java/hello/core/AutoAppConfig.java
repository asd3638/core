package hello.core;

import org.springframework.context.annotation.Configuration;


//이거 하면 스프링이 자동적으로 객체의 중복 생성을 방지하는 클래스로 변환해서 AppConfig를 컨테이너에 등록하게 된다.
@Configuration
public class AutoAppConfig {
}
