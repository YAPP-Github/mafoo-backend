package kr.mafoo.photo.service;

import static kr.mafoo.photo.domain.enums.PermissionLevel.VIEW_ACCESS;

import java.util.Comparator;
import kr.mafoo.photo.service.dto.SharedAlbumDto;
import kr.mafoo.photo.service.dto.SharedMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class SharedAlbumService {

    private final SharedMemberQuery sharedMemberQuery;

    private final AlbumPermissionVerifier albumPermissionVerifier;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public Mono<SharedAlbumDto> findSharedAlbumOwnerAndMemberList(String albumId, String requestMemberId, String token) {
        return albumPermissionVerifier.verifyOwnershipOrAccessPermission(albumId, requestMemberId, VIEW_ACCESS)
            .flatMap(album -> memberService.getMemberInfoById(album.getOwnerMemberId(), token)
                    .flatMap(ownerMember -> Mono.just(SharedAlbumDto.fromSharedAlbum(
                        album,
                        ownerMember,
                        findSharedAlbumMemberDetail(albumId, token)
                    )))
            );
    }

    private Flux<SharedMemberDto> findSharedAlbumMemberDetail(String albumId, String token) {
        return sharedMemberQuery.findAllByAlbumIdWhereStatusNotRejected(albumId)
            .concatMap(sharedMember -> memberService.getMemberInfoById(sharedMember.getMemberId(), token)
                .flatMap(memberInfo -> Mono.just(SharedMemberDto.fromSharedMember(sharedMember, memberInfo)))
            ).sort(Comparator.comparing(SharedMemberDto::shareStatus));
    }
}

