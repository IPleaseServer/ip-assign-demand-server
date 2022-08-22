package site.iplease.iadserver.domain.query.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("iplease.query")
data class DataQueryProperty(
    val all: QueryProperty,
    val byIssuer: QueryProperty,
)

data class QueryProperty(
    val pageSize: Int
)