package kr.mafoo.user.service;

import kr.mafoo.user.domain.MemberEntity;
import kr.mafoo.user.exception.MemberNotFoundException;
import kr.mafoo.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MemberQuery {

    private final MemberRepository memberRepository;

    public Flux<MemberEntity> findAllByNameKeywordAndMemberIdNot(String keyword, String memberId) {
        return memberRepository.findAllByNameContainingAndMemberIdNot(
            convertToSearchableKeyword(keyword),
            memberId
        );
    }

    private String convertToSearchableKeyword(String keyword) {
        return "%" + keyword + "%";
    }

    public Mono<MemberEntity> findById(String memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId)
            .switchIfEmpty(Mono.error(new MemberNotFoundException()));
    }
}
