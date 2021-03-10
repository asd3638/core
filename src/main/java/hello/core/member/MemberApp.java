package hello.core.member;

import hello.core.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {
    public static void main(String[] args) {

        /*AppConfig appConfig = new AppConfig();
        MemberService memberService = appConfig.memberService();*/

        //스프링에서 모든것을 관리해주는 역할을 하는 ApplicationContext!
        //파라미터로 AppConfig를 넣어주면 얘가 스프링 컨테이너에 AppConfig에 작성한 메소드들 다 등록해놔서 관리한다.
        //결국 하나하나 AppConfig 객체 생성하고 메소드 불러서 사용할 필요가 이제 없는거야
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        //AppConfig에 작성해두었던 메소드들 중에서 내가 사용할 메소드를 getBean메소드를 이용해서 꺼내는데 파라미터로는 해당 메소드 이름과 리턴 타입명을 적어주면 된다.
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);

        Member member = new Member(1L, "spring", Grade.BASIC);
        memberService.join(member);
        Member result = memberService.findMember(1L);

        System.out.println("new member = " + member.getName());
        System.out.println("find member = " + result.getName());
    }
}
