package com.hisham.HomeCentre.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;

@OpenAPIDefinition(
        info = @Info(
                title = "CozyHomes API",
                description = "Welcome to the CozyHomes API. This API allows you to manage products, categories, user authentication, shopping carts, and orders for the CozyHomes e-commerce platform.",
                version = "1.0.0"
        )
)
@SecuritySchemes({
        @SecurityScheme(
                name = "bearerAuth",
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT",
                description = "JWT token obtained from the /auth/signin endpoint. Include the token in the Authorization header like this: `Authorization: Bearer <token>`"
        )
})
public class OpenAPIConfig {
}
