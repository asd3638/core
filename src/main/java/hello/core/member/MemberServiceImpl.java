package hello.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberServiceImpl implements MemberService{
    private MemberRepository memberRepository;

    //필드 주입은 이런 식으로 생성자 생성 안하고 바로 변수 선언할 때 주입하는 방법이다.
    // @Autowired private MemberRepository memberRepository; 이렇게 만든다.
    //근데 이러면 테스트 하거나 할 때 직접 값을 넣어볼 수가 없어 코드 상으로 객체에 접근할 수 있는 방법이 없어서
    //권하지 않는 방식이다. 그냥 생성자 생성해서 객체에 직접 접근할 수 있도록 만들자
    //간단한 테스트 할 때만 사용하자.
    @Autowired
    //마치 ac.getBean(MemberRepository.class) 이거 수행하는 것과 비슷하다.
    //자동으로 MemberRepository의 객체를 찾아야하니까 어디서? 빈에서
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(
            Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(long id) {
        return memberRepository.findById(id);
    }
}
