package kr.mafoo.photo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("마푸 백엔드 API 명세 - 사진/앨범 서비스")
                        .version("v1")
                        .description("사진/앨범 관련 API 명세 목록입니다.")
                        .contact(new Contact()
                                .name("깃허브 주소")
                                .url("https://github.com/YAPP-Github/mafoo-backend"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                )
                .addServersItem(new Server()
                        .url("https://gateway.mafoo.kr/photo/")
                        .description("프로덕션 서버 사진/앨범 서비스 URL")
                )
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .components(
                        new Components()
                                .addSecuritySchemes("Authorization",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("Bearer")
                                                .bearerFormat("JWT"))
                                );
    }
}
