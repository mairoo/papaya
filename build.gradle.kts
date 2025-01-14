plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
}

group = "kr.co.pincoin"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

// 버전 상수
object Versions {
    const val KOTLIN_LOGGING_VERSION = "7.0.3"
    const val QUERYDSL_VERSION = "5.1.0"
    const val JJWT_VERSION = "0.12.6"
    const val SPRINGDOC_OPENAPI_VERSION = "2.8.1"
    const val NETTY_VERSION = "4.1.116.Final"
    const val DANAL_VERSION = "1.6.2"
}

// OS 및 아키텍처 관련 상수
object Platform {
    val nettyClassifier: String? = when {
        System.getProperty("os.name").lowercase().contains("mac") ->
            if (System.getProperty("os.arch").lowercase().contains("aarch64")) "osx-aarch_64"
            else "osx-x86_64"

        else -> null
    }
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // Development
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Database
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

    // Annotation Processing
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Logging
    implementation("io.github.oshai:kotlin-logging-jvm:${Versions.KOTLIN_LOGGING_VERSION}")

    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:${Versions.QUERYDSL_VERSION}:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:${Versions.QUERYDSL_VERSION}:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:${Versions.JJWT_VERSION}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${Versions.JJWT_VERSION}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${Versions.JJWT_VERSION}")

    // SpringDoc OpenAPI Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${Versions.SPRINGDOC_OPENAPI_VERSION}")

    // Netty DNS resolver for Mac
    Platform.nettyClassifier?.let {
        runtimeOnly("io.netty:netty-resolver-dns-native-macos:${Versions.NETTY_VERSION}:${it}")
    }

    // 다날 휴대폰 인증
    implementation(files("libs/jsinbi-${Versions.DANAL_VERSION}.jar"))
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}