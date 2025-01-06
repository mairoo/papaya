package kr.co.pincoin.api.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.data.web.SortHandlerMethodArgumentResolver
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    // Rest API 서버에서는 별도의 Web MVC 설정을 할 필요 없다.
    // 단, 인터셉터, 메시지 컨버터가 필요할 경우 설정이 필요 - 최소한의 설정
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        val sortResolver = SortHandlerMethodArgumentResolver().apply {
            setFallbackSort(Sort.by(Sort.Order.desc("id")))
        }

        val pageResolver = PageableHandlerMethodArgumentResolver(sortResolver).apply {
            setFallbackPageable(PageRequest.of(0, 20))
        }

        resolvers.add(pageResolver)
    }

    // @Component 애노테이션만 컨버터는 동작 가능
    // 스프링의 타입 변환 시스템에 명시적으로 등록하는 것을 권장
    // WebConfig에서 명시적으로 등록하면 다른 개발자들이 컨버터 존재를 쉽게 파악할 수 있고 관리가 더 쉬움
}