# 클래스 선언 규칙

## 엔티티 클래스

* 불변성을 위해 모든 프로퍼티를 `val`로 선언
    * 단, 상태가 변경되어야 하는 필드는 `var` 사용 (예: 주문 상태, 수정일시 등)
    * JPA의 지연 로딩을 위해서는 var 필요할 수 있음

* `private constructor`와 `companion object`로 빌더 패턴 대체
    * 명명된 파라미터와 기본값으로 빌더의 장점 대체
    * 팩토리 메소드 네이밍: of(), from() 등 상황에 맞게 선택

* Lombok 관련
    * @Getter - 코틀린 프로퍼티로 대체
    * @Builder - 코틀린의 named arguments로 대체
    * @NoArgsConstructor - JPA 플러그인으로 대체
    * @EqualsAndHashCode - data class 또는 직접 구현 (엔티티는 보통 직접 구현 권장)
    * @ToString - 직접 구현 권장 (순환 참조 위험)

* JPA 관련
    * `allOpen` 플러그인 사용 - JPA 프록시를 위해 클래스와 메서드를 open으로 만들어줌
    * ID 필드는 `val id: Type? = null`로 선언 (auto increment 시)
    * 연관관계 매핑 시 nullable 처리 주의 (`Type?`)
    * 지연 로딩(LAZY) 사용 시 관련 필드는 var로 선언 필요할 수 있음

## 도메인 모델

* 불변/가변 구분 명확화
    * 식별자(id), 생성일시 등 변경되지 않는 필드는 `val`로 선언
    * 상태값, 업데이트되는 정보 등은 `var`로 선언 (예: 비밀번호, 로그인시각, 개인정보 등)
    * 도메인 로직에 따른 변경 가능성을 기준으로 결정

* 팩토리 메소드 패턴 활용
    * `private constructor`와 `companion object`로 객체 생성 제어
    * 생성 목적에 맞는 팩토리 메소드 제공 (예: of(), createAdmin() 등)
    * 각 메소드는 해당 도메인의 초기 상태를 보장

* 엔티티 변환
    * toEntity() 메소드로 인프라스트럭처 계층의 엔티티로 변환
    * 도메인 모델과 엔티티 간의 명확한 경계 유지
    * 영속성 관심사와 도메인 로직 분리

* 도메인 로직 응집도
    * 관련된 비즈니스 로직은 도메인 모델 내부에 구현
    * 상태 변경은 도메인 모델의 메소드를 통해서만 가능하도록 캡슐화
    * 유효성 검사 등 도메인 규칙을 도메인 모델에서 강제

* 테스트 용이성
    * 영속성 계층과 분리되어 단위 테스트 용이
    * 도메인 규칙 검증에 집중된 테스트 작성 가능
    * 팩토리 메소드로 인한 테스트 데이터 생성 편의성

## Mapper

* 확장 함수로 구현
* Null Safety 처리
    - Nullable 타입 사용 (`Type?`)
    - Safe call 연산자 활용 (`?.`)
    - Elvis 연산자로 기본값 처리 (`?:`)
* 순환 참조 방지
    - Mapper 로직은 별도 파일에 확장 함수로 구현
    - 도메인/엔티티 클래스 내부에 변환 메서드 정의하지 않음
* Collection 처리
    - `mapNotNull`을 사용하여 null 항목 필터링
    - 빈 컬렉션은 `emptyList()`로 처리
* 단방향 매핑 원칙
    - Entity → Domain Model (조회)
    - Domain Model → Entity (저장)
    - 각 방향별 독립적인 매핑 로직 유지

## 리파지토리

* UserRepository: 최상위 리파지토리 인터페이스
* UserRepositoryImpl: 리파지토리 실질적 구현체
* UserJpaRepository: JPA 활용 메소드
* UserJdbcRepository: JdbcTemplate 기반 메소드
* UserQueryRepository: QueryDSL 기반 메소드
* UserQueryRepositoryImpl

## 비즈니스 로직

### 도메인 모델

* 책임
    * **단일 객체의 책임을 캡슐화**
    * 자신의 상태를 관리하며 값 변경 및 불변성 보장
    * 자신의 컬렉션을 관리 (객체의 추가/삭제 등)
    * 기본적인 비즈니스 규칙 검증(validate 메서드 활용)

### 도메인 수준 서비스

* 역할
    * **여러 도메인 객체 간의 상호작용을 처리**
    * 복잡한 비즈니스 로직을 구현
    * 트랜잭션 일관성을 보장
    * 실제 비즈니스 프로세스를 표현

### 애플리케이션 수준 서비스

* 역할
    * 컨트롤러와 연계하여 서비스를 제공
    * 관리자용과 일반 사용자용 서비스를 구분하고 **메소드 보안** 등을 처리

### 인프라 수준 영속성 서비스

* 역할
    * 영속성 처리를 위한 서비스 계층
    * 여러 개의 Repository를 주입받아 호출
    * 도메인 서비스가 직접 Repository를 사용하지 않도록 중간 계층 역할
