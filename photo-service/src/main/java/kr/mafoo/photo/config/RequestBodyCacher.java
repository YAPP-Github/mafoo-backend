package kr.mafoo.photo.config;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class RequestBodyCacher implements WebFilter {

    private static final byte[] EMPTY_BYTES = new byte[0];

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        return DataBufferUtils
            .join(exchange.getRequest().getBody())
            .map(databuffer -> {

                final byte[] bytes = new byte[databuffer.readableByteCount()];

                DataBufferUtils.release(databuffer.read(bytes));

                return bytes;
            })
            .defaultIfEmpty(EMPTY_BYTES)
            .flatMap(bytes -> {

                final RequestBodyDecorator decorator = new RequestBodyDecorator(exchange, bytes);

                return chain.filter(exchange.mutate().request(decorator).build());
            });
    }
}

class RequestBodyDecorator extends ServerHttpRequestDecorator {

    private final byte[] bytes;
    private final ServerWebExchange exchange;

    public RequestBodyDecorator(ServerWebExchange exchange, byte[] bytes) {

        super(exchange.getRequest());
        this.bytes = bytes;
        this.exchange = exchange;
    }

    @Override
    public Flux<DataBuffer> getBody() {

        return bytes==null||bytes.length==0?
            Flux.empty(): Flux.just(exchange.getResponse().bufferFactory().wrap(bytes));
    }
}