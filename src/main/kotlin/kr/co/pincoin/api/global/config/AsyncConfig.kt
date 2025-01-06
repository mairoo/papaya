package kr.co.pincoin.api.global.config

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor

@Configuration
@EnableAsync
class AsyncConfig : AsyncConfigurer {

    override fun getAsyncExecutor(): Executor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 5  // 기본 스레드 풀 크기
            maxPoolSize = 10  // 최대 스레드 풀 크기
            queueCapacity = 25  // 대기 큐 크기
            setThreadNamePrefix("PincoinAsync-")  // 스레드 이름 접두사

            // 초과 요청에 대한 정책 설정
            setRejectedExecutionHandler(ThreadPoolExecutor.CallerRunsPolicy())

            initialize()
        }
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler {
        return SimpleAsyncUncaughtExceptionHandler()
    }
}