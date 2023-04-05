package com.eunes.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@Profile("url-rewrite")
public class DynamicRewriteRoute {
    
    private String serviceId;
    private String servicePath;
    private String CONSUL_SERVICE_INFO = "consulService";

    @Bean
    public RouteLocator dynamicZipCodeRoute(RouteLocatorBuilder builder) {
        Builder locator = builder.routes()
            .route("dynamicRewrite", r ->
                r.path("/api/**")
                    .filters(f -> f
                        .filter((exchange, chain) -> {
                            ServerHttpRequest req = exchange.getRequest();
                            addOriginalRequestUrl(exchange, req.getURI());
                            
                            String path = req.getURI().getRawPath();
                            Pattern pattern = Pattern.compile("/api/(?<service>\\w+)(?<path>/.*)");
                            Matcher matcher = pattern.matcher(path);
                            if (matcher.find()) {
                                serviceId = matcher.group("service");
                                servicePath = matcher.group("path");
                            }
                            ServerHttpRequest request = req.mutate().path(servicePath).build();
                            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());

                            Map<String, String> service = new HashMap<>();
                            service.put("id", serviceId);
                            exchange.getAttributes().put(CONSUL_SERVICE_INFO, service);

                            return chain.filter(exchange.mutate().request(request).build());
                        })
                        .changeRequestUri((exchange) -> {
                            HashMap<String, String> service = exchange.getRequiredAttribute(CONSUL_SERVICE_INFO);
                            String serviceUri = String.format("lb://%s", service.get("id"));
                            return Optional.of(URI.create(serviceUri));
                        })
                    )
                    .uri("no://op")
                );
        
        return locator.build();
    }
}
