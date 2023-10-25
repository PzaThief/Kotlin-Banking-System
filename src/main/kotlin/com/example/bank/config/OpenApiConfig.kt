package com.example.bank.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(
    info = Info(title = "Kotlin Bank API 명세서", version = "v1")
)
@Configuration
class OpenApiConfig

