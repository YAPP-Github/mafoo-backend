package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.SharedMemberEntity;
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

    public Flux<SharedMemberEntity> findAllByAlbumId(String albumId) {
        return sharedMemberRepository.findAllByAlbumId(albumId)
            .switchIfEmpty(Mono.error(new SharedMemberNotFoundException()));
    }

    public Mono<SharedMemberEntity> findBySharedMemberId(String sharedMemberId) {
        return sharedMemberRepository.findById(sharedMemberId)
            .switchIfEmpty(Mono.error(new SharedMemberNotFoundException()));
    }

    public Mono<SharedMemberEntity> findByAlbumIdAndMemberId(String albumId, String memberId) {
        return sharedMemberRepository.findByAlbumIdAndMemberId(albumId, memberId)
            .switchIfEmpty(Mono.error(new SharedMemberNotFoundException()));
    }

}
