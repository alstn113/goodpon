# 굿폰(Goodpon) 모듈 분석 보고서

## 프로젝트 개요
- **프로젝트명**: 굿폰(Goodpon)
- **목적**: B2B 쿠폰/프로모션 발행 및 관리 서비스
- **아키텍처**: 멀티모듈 Spring Boot 애플리케이션 (클린 아키텍처 원칙 적용)
- **기술스택**: Kotlin, Spring Boot, JPA, Redis, AWS SES, JWT, MySQL/H2

## 모듈 구조 및 책임

### 1. API 모듈

#### `modules/api-core`
**목적**: 외부 상점(Merchant)을 위한 코어 API
- **주요 기능**:
  - 쿠폰 발행 (Issue)
  - 쿠폰 사용 (Redeem)  
  - 쿠폰 사용 취소 (Cancel)
- **핵심 컨트롤러**: `CouponController`
- **주요 엔드포인트**:
  - `POST /v1/coupons/issue` - 쿠폰 발행
  - `POST /v1/coupons/{couponId}/redeem` - 쿠폰 사용
  - `POST /v1/coupons/{couponId}/cancel` - 쿠폰 사용 취소
- **인증**: `MerchantPrincipal` 기반 인증
- **의존성**: core, 모든 infra 모듈, logging

#### `modules/api-dashboard`
**목적**: 내부 관리를 위한 대시보드 API
- **주요 기능**:
  - 계정 관리 (가입, 로그인, 인증)
  - 쿠폰 템플릿 관리
  - 상점 관리
- **핵심 컨트롤러**: 
  - `AccountController` - 계정 관리
  - `AuthController` - 인증 관리
  - `CouponTemplateController` - 쿠폰 템플릿 관리
  - `MerchantController` - 상점 관리
- **인증**: `AccountPrincipal` 기반 인증
- **보안 설정**: JWT 필터, 계정 검증 필터 적용
- **의존성**: core, 모든 infra 모듈, logging

### 2. 코어 모듈

#### `modules/core`
**목적**: 비즈니스 로직과 도메인 모델을 포함하는 핵심 모듈
- **주요 도메인**:
  - **Account**: 사용자 계정 관리 및 이메일 인증
    - `Account` - 계정 도메인 모델
    - `AccountEmail`, `AccountName`, `AccountPassword` - 값 객체
    - `AccountService` - 계정 관련 비즈니스 로직
  - **Coupon**: 쿠폰 생명주기 관리
    - `CouponTemplate` - 쿠폰 템플릿 도메인
    - `CouponTemplateStats` - 쿠폰 통계 관리
    - `CouponTemplateService` - 쿠폰 템플릿 비즈니스 로직
    - `CouponIssueService`, `CouponRedeemService` - 쿠폰 발행/사용 로직
  - **Merchant**: 상점 및 상점 계정 관리
    - `Merchant` - 상점 도메인 모델
    - `MerchantAccount` - 상점 계정 관리
    - `MerchantService` - 상점 관련 비즈니스 로직
  - **Auth**: 인증 및 권한 관리
    - `AccountPrincipal`, `MerchantPrincipal` - 인증 주체 모델
- **아키텍처 특징**:
  - Domain-Driven Design (DDD) 적용
  - Repository 패턴 사용
  - Application Service 레이어로 비즈니스 로직 조율
- **의존성**: Spring Context, Jakarta Validation, SLF4J만 사용 (외부 기술 독립성 유지)

### 3. 인프라스트럭처 모듈

#### `modules/infra/jpa`
**목적**: 데이터베이스 액세스 레이어
- **주요 구성요소**:
  - `AccountEntity` - 계정 데이터베이스 엔티티
  - `CouponTemplateEntity` - 쿠폰 템플릿 엔티티
  - `MerchantEntity` - 상점 엔티티
  - `MerchantAccountEntity` - 상점 계정 엔티티
  - `AuditableEntity` - 감사 정보 기본 엔티티
- **기술스택**: Spring Data JPA, Hibernate, MySQL/H2
- **특징**: 도메인-엔티티 매핑, 감사 가능한 엔티티

#### `modules/infra/security`
**목적**: 인증, 권한 및 보안 인프라
- **주요 구성요소**:
  - `JwtAuthenticationFilter` - JWT 인증 필터
  - `AccountVerifiedFilter` - 계정 인증 상태 확인 필터
  - `JwtTokenProvider` - JWT 토큰 생성/검증
  - `PasswordEncoder` - 비밀번호 암호화
- **기술스택**: Spring Security, JWT (jjwt 라이브러리)
- **특징**: 토큰 기반 인증, 역할 기반 접근 제어

