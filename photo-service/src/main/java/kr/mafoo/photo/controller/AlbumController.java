package kr.mafoo.photo.controller;

import kr.mafoo.photo.api.AlbumApi;
import kr.mafoo.photo.controller.dto.request.AlbumCreateRequest;
import kr.mafoo.photo.controller.dto.request.AlbumUpdateRequest;
import kr.mafoo.photo.controller.dto.response.AlbumResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static kr.mafoo.photo.domain.AlbumType.*;

@RestController
public class AlbumController implements AlbumApi {

    @Override
    public Flux<AlbumResponse> getAlbums(
    ) {
        return Flux.just(
                new AlbumResponse("test_album_id_a", "단짝이랑", TYPE_A, "1"),
                new AlbumResponse("test_album_id_b", "야뿌들", TYPE_B, "5"),
                new AlbumResponse("test_album_id_c", "농구팟", TYPE_C, "2"),
                new AlbumResponse("test_album_id_d", "화사사람들", TYPE_D, "12"),
                new AlbumResponse("test_album_id_e", "기념일", TYPE_E, "4"),
                new AlbumResponse("test_album_id_f", "친구들이랑", TYPE_F, "9")
        );
    }

    @Override
    public Mono<AlbumResponse> createAlbum(
            AlbumCreateRequest request
    ){
        return Mono.just(
                new AlbumResponse("test_album_id", "시금치파슷하", TYPE_A, "0")
        );
    }

    @Override
    public Mono<AlbumResponse> updateAlbum(
            String albumId,
            AlbumUpdateRequest request
    ){
        return Mono.just(
                new AlbumResponse("test_album_id", "시금치파슷하", TYPE_A, "0")
        );
    }

    @Override
    public Mono<Void> deleteAlbum(
            String albumId
    ){
        return Mono.empty();
    }

}
