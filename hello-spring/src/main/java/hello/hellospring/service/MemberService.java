package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService {
    //command shift T 로 테스트 만들 수 있음.
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /*
            회원가입
            같은 이름이면 회원가입이 안된다.
         */
    public Long join(Member member){


        /* version 1 : Optional 에 넣어서 ifPresent를 사용.

        Optional<Member> result = memberRepository.findByName(member.getName());

        //ifPresent는 여기에 값이 있으면이라는 뜻 Optional이기때문에 가능 컨트롤 T로 함수 만들기
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });

         */
        //version 2:        command option v 하면 optional 감싸줌
        validateDuplicateMember(member);        //중복회원검증



        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                        .ifPresent(m ->{
                            throw  new IllegalStateException("이미 존재하는 회원입니다.");
                        });
    }

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }
}
