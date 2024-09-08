package kr.mafoo.admin.controller

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.http.codec.multipart.FilePart
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/v1/users")
class AdminController(
    @Qualifier("photoServiceWebClient") private val photoWebClient: WebClient,
) {
    companion object {
        const val MEMBER_ID_HEADER_KEY = "X-MEMBER-ID"
    }

    @PostMapping("/upload-image-with-new-album",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun uploadImage(
        @RequestPart("memberId") memberId: String,
        @RequestPart("files") files: Flux<FilePart>,
        @RequestPart("albumName") albumName: String,
    ): Flux<LinkedHashMap<*, *>> {
        // #1. upload photos
        val multipartBuilder = MultipartBodyBuilder()
        files.collectList().awaitSingle().forEach {
            multipartBuilder
                .asyncPart("files", it.content(), DataBuffer::class.java)
                .filename(it.filename())
        }

        val uploadedPhotos = photoWebClient
            .post()
            .uri("/v1/photos/files")
            .header(MEMBER_ID_HEADER_KEY, memberId)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData(multipartBuilder.build()))
            .retrieve()
            .bodyToFlux(LinkedHashMap::class.java)
            .collectList()
            .awaitSingle()

        // #2. create album
        val newAlbum = photoWebClient
            .post()
            .uri("/v1/albums")
            .header(MEMBER_ID_HEADER_KEY, memberId)
            .bodyValue(mapOf("name" to albumName, "type" to "HEART"))
            .retrieve()
            .bodyToMono(LinkedHashMap::class.java)
            .awaitSingle()

        val albumId = newAlbum["albumId"] ?: throw RuntimeException()

        // #3. bulk update album ids
        val photoIds = uploadedPhotos.map { it["photoId"] ?: throw RuntimeException() }
        return photoWebClient
            .patch()
            .uri("/v1/photos/bulk/album")
            .header(MEMBER_ID_HEADER_KEY, memberId)
            .bodyValue(mapOf("albumId" to albumId, "photoIds" to photoIds))
            .retrieve()
            .bodyToFlux(LinkedHashMap::class.java)
    }
}
