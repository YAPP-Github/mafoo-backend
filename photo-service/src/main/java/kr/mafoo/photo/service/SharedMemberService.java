package kr.mafoo.photo.service;

import static kr.mafoo.photo.domain.enums.PermissionLevel.FULL_ACCESS;
import static kr.mafoo.photo.domain.enums.PermissionLevel.VIEW_ACCESS;

import kr.mafoo.photo.domain.SharedMemberEntity;
import kr.mafoo.photo.exception.SharedMemberDuplicatedException;
import kr.mafoo.photo.service.dto.SharedMemberDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class SharedMemberService {

    private final SharedMemberQuery sharedMemberQuery;
    private final SharedMemberCommand sharedMemberCommand;

    private final AlbumPermissionQuery albumPermissionQuery;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public Flux<SharedMemberDetailDto> findSharedMemberDetailListByAlbumId(String albumId, String requestMemberId, String token) {
        return albumPermissionQuery.verifyOwnershipOrAccessPermission(albumId, requestMemberId, VIEW_ACCESS)
            .thenMany(sharedMemberQuery.findAllByAlbumId(albumId))
            .flatMap(sharedMemberEntity -> memberService.getMemberInfoById(sharedMemberEntity.getMemberId(), token)
                .map(memberDto -> SharedMemberDetailDto.from(sharedMemberEntity, memberDto))
            );
    }

    @Transactional
    public Mono<SharedMemberEntity> addSharedMember(String albumId, String permissionLevel, String sharingMemberId, String requestMemberId) {
        return albumPermissionQuery.verifyOwnershipOrAccessPermission(albumId, requestMemberId, FULL_ACCESS)
            .then(sharedMemberQuery.findByAlbumIdAndMemberId(albumId, sharingMemberId)
                .switchIfEmpty(
                    sharedMemberCommand.addSharedMember(albumId, permissionLevel, sharingMemberId)
                )
                .flatMap(existingMember -> Mono.error(new SharedMemberDuplicatedException()))
            );
    }

    @Transactional
    public Mono<Void> removeSharedMember(String sharedMemberId, String requestMemberId) {
        return sharedMemberQuery.findBySharedMemberId(sharedMemberId)
            .flatMap(sharedMember -> {
                if (sharedMember.getMemberId().equals(requestMemberId)) {
                    return sharedMemberCommand.removeSharedMember(sharedMember);
                } else {
                    return albumPermissionQuery.verifyOwnership(sharedMember.getAlbumId(), requestMemberId)
                        .then(sharedMemberCommand.removeSharedMember(sharedMember));
                }
            });
    }

    @Transactional
    public Mono<SharedMemberEntity> modifySharedMemberShareStatus(String sharedMemberId, String newShareStatus, String requestMemberId) {
        return sharedMemberQuery.findBySharedMemberId(sharedMemberId)
            .flatMap(sharedMember -> albumPermissionQuery.verifyOwnership(sharedMember.getAlbumId(), requestMemberId)
                .then(sharedMemberCommand.modifySharedMemberShareStatus(sharedMember, newShareStatus))
            );
    }

    @Transactional
    public Mono<SharedMemberEntity> modifySharedMemberPermissionLevel(String sharedMemberId, String newPermissionLevel, String requestMemberId) {
        return sharedMemberQuery.findBySharedMemberId(sharedMemberId)
            .flatMap(sharedMember -> albumPermissionQuery.verifyOwnership(sharedMember.getAlbumId(), requestMemberId)
                .then(sharedMemberCommand.modifySharedMemberPermissionLevel(sharedMember, newPermissionLevel))
            );
    }

}
