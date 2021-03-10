package hello.core.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

//스프링 빈에 등록하고 싶으면 무조건 @Service, @Repository, @Conponent등으로 컴포넌트 스캔을 받을 수 있게 어노테이션 적어줘야한다.
@Component
//scope를 request를 사용했기 때문에 http 요청 당 하나씩 생성되고 요청이 종료되면 이 스코프도 종료된다.
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
//스코프를 이렇게 설정하면 component로 스캔을 한다고 하더라도 이게 실제로 스프링 빈에 등록되고 사용되는 건 request가 들어와서 나갈 때 까지다.
//만약 이 클래스가 생존 범위가 스프링 컨테이너가 생성되는 클래스 안으로 의존성 주입이 된다면 당연히 오류를 발생시킨다.
//이 문제에 대한 해결 방안은 아까 프로토 타입 클래스 해결한 것과 비슷한데 그때도 사용자가 prototypeBean을 조회할 때 생성하고 싶어서 객체를 그냥 선언하지 않고
//ObjectProvider<PrototypeBean> 이런식으로 Provider에 묶어서 생선한 뒤 .getObject나 .get으로 가져왔는데
//지금도 그렇게 사용하면 된다.
//request왔을 때 MyLogger 빈이 등록되게 설정해주면 된다.
public class MyLogger {
    private String uuid;
    private String requestURL;

    //수정자
    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }
    public void log(String message) {
        System.out.println("[" + uuid + "]" + " [" + requestURL + "]" + "[" + message + "]");
    }
    //처음 스트링 빈에 등록할 때 수행된다.
    @PostConstruct
    public void init() {
        //빈에 등록할 때 실행되는 것!
        //request scope는 아까도 말했 듯이 http요청이 오면 올때마다 클라이언트 구분을 하면서 특정하게 생성된다.
        //이를 uuid로 구분해서 확인할 수 있다.
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "]" + " request bean created" + this);
    }
    @PreDestroy
    public void close() {
        System.out.println("[" + uuid + "]" + " request bean closed" + this);

    }
}
