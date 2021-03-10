package hello.core.web;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
//계속 반복해서 왜우자 저거 있으면 생성자 주입 자동으로 해준다.
public class LogDemoController {

    private final LogDemoService logDemoService;
    //RequiredArgsConstructor있으니까 따로 생성자는 만들 필요가 없어짐.
    private final MyLogger myLogger;
    //이러면 MyLogger을 바로 선언하고 할당하는게 아니라 MyLogger를 찾을 수 있는 Dependency Lookup을 할당받게 된다.
    //직접 사용하는 코드로 MyLogger에 대한 값 할당 시점을 넘겨버렸다.
    //얘는 주입 시점에 MyLogger을 사용하는게 아니니까 이런 식으로 선언해도 오류를 나타내지 않는다.

    //여기까지 작성하고 실행하면 실행이 안되는게 당연...!
    //이유는 이것도 의존 관계 주입과 관련이 있는데
    //LogDemoController 는 @Controller annotation을 해놨기 때문에 스프링이 실행될 때 scan돌면서 bean등록 확인할 때 등록이 된다.
    //당연히 생성자 주입을 통해 의존 관계 주입한 logDemoService, myLogger도 자동으로 빈에 등록되려고 하는데
    //중요한건 myLogger 클래스는 scope가 ("request")로 생성이 되었다. 즉 이건 앞선 LogDemoController와는 다르게 request가 요청되어야
    //빈에 등록되고 실행된다.
    //이 차이로 인해 오류가 발생한다.


    @RequestMapping("log-demo")
    @ResponseBody
    //보통 controller에서 분기하면 return과 이름이 일치하는 view을 찾아서 html문서를 화면에 뿌려서 출력한다.
    //근데 @RequestBody 어노테이션을 해주면 return이 스트링이면 문자열이 화면에 body에 들어간 것처럼 출력된다.
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        //MyLogger Bean은 이거 실행해야 만들어져
        //물론LogDemoController는 스프링 컨테이너 생성 시점에 bean에 등록돼고 관리되는데 얘는 지금 내가 따로 실행 시점을 명시하기 위해서
        //현재는 request 요청 들어왔을 대가 클라이언트가 이 클래스를 실행하고 싶은 때이다.
        //그래서 이걸 실행 메소드 내부에서 생성하도록 만드는 것이다.
        //request scope로 설정되었기 때문에 얘는 http요청이 끝나면 스프링 컨테이너에서 자동으로 소멸되며
        //요청한 클라이언트에 대한 정보를 담고 있다. (uuid)
        myLogger.setRequestURL(requestURL);
        System.out.println("myLogger = " + myLogger.getClass());

        myLogger.log("Controller Test");
        logDemoService.logic("Service Test");
        return "OK";
    }
}
