package kr.co.pincoin.api.global.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
@ConfigurationPropertiesScan(basePackages = ["kr.co.pincoin.api"])
class AppConfig {
}
