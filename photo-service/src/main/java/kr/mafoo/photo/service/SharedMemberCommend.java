package kr.mafoo.photo.service;

import static kr.mafoo.photo.domain.enums.ShareStatus.PENDING;

import kr.mafoo.photo.domain.enums.PermissionLevel;
import kr.mafoo.photo.domain.SharedMemberEntity;
import kr.mafoo.photo.domain.enums.ShareStatus;
import kr.mafoo.photo.repository.SharedMemberRepository;
import kr.mafoo.photo.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class SharedMemberCommend {

    private final SharedMemberRepository sharedMemberRepository;

    public Mono<SharedMemberEntity> addSharedMember(String albumId, String permissionLevel, String sharingMemberId) {
        return sharedMemberRepository.save(
            SharedMemberEntity.newSharedMember(
                IdGenerator.generate(), PENDING, PermissionLevel.valueOf(permissionLevel), sharingMemberId, albumId
            )
        );
    }

    public Mono<Void> removeSharedMember(SharedMemberEntity sharedMember) {
        return sharedMemberRepository.delete(sharedMember);
    }

    public Mono<SharedMemberEntity> modifySharedMemberShareStatus(SharedMemberEntity sharedMember, String newShareStatus) {
        return sharedMemberRepository.save(sharedMember.updateShareStatus(
            ShareStatus.valueOf(newShareStatus)
        ));
    }

    public Mono<SharedMemberEntity> modifySharedMemberPermissionLevel(SharedMemberEntity sharedMember, String newPermissionLevel) {
        return sharedMemberRepository.save(sharedMember.updatePermissionLevel(
            PermissionLevel.valueOf(newPermissionLevel)
        ));
    }

}