#### `modules/infra/aws`
**목적**: AWS 서비스 통합
- **주요 기능**:
  - SES를 이용한 이메일 발송
  - 이메일 템플릿 관리
- **기술스택**: AWS SDK, Thymeleaf (이메일 템플릿)
- **특징**: 인증 이메일 발송, 템플릿 기반 이메일 생성

#### `modules/infra/redis`
**목적**: 캐싱 및 임시 데이터 저장소
- **주요 기능**:
  - 이메일 인증 토큰 저장
  - 임시 데이터 캐싱
- **기술스택**: Spring Data Redis, Jackson (JSON 직렬화)
- **특징**: 만료 시간 기반 데이터 관리

### 4. 지원 모듈

#### `modules/support/logging`
**목적**: 중앙화된 로깅 및 모니터링 설정
- **주요 기능**:
  - 분산 추적 지원
  - 애플리케이션 모니터링
- **기술스택**: Micrometer Tracing, Spring Boot Actuator
- **특징**: 마이크로서비스 환경 추적 지원

## 아키텍처 패턴

### 1. 클린 아키텍처 (Clean Architecture)
- **도메인 레이어**: 비즈니스 로직과 도메인 모델 (`modules/core`)
- **애플리케이션 레이어**: 사용 사례 조율 (`modules/core/application`)
- **인프라스트럭처 레이어**: 외부 기술 연동 (`modules/infra/*`)
- **인터페이스 레이어**: API 컨트롤러 (`modules/api-*`)

### 2. 멀티모듈 설계
- **모듈별 책임 분리**: 각 모듈은 특정 책임을 가짐
- **의존성 관리**: 코어 모듈은 외부 기술에 독립적
- **재사용성**: 공통 기능의 모듈화

### 3. 도메인 주도 설계 (DDD)
- **풍부한 도메인 모델**: 비즈니스 로직을 도메인 객체에 캡슐화
- **값 객체**: 도메인 개념의 명확한 표현
- **리포지토리 패턴**: 데이터 액세스 추상화

## 핵심 비즈니스 기능

### 1. 계정 관리
- **사용자 등록**: 이메일 기반 계정 생성
- **이메일 인증**: AWS SES를 통한 인증 이메일 발송
- **로그인/로그아웃**: JWT 기반 인증

### 2. 상점 관리
- **상점 등록**: 상점 정보 및 비밀키 생성
- **상점 계정**: 상점별 사용자 역할 관리
- **권한 관리**: 상점 소유자/관리자 역할

### 3. 쿠폰 템플릿 관리
- **템플릿 생성**: 쿠폰 유형, 할인 정책, 제한 조건 설정
- **발행 정책**: 최대 발행 수량, 사용 조건 등 관리
- **상태 관리**: 초안, 발행 가능, 만료, 폐기 상태 관리

### 4. 쿠폰 생명주기 관리
- **쿠폰 발행**: 템플릿 기반 쿠폰 생성
- **쿠폰 사용**: 주문 금액에 따른 쿠폰 적용
- **사용 취소**: 쿠폰 사용 취소 및 상태 복원
- **통계 추적**: 발행/사용 현황 실시간 추적

### 5. 보안 및 인증
- **JWT 인증**: 무상태 토큰 기반 인증
- **역할 기반 접근**: 계정별/상점별 권한 관리
- **API 보안**: 요청 검증 및 인증 필터링

## 모듈 간 의존성 관계

```
api-core ─┐
          ├─► core ◄─┐
api-dashboard ─┘     │
                     │
infra/jpa ──────────┐│
infra/security ─────┤│
infra/aws ──────────┤├─► (implements)
infra/redis ────────┤│
                    ││
support/logging ────┘│
```

## 데이터베이스 스키마 구조

### 주요 테이블 구조
1. **accounts**: 사용자 계정 정보
   - id, email, password, name, verified, verified_at
   - 이메일 기반 회원가입 및 인증 처리

2. **merchants**: 상점 정보
   - id, name, secret_key
   - 상점 등록 및 API 인증용 비밀키 관리

3. **merchant_accounts**: 상점-계정 연결
   - id, merchant_id, account_id, role
   - 상점별 사용자 역할 관리 (OWNER 등)

4. **coupon_templates**: 쿠폰 템플릿
   - 할인 정책, 발행 기간, 제한 정책 등 포함
   - 템플릿 상태 관리 (DRAFT, ISSUABLE, EXPIRED, DISCARDED)

5. **coupon_template_stats**: 쿠폰 템플릿 통계
   - issue_count, redeem_count
   - 실시간 쿠폰 발행/사용 현황 추적

6. **user_coupons**: 사용자 쿠폰
   - id, coupon_template_id, user_id, status
   - 개별 쿠폰 생명주기 관리

