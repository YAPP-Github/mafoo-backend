package kr.mafoo.photo.service.vendors;

import reactor.core.publisher.Mono;

public interface QrVendor {
    Mono<byte[]> extractImageFromQrUrl(String qrUrl);
}
