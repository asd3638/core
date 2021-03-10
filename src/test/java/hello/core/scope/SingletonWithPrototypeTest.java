package hello.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

import static org.assertj.core.api.Assertions.*;

public class SingletonWithPrototypeTest {

    @Test
    public void prototypeFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        //client 1 이 프로토타입으로 생성되었으니까 빈을 조회할 때에야 객체가 생성되겠지
        PrototypeBean pb1 = ac.getBean(PrototypeBean.class);
        pb1.add();
        //client 2 가 프로토타입으로 생성되었으니까 빈을 조회할 때에야 객체가 생성되겠지
        PrototypeBean pb2 = ac.getBean(PrototypeBean.class);
        pb2.add();

        //각각 다른 객체가 생성되고 생성되면서 count값이 초기화 되었을 테니까 둘다 count값은 1을 가지고 있을 것이다.
        assertThat(pb2.getCount()).isSameAs(1);
    }

    @Test
    public void singletonClientUsePrototype() {
        //의존 관계 주입까지 있어서 내가 빈에 등록한 클래스가 2개 이상이잖아 그럴때는 파라미터로 등록한 클래스 다 적어주면 된다.
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int countByClient1 = clientBean1.logic();
        assertThat(countByClient1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int countByClient2 = clientBean2.logic();
        assertThat(countByClient2).isEqualTo(1);
        //싱글톤 객체는 값을 저장해두면 절대 안되는 이유에 대한 실습이기도 하다. 싱글톤은 객체를 하나만 생성하고 클라이언트들이 그 객체를 공유해서 사용하기 때문에 각각의 클라이언트별로 객체에 값을 저장해두면 절대 안된다
        //assertThat(countByClient1).isNotSameAs(countByClient2);
    }

    //근데 문제는 이렇게 사용자가 조회를 해야만 스프링 빈으로 등록되었다가 사용 후에는 관리 되지 않는 프로토타입 형식의 클래스와 싱글톤 클래스가 함께 사용되었을 경우이다.

    //빈칸으로 두면 싱글톤이 디폴트
    //클래스 안에 클래스를 넣고 싶을 때 의존관계 주입을 꼭 생각해 어떻게 하는지 코드 반복 작성하면서 익히자
    //우선 선언은 해주는데 다른 데서 변수 잘못 건들이지 못하게 private으로 선언하고
    //값을 생성자 주입을 통해 할당할 것이니까 final선언도 해주고
    //마지막으로 제일 중요!!!!!
    //의존 관계가 있다는 것을 명시해 @Autowired 어노테이션으로
    //지금가지 쓴 것은 생성자가 하나 밖에 없기 때문에 생성자 다 지우고 그냥 클래스 위에 @RequiredArgsContructor 어노테이션 해줘도 된다.

    static class ClientBean {
        //의존성 주입은 했지만 나는 그 안에 객체는 PrototypeBean은 프로토타입 스코프로 지정해주고 싶어
        //그럴 때 사용하는 게 ObjectProvider
        private final Provider<PrototypeBean> prototypeBeanProvider;
        //얘는 의존성 주입으로 인해 ClientBean이 스프링 컨테이너에 등록되는 그 시점에 이미 주입이 완료된다.
        //그리고 나서는 계속 같은 객체를 쓰게 된다.
        //사용자가 조회할 때 생성해서 사용을 완료하면 스프링 컨테이너에서 없어지는 프로토타입 스코프 개념과 충돌

        @Autowired
        public ClientBean(Provider<PrototypeBean> prototypeBeanProvider) {
            this.prototypeBeanProvider = prototypeBeanProvider;
        }

        public int logic() {
            PrototypeBean object = prototypeBeanProvider.get();
            //이때서야 스프링 컨테이너에서 프로토타입빈을 찾아서 반환해준다.
            object.add();
            return object.getCount();
        }
    }
    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;
        public void add() {
            count++;
        }
        public int getCount() {
            return count;
        }
        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init" + this);
        }
        @PreDestroy
        public void close() {
            System.out.println("PrototypeBean.close");
        }
    }
}
