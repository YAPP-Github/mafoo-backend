package kr.mafoo.photo.service;

import java.util.List;
import kr.mafoo.photo.domain.AlbumEntity;
import kr.mafoo.photo.exception.AlbumNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDataService {

    private final AlbumQuery albumQuery;
    private final AlbumCommand albumCommand;

    private final PhotoCommand photoCommand;

    private final SharedMemberCommand sharedMemberCommand;

    @Transactional
    public Flux<Void> removeMemberData(String requestMemberId) {
        return sharedMemberCommand.removeShareMemberByMemberId(requestMemberId)
            .thenMany(
                albumQuery.findByMemberId(requestMemberId)
                    .onErrorResume(AlbumNotFoundException.class, ex -> Mono.empty())
                    .collectList()
                    .filter(albumList -> !albumList.isEmpty())
                    .flatMapMany(albumList -> {
                        List<String> albumIdList = albumList.stream().map(AlbumEntity::getAlbumId).toList();

                        return Mono.when(
                            sharedMemberCommand.removeShareMemberByAlbumIds(albumIdList),
                            photoCommand.removePhotoByOwnerMemberId(requestMemberId),
                            albumCommand.removeAlbumByOwnerMemberId(requestMemberId)
                        );
                    })
            );
    }
}
