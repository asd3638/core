package hello.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class singletonTest {
    @Test
    public void singletonBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class);
        System.out.println("find singleton object singleton1");
        SingletonBean singletonBean1 = ac.getBean(SingletonBean.class);
        //여기서 객체 생성하고 의존관계 주입하고 init실행한다고 생각하면 된다!!!
        //여기!!!
        System.out.println("singletonBean1 = " + singletonBean1);
        System.out.println("find singleton object singleton2");
        SingletonBean singletonBean2 = ac.getBean(SingletonBean.class);
        System.out.println("singletonBean2 = " + singletonBean2);
        Assertions.assertThat(singletonBean1).isSameAs(singletonBean2);
        //당연히 싱글톤 객체니까 두 개가 서로 같은 객체이다.

        ac.close();
    }

    @Scope()
    static class SingletonBean {
        @PostConstruct
        public void init() {
            System.out.println("SingletonBean.init");
        }
        @PreDestroy
        public void close() {
            System.out.println("SingletonBean.close");
        }
    }
}
