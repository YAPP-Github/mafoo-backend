package kr.mafoo.photo.service;

import static kr.mafoo.photo.domain.enums.ShareStatus.ACCEPTED;
import static kr.mafoo.photo.domain.enums.ShareStatus.REJECTED;

import java.util.List;
import kr.mafoo.photo.domain.SharedMemberEntity;
import kr.mafoo.photo.exception.SharedMemberDuplicatedException;
import kr.mafoo.photo.exception.SharedMemberNotFoundException;
import kr.mafoo.photo.repository.SharedMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class SharedMemberQuery {

    private final SharedMemberRepository sharedMemberRepository;

    public Flux<SharedMemberEntity> findAllByAlbumIdList(List<String> albumIdList) {
        return sharedMemberRepository.findAllByAlbumIdList(albumIdList)
            .switchIfEmpty(Mono.error(new SharedMemberNotFoundException()));
    }

    public Flux<SharedMemberEntity> findAllByAlbumIdListAndMemberIdNot(List<String> albumIdList, String memberId) {
        return sharedMemberRepository.findAllByAlbumIdListAndMemberIdNot(albumIdList, memberId)
            .switchIfEmpty(Mono.error(new SharedMemberNotFoundException()));
    }

    public Flux<SharedMemberEntity> findByMemberId(String memberId) {
        return sharedMemberRepository.findByMemberId(memberId)
            .switchIfEmpty(Mono.error(new SharedMemberNotFoundException()));
    }

    public Flux<SharedMemberEntity> findAllByAlbumIdWhereStatusNotRejected(String albumId) {
        return sharedMemberRepository.findAllByAlbumIdAndShareStatusNot(albumId, REJECTED)
            .switchIfEmpty(Mono.error(new SharedMemberNotFoundException()));
    }

    public Flux<SharedMemberEntity> findAllByMemberIdWhereStatusNotRejected(String memberId) {
        return sharedMemberRepository.findAllByMemberIdAndShareStatusNot(memberId, REJECTED)
            .switchIfEmpty(Mono.error(new SharedMemberNotFoundException()));
    }

    public Mono<SharedMemberEntity> findBySharedMemberId(String sharedMemberId) {
        return sharedMemberRepository.findById(sharedMemberId)
            .switchIfEmpty(Mono.error(new SharedMemberNotFoundException()));
    }

    public Mono<SharedMemberEntity> findByAlbumIdAndMemberIdWhereStatusAccepted(String albumId, String memberId) {
        return sharedMemberRepository.findByAlbumIdAndMemberIdAndShareStatus(albumId, memberId, ACCEPTED)
            .switchIfEmpty(Mono.error(new SharedMemberNotFoundException()));
    }

    public Mono<SharedMemberEntity> findByAlbumIdAndMemberId(String albumId, String memberId) {
        return sharedMemberRepository.findByAlbumIdAndMemberId(albumId, memberId)
            .switchIfEmpty(Mono.error(new SharedMemberNotFoundException()));
    }

    public Mono<Void> checkDuplicateByAlbumIdAndMemberId(String albumId, String memberId) {
        return sharedMemberRepository.findByAlbumIdAndMemberId(albumId, memberId)
            .switchIfEmpty(Mono.empty())
            .flatMap(existingMember -> Mono.error(new SharedMemberDuplicatedException()));
    }

}
