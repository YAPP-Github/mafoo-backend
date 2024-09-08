package kr.mafoo.admin.controller

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/v1/users")
class AdminController(
    @Qualifier("photoServiceWebClient") private val photoWebClient: WebClient,
) {
    @PostMapping("/upload-image", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun uploadImage(
        memberId: String,
        @RequestPart("files") files: Flux<FilePart>
    ) {
        photoWebClient
            .post()
            .uri("/v1/photos/files")
            .header("X-MEMBER-ID", memberId)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .bodyValue(files)
            .retrieve()

    }
}
