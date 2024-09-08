package kr.mafoo.photo.controller.dto.response;

import java.util.Collection;
import java.util.function.Function;

public record PageResponse<T>(
        Collection<T> results,
        Integer page,
        Integer size,
        Integer totalElement
) {
    public <R> PageResponse<R> map(Function<T, R> mapper) {
        return new PageResponse<>(
                results.stream().map(mapper).toList(),
                page,
                size,
                totalElement
        );
    }
}
