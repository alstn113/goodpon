# 굿폰 모듈 의존성 다이어그램

## 모듈 계층 구조

```
┌─────────────────────────────────────────────────────────────┐
│                    API Layer (Interface)                    │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────────┐    ┌─────────────────────────────────┐ │
│  │   api-core      │    │         api-dashboard           │ │
│  │                 │    │                                 │ │
│  │ • CouponController│    │ • AccountController             │ │
│  │ • MerchantAuth   │    │ • AuthController                │ │
│  │ • External API   │    │ • CouponTemplateController      │ │
│  │                 │    │ • MerchantController            │ │
│  │                 │    │ • AccountAuth                   │ │
│  │                 │    │ • Internal Management           │ │
│  └─────────────────┘    └─────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────┐
│                 Application & Domain Layer                  │
├─────────────────────────────────────────────────────────────┤
│                        ┌─────────────┐                     │
│                        │    core     │                     │
│                        │             │                     │
│                        │ Application │                     │
│                        │ • Services  │                     │
│                        │ • Readers   │                     │
│                        │ • Appenders │                     │
│                        │             │                     │
│                        │ Domain      │                     │
│                        │ • Entities  │                     │
│                        │ • VOs       │                     │
│                        │ • Repos     │                     │
│                        │ • Factories │                     │
│                        └─────────────┘                     │
└─────────────────────────────────────────────────────────────┘
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────┐
│                 Infrastructure Layer                        │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │ infra/jpa   │  │infra/security│  │  infra/aws  │        │
│  │             │  │             │  │             │        │
│  │• Entities   │  │• JWT        │  │• SES        │        │
│  │• Repos      │  │• Filters    │  │• Templates  │        │
│  │• JPA Config │  │• Encoders   │  │• Email      │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
│                                                             │
│  ┌─────────────┐  ┌─────────────┐                         │
│  │infra/redis  │  │support/     │                         │
│  │             │  │logging      │                         │
│  │• Redis      │  │             │                         │
│  │• Cache      │  │• Tracing    │                         │
│  │• Tokens     │  │• Actuator   │                         │
│  └─────────────┘  └─────────────┘                         │
└─────────────────────────────────────────────────────────────┘
```

## 의존성 관계 세부 분석

### API 모듈 의존성

**api-core** 의존성:
- **직접 의존**: core, infra/* (jpa, security, aws, redis), support/logging
- **역할**: 외부 상점이 사용하는 쿠폰 관련 API
- **인증**: MerchantPrincipal (상점 비밀키 기반)

**api-dashboard** 의존성:
- **직접 의존**: core, infra/* (jpa, security, aws, redis), support/logging
- **역할**: 내부 관리자가 사용하는 관리 API
- **인증**: AccountPrincipal (사용자 계정 기반)

### 코어 모듈 의존성

**core** 의존성:
- **직접 의존**: Spring Context, Jakarta Validation, SLF4J
- **특징**: 외부 기술에 독립적, 순수 비즈니스 로직
- **역할**: 도메인 모델, 애플리케이션 서비스, 리포지토리 인터페이스

### 인프라 모듈 의존성

**infra/jpa** 의존성:
- **직접 의존**: core (compileOnly), Spring Data JPA, Hibernate
- **역할**: 데이터베이스 액세스 구현체

**infra/security** 의존성:
- **직접 의존**: core, Spring Security, JWT
- **역할**: 인증/권한 구현체

**infra/aws** 의존성:
- **직접 의존**: core, AWS SDK, Thymeleaf
- **역할**: AWS 서비스 통합

**infra/redis** 의존성:
- **직접 의존**: core, Spring Data Redis, Jackson
- **역할**: 캐시 및 임시 저장소

**support/logging** 의존성:
- **직접 의존**: Micrometer, Spring Boot Actuator
- **역할**: 로깅 및 모니터링

## 계층별 통신 패턴

### 1. 요청 처리 흐름
```
HTTP Request → Controller → Service → Repository Interface
                                           ↓
                                    Repository Implementation
                                           ↓
                                       Database
```

### 2. 의존성 역전 (Dependency Inversion)
- **코어 모듈**: 인터페이스 정의 (Repository, Service 등)
- **인프라 모듈**: 구현체 제공 (JPA Repository, SES EmailSender 등)
- **API 모듈**: 의존성 주입을 통한 구현체 사용

### 3. 패키지 구조
```
com.goodpon
├── api
│   ├── core        # 외부 API
│   └── dashboard   # 내부 관리 API
├── core
│   ├── application # 애플리케이션 서비스
│   ├── domain      # 도메인 모델
│   └── support     # 공통 기능
├── infra
│   ├── jpa         # 데이터베이스 연동
│   ├── security    # 보안 구현
│   ├── aws         # AWS 서비스
│   └── redis       # 레디스 연동
└── support
    └── logging     # 로깅/모니터링
```

이러한 구조는 **헥사고날 아키텍처(Hexagonal Architecture)**의 원칙을 따르며, 비즈니스 로직을 외부 기술로부터 격리시켜 테스트 가능성과 유지보수성을 높입니다.