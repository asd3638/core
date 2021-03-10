package hello.core.lifecycle;

//스프링에 종속적인게 아니고 자바 표준이라는 뜻이다.
//스프링이 아닌 다른 컨테이너에서도 잘 동작한다.
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class NetworkClient {
    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //서비스 시작 시 불러지는 메소드
    public void connect() {
        System.out.println("url = " + url);
    }
    public void call (String message) {
        System.out.println("call : " + url + "message : " + message);
    }
    public void disconnect () {
        System.out.println("close : " + url);
    }

    //의존 관계 주입이 다 끝나고 나서 호출이 되는 부분
    /*@Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("NetworkClient.afterPropertiesSet");
        connect();
        //원래 생성자 안에 있을 때에는 null값인 채로 초기화된 url정보만 담고 있었는데 콜백을 사용해서
        //의존 관계 주입이 모두 끝난 후에 호출되기 때문에 정상적으로 등록한 url정보가 담긴 채로 connect메소드를 실행하게 된다.
        call("초기화 연결 메세지");
    }*/

    //스프링 빈 사용이 모두 종료되고 호출이 되는 부분분
    //순서에 맞게 의존관계 끊어지고 스프링 빈들 싱글톤 객체들이 소멸하게 되는데
    //그 이후에 이 메소드가 불리게 된다.
   /*@Override
    public void destroy() throws Exception {
       System.out.println("NetworkClient.destroy");
       disconnect();
    }*/
    //빈 등록과 의존 관계 주입의 시점에 대한 콜백은 3가지 방법이 있지만 이게 가장 권하는 방법이다.
    //생성되는 대상이 되는 객체에 메소드로 의존 관계 주입 후 실행되는 메소드, 관계 해제 후 실행되는 메소드들을 각각 설정하고
    //어노테이션으로 PostConstruct, PreDestroy 명시해주면 된다.
    @PostConstruct
    public void init() {
        System.out.println("init");
        connect();
        //원래 생성자 안에 있을 때에는 null값인 채로 초기화된 url정보만 담고 있었는데 콜백을 사용해서
        //의존 관계 주입이 모두 끝난 후에 호출되기 때문에 정상적으로 등록한 url정보가 담긴 채로 connect메소드를 실행하게 된다.
        call("초기화 연결 메세지");
    }
    @PreDestroy
    public void close() {
        System.out.println("close");
        disconnect();
    }
}
