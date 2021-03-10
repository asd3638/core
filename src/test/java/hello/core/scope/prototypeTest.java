package hello.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class prototypeTest {
    @Test
    public void prototypeBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(prototypeBean.class);
        prototypeBean pb1 = ac.getBean(prototypeBean.class);
        System.out.println("pb1 = " + pb1);
        prototypeBean pb2 = ac.getBean(prototypeBean.class);
        System.out.println("pb2 = " + pb2);
        //프로토타입 스코프로 생성된 객체도 싱글톤인지 확인
        Assertions.assertThat(pb1).isNotSameAs(pb2);

        ac.close();
    }

    @Scope("prototype")
    static class prototypeBean {
        @PostConstruct
        public void init() {
            System.out.println("prototypeBean.init");
        }
        @PreDestroy
        public void close() {
            System.out.println("prototypeBean.close");
        }
    }
}
