package kr.mafoo.photo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;

@RequiredArgsConstructor
@Service
public class LocalFileService {

    public Mono<Void> deleteSimilarNameFileForPath(String path, String name) {
        return Mono.fromCallable(() -> new File(path).listFiles())
                .flatMapMany(files -> {
                    if (files == null) {
                        throw new RuntimeException("Failed to retrieve file list from the specified path: " + path);
                    }
                    return Flux.fromArray(files);
                })
                .filter(file -> file.getName().contains(name) && file.isFile())
                .flatMap(file -> Mono.fromCallable(() -> {
                                    boolean deleted = file.delete();
                                    if (!deleted) {
                                        throw new RuntimeException("Failed to delete file: " + file.getAbsolutePath());
                                    }
                                    return deleted;
                                })
                                .subscribeOn(Schedulers.boundedElastic())
                                .onErrorResume(e -> Mono.empty())
                                .then()
                )
                .then()
                .onErrorResume(e -> {
                    throw new RuntimeException("Error occurred while processing files in the directory: " + path, e);
                });
    }
}
