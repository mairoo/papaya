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
    const val NETTY_VERSION = "4.1.116.Final"
    const val QUERYDSL_VERSION = "5.1.0"
    const val JJWT_VERSION = "0.12.6"
    const val DANAL_VERSION = "1.6.2"
}

// OS 및 아키텍처 관련 상수
object Platform {
    const val ARCH_ARM64 = "aarch64"
    const val OS_MACOS = "mac"

    val isMacOS = System.getProperty("os.name").lowercase().contains(OS_MACOS)
    val isArm64 = System.getProperty("os.arch").lowercase().contains(ARCH_ARM64)
}

object Dependencies {
    object QueryDsl {
        const val GROUP = "com.querydsl"
        const val JAKARTA_CLASSIFIER = "jakarta"

        object Artifacts {
            const val JPA = "querydsl-jpa"
            const val APT = "querydsl-apt"
        }
    }

    object Jakarta {
        const val GROUP = "jakarta"

        object Artifacts {
            const val ANNOTATION = "jakarta.annotation-api"
            const val PERSISTENCE = "jakarta.persistence-api"
        }
    }

    object Jwt {
        const val GROUP = "io.jsonwebtoken"

        object Artifacts {
            const val API = "jjwt-api"
            const val IMPL = "jjwt-impl"
            const val JACKSON = "jjwt-jackson"
        }
    }

    object Netty {
        const val GROUP = "io.netty"

        object Artifacts {
            const val DNS_RESOLVER_MACOS = "netty-resolver-dns-native-macos"
        }

        object Classifier {
            const val ARM64 = "osx-aarch_64"
            const val X86 = "osx-x86_64"
        }
    }

    object Local {
        object Artifacts {
            const val DANAL = "jsinbi-${Versions.DANAL_VERSION}.jar"
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // QueryDSL
    implementation("${Dependencies.QueryDsl.GROUP}:${Dependencies.QueryDsl.Artifacts.JPA}:${Versions.QUERYDSL_VERSION}:${Dependencies.QueryDsl.JAKARTA_CLASSIFIER}")
    annotationProcessor("${Dependencies.QueryDsl.GROUP}:${Dependencies.QueryDsl.Artifacts.APT}:${Versions.QUERYDSL_VERSION}:${Dependencies.QueryDsl.JAKARTA_CLASSIFIER}")
    annotationProcessor("${Dependencies.Jakarta.GROUP}.annotation:${Dependencies.Jakarta.Artifacts.ANNOTATION}")
    annotationProcessor("${Dependencies.Jakarta.GROUP}.persistence:${Dependencies.Jakarta.Artifacts.PERSISTENCE}")

    // JWT
    implementation("${Dependencies.Jwt.GROUP}:${Dependencies.Jwt.Artifacts.API}:${Versions.JJWT_VERSION}")
    runtimeOnly("${Dependencies.Jwt.GROUP}:${Dependencies.Jwt.Artifacts.IMPL}:${Versions.JJWT_VERSION}")
    runtimeOnly("${Dependencies.Jwt.GROUP}:${Dependencies.Jwt.Artifacts.JACKSON}:${Versions.JJWT_VERSION}")

    // Netty DNS resolver for Mac
    if (Platform.isMacOS) {
        val classifier = if (Platform.isArm64) Dependencies.Netty.Classifier.ARM64 else Dependencies.Netty.Classifier.X86
        runtimeOnly("${Dependencies.Netty.GROUP}:${Dependencies.Netty.Artifacts.DNS_RESOLVER_MACOS}:${Versions.NETTY_VERSION}:${classifier}")
    }

    // 다날 휴대폰 인증
    implementation(files("libs/${Dependencies.Local.Artifacts.DANAL}"))
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
