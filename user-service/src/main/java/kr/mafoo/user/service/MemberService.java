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

    private final MemberDataService memberDataService;

    @Transactional
    public Mono<Void> quitMemberByMemberId(String memberId, String token) {
        return socialMemberRepository.softDeleteByMemberId(memberId)
            .then(memberRepository.softDeleteById(memberId))
            .then(memberDataService.deleteMemberData(token));
    }

    @Transactional(readOnly = true)
    public Flux<MemberDetailDto> getMemberByKeywordForSharedAlbum(String keyword, String albumId, String memberId, String token) {
        return memberRepository
            .findAllByNameContainingAndDeletedAtIsNull(keyword)
            .filter(member -> !member.getId().equals(memberId))
            .switchIfEmpty(Mono.empty())
            .concatMap(member -> sharedMemberService.getSharedMemberInfoByAlbumId(albumId, member.getId(), token)
                .flatMap(sharedMemberDto -> Mono.just(MemberDetailDto.fromSharedMember(member, sharedMemberDto)))
            );
    }

    @Transactional(readOnly = true)
    public Mono<MemberEntity> getMemberByMemberId(String memberId) {
        return memberRepository
                .findByIdAndDeletedAtIsNull(memberId)
                .switchIfEmpty(Mono.error(new MemberNotFoundException()));
    }

    @Transactional
    public Mono<MemberEntity> createNewMember(String username, String profileImageUrl, String userAgent) {
        MemberEntity memberEntity = MemberEntity.newMember(IdGenerator.generate(), username, profileImageUrl, true);

        return memberRepository.save(memberEntity)
            .flatMap(savedMember ->
                memberRepository.findByIdAndDeletedAtIsNull(savedMember.getId())
                    .flatMap(fetchedMember ->
                        slackService.sendNewMemberNotification(
                            fetchedMember.getSerialNumber(),
                            fetchedMember.getId(),
                            fetchedMember.getName(),
                            fetchedMember.getProfileImageUrl(),
                            fetchedMember.getCreatedAt().toString(),
                            userAgent
                        )
                    )
                    .then(Mono.just(savedMember))
            );
    }

    @Transactional
    public Mono<MemberEntity> changeName(String memberId, String name) {
        return memberRepository
                .findByIdAndDeletedAtIsNull(memberId)
                .switchIfEmpty(Mono.error(new MemberNotFoundException()))
                .map(member -> {
                    member.setName(name);
                    member.setDefaultName(false);
                    return member;
                })
                .flatMap(memberRepository::save);
    }
}
