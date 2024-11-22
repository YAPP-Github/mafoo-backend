package kr.mafoo.user.service;

import kr.mafoo.user.domain.MemberEntity;
import kr.mafoo.user.exception.MemberNotFoundException;
import kr.mafoo.user.repository.MemberRepository;
import kr.mafoo.user.repository.SocialMemberRepository;
import kr.mafoo.user.service.dto.MemberDetailDto;
import kr.mafoo.user.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final SocialMemberRepository socialMemberRepository;
    private final SlackService slackService;
    private final SharedMemberService sharedMemberService;

    @Transactional
    public Mono<Void> quitMemberByMemberId(String memberId) {
        return socialMemberRepository
                .deleteSocialMemberByMemberId(memberId)
                .then(memberRepository.deleteMemberById(memberId));
    }

    public Flux<MemberDetailDto> getMemberByKeywordForSharedAlbum(String keyword, String albumId, String token) {
        return memberRepository
            .findAllByNameContaining(keyword)
            .switchIfEmpty(Mono.error(new MemberNotFoundException()))
            .concatMap(member -> sharedMemberService.getSharedMemberInfoByAlbumId(albumId, member.getId(), token)
                .flatMap(sharedMemberDto -> Mono.just(MemberDetailDto.fromSharedMember(member, sharedMemberDto)))
            );
    }

    public Mono<MemberEntity> getMemberByMemberId(String memberId) {
        return memberRepository
                .findById(memberId)
                .switchIfEmpty(Mono.error(new MemberNotFoundException()));
    }

    @Transactional
    public Mono<MemberEntity> createNewMember(String username, String profileImageUrl, String userAgent) {
        MemberEntity memberEntity = MemberEntity.newMember(IdGenerator.generate(), username, profileImageUrl);

        return memberRepository.save(memberEntity)
                .flatMap(savedMember ->
                        slackService.sendNewMemberNotification(
                                memberEntity.getId(),
                                memberEntity.getName(),
                                memberEntity.getProfileImageUrl(),
                                memberEntity.getCreatedAt().toString(),
                                userAgent
                        )
                        .then(Mono.just(savedMember))
                );
    }
}