7. **coupon_histories**: 쿠폰 히스토리
   - 쿠폰 사용 내역 추적

## 핵심 비즈니스 로직 구현

### 1. 쿠폰 발행 플로우 (CouponIssueService)
```kotlin
@Transactional
fun issueCoupon(request: IssueCouponRequest): CouponIssueResult {
    // 1. 통계 정보 락 획득
    val stats = couponTemplateStatsReader.readByCouponTemplateIdForUpdate(request.couponTemplateId)
    
    // 2. 템플릿 유효성 검증
    val couponTemplate = couponTemplateReader.readByIdForRead(request.couponTemplateId)
    couponTemplate.validateIssue(stats.issueCount, LocalDateTime.now())
    
    // 3. 중복 발행 방지
    validateAlreadyIssued(request.userId, request.couponTemplateId)
    
    // 4. 쿠폰 발행 및 통계 업데이트
    val result = couponIssuer.issueCoupon(couponTemplate, request.userId, stats.issueCount, now)
    couponTemplateStatsUpdater.incrementIssueCount(stats)
    
    return result
}
```

### 2. 쿠폰 도메인 모델 (CouponTemplate)
- **풍부한 도메인 모델**: 비즈니스 로직을 도메인 객체에 캡슐화
- **값 객체 사용**: CouponDiscountPolicy, CouponLimitPolicy, CouponPeriod 등
- **유효성 검증**: 발행 조건, 사용 조건 등 비즈니스 규칙 검증
- **불변성**: 데이터 클래스로 불변 객체 구현

### 3. 할인 정책 구현 (CouponDiscountPolicy)
```kotlin
fun calculateDiscountAmount(orderAmount: Int): Int {
    return discountType.calculate(
        orderAmount = orderAmount,
        discountValue = discountValue,
        maxDiscountAmount = maxDiscountAmount
    )
}
```
- **고정 할인**: 특정 금액 할인
- **비율 할인**: 퍼센트 할인 (최대 할인 금액 제한)

### 4. 제한 정책 구현 (CouponLimitPolicy)
- **발행 수량 제한**: 최대 발행 수량 관리
- **사용 수량 제한**: 최대 사용 수량 관리
- **제한 없음**: 무제한 발행/사용

## 인증 및 보안 구현

### 1. JWT 기반 인증
```kotlin
// 토큰 생성
override fun generateAccessToken(accountId: Long): String {
    return Jwts.builder()
        .subject(accountId.toString())
        .issuedAt(now)
        .expiration(expiration)
        .signWith(accessTokenSecretKey)
        .compact()
}
```

### 2. 이중 인증 체계
- **AccountPrincipal**: 대시보드 API 인증 (사용자 계정)
- **MerchantPrincipal**: 코어 API 인증 (상점 비밀키)

### 3. 보안 필터 체인
```kotlin
// JWT 인증 필터 → 계정 인증 필터 → 컨트롤러
.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
.addFilterBefore(accountVerifierFilter, JwtAuthenticationFilter::class.java)
```

## 이메일 서비스 구현

### AWS SES 통합
```kotlin
override fun sendVerificationEmail(name: String, email: String, verificationLink: String) {
    val htmlContent = templateRenderer.render(
        "email-verification",
        mapOf("name" to name, "verificationLink" to verificationLink)
    )
    // SES 이메일 발송
}
```

### 특징
- **템플릿 기반**: Thymeleaf 템플릿 엔진 사용
- **이메일 인증**: 회원가입 시 이메일 인증 필수
- **Redis 토큰 관리**: 인증 토큰 임시 저장

## 결론

굿폰 프로젝트는 현대적인 마이크로서비스 아키텍처 원칙을 따르는 잘 구조화된 B2B 쿠폰 관리 시스템입니다. 각 모듈은 명확한 책임을 가지고 있으며, 클린 아키텍처를 통해 비즈니스 로직과 기술적 구현을 분리하여 유지보수성과 확장성을 높였습니다.

주요 강점:
- **명확한 책임 분리**: 각 모듈의 역할이 명확함
- **기술 독립성**: 코어 모듈은 외부 기술에 독립적
- **확장성**: 새로운 기능 추가 시 모듈 단위로 확장 가능
- **테스트 가능성**: 각 레이어별 단위 테스트 가능
- **운영 편의성**: 별도의 API 서버 운영 가능 (core vs dashboard)
- **동시성 처리**: 쿠폰 발행 시 락을 통한 동시성 제어
- **풍부한 도메인 모델**: 비즈니스 로직의 도메인 객체 캡슐화
- **포괄적인 보안**: JWT 인증, 이메일 인증, 권한 관리