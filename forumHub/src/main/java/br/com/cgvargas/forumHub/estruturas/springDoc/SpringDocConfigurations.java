package br.com.cgvargas.forumHub.estruturas.springDoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    private static final String BEARER_KEY = "bearer-key";
    private static final String BEARER_SCHEME = "bearer";
    private static final String BEARER_FORMAT = "CGV";
    private static final String TITLE = "Forum Hub";
    private static final String DESCRIPTION = "Projeto Forum Hub";
    private static final String CONTACT_NAME = "developer";
    private static final String CONTACT_EMAIL = "cgvargas@gmail.com";
    private static final String LICENSE_NAME = "MIT";
    private static final String LICENSE_URL = "https://github.com/cgvargas/forumHubChallenge";

    @Bean
    public OpenAPI personalAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(BEARER_KEY,
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme(BEARER_SCHEME).bearerFormat(BEARER_FORMAT)))
                .info(new Info()
                        .title(TITLE)
                        .description(DESCRIPTION)
                        .contact(new Contact()
                                .name(CONTACT_NAME)
                                .email(CONTACT_EMAIL))
                        .license(new License()
                                .name(LICENSE_NAME)
                                .url(LICENSE_URL)));
    }
}
