package kr.mafoo.gateway;

import io.micrometer.observation.ObservationPredicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.observation.ServerRequestObservationContext;

@Configuration
public class TracingConfig {
    @Bean
    ObservationPredicate noopServerRequestObservationPredicate() {
        ObservationPredicate  predicate = (name, context) -> {
            if(context instanceof ServerRequestObservationContext c) {
                ServerHttpRequest servletRequest = c.getCarrier();
                String requestURI = servletRequest.getPath().toString();
                if(StringUtils.containsAny(requestURI, "actuator", "swagger", "api-docs")) {
                    return false;
                }
            }
            return true;
        };

        return predicate;
    }
}
