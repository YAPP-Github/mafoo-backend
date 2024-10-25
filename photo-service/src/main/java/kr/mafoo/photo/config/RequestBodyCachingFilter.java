package kr.mafoo.photo.config;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class RequestBodyCachingFilter implements WebFilter {

    // TODO: 추후 정리 필요

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);

                    ServerHttpRequestDecorator decoratedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                        @Override
                        public Flux<DataBuffer> getBody() {
                            return Flux.just(exchange.getResponse().bufferFactory().wrap(bytes));
                        }
                    };

                    ServerWebExchangeDecorator decoratedExchange = new ServerWebExchangeDecorator(exchange) {
                        @Override
                        public ServerHttpRequest getRequest() {
                            return decoratedRequest;
                        }
                    };

                    return chain.filter(decoratedExchange);
                });
    }
}

