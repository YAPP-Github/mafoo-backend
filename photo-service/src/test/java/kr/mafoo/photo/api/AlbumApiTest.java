package kr.mafoo.photo.api;

import kr.mafoo.photo.controller.dto.request.AlbumCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class AlbumApiTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("앨범 생성 API는 생성 결과를 리턴한다")
    public void createAlbum_shouldReturnResult() {
        // given
        String albumName = "example_album_name";
        String albumType = "HEART";
        AlbumCreateRequest request = new AlbumCreateRequest(albumName, albumType);

        //when
        webTestClient.post()
                .uri("/v1/albums")
                .bodyValue(request)
                .header("X-MEMBER-ID", "TESTMEMBERID12345678912345")
                .exchange()
        //then
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$.name").isEqualTo(albumName)
                    .jsonPath("$.type").isEqualTo(albumType);
    }
}
