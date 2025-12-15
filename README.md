# ⭐️ 굿폰(Goodpon), 쿠폰/프로모션 발행 및 관리 B2B 서비스

## 목차

- [프로젝트 소개](#프로젝트-소개)
- [기술 스택](#기술-스택)
- [프로젝트 아키텍처](#프로젝트-아키텍처)
- [프로젝트 API 명세서](#프로젝트-api-명세서)
  - [Dashboard API 명세서](#dashboard-api-명세서)
  - [Partner OpenAPI 명세서](#partner-openapi-명세서)
- [모니터링 시스템 구조](#모니터링-시스템-구조)
- [서비스 핵심 흐름도](#서비스-핵심-흐름도)

## 프로젝트 소개

굿폰(Goodpon)은 고객사가 자사 고객에게 쿠폰 및 프로모션을 발행하고 관리할 수 있도록 돕는 B2B 서비스입니다. 이 시스템은 크게 두 가지 주요 부분으로 구성됩니다.

`Dashboard`는 고객사 관리자가 계정을 생성하고, 상점을 관리하며, 다양한 쿠폰 템플릿을 직접 생성하고 발행할 수 있는 관리자 시스템입니다. 또한, 사용자 또는 주문별 쿠폰 내역들을 조회하여 효율적으로 관리할
수 있습니다.

`Partner OpenAPI`는 고객사의 커머스 서비스와 연동되어 최종 사용자가 쿠폰을 발급받고, 보유 쿠폰을 조회하며, 주문/결제 시 쿠폰을 사용하여 할인 혜택을 적용받을 수 있도록 지원합니다. 이를 통해
고객사는 자체 개발 없이도 손쉽게 쿠폰 기능을 서비스에 통합할 수 있습니다.

## 기술 스택

- Kotlin
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Security
- MySQL 8.4
- Valkey 8
- Apache Kafka 3.5
- Oracle Cloud Infrastructure (OCI)
- Testcontainers

## 프로젝트 아키텍처

<img width="1923" height="1436" alt="프로젝트 아키텍처" src="https://github.com/user-attachments/assets/c8310ccd-916c-4ed7-83e8-5da84c5b2f05" />

굿폰 프로젝트는 단일 도메인을 공유하되, 사용자·트래픽·인증 특성에 따라 실행 단위를 분리한 멀티 모듈 헥사고날 아키텍처 프로젝트입니다.
- 서비스별 물리적 격리: 고객사 관리자용 `Dashboard`, 고객사 커머스 서비스 연동용 `Partner`, 대량 쿠폰 발급 처리용 `Coupon Issuer`로 시스템을 분리하여 각 특성에 맞는 독립적인 배포와 확장을 보장합니다.
- 도메인 보호: Adapter ➔ Application ➔ Domain으로 향하는 단방향 의존성 규칙을 준수합니다. 특히 Bootstrap 모듈을 통해 런타임에 인프라를 주입함으로써, 컴파일 타임에는 도메인이 기술 구현체에 전혀 의존하지 않도록 했습니다.
  
---

<img width="1714" height="697" alt="선착순 쿠폰 발급 아키텍처" src="https://github.com/user-attachments/assets/99db8470-7693-48f2-b184-ff163a6542ec" />

선착순 쿠폰 발급 기능은 **검증 로직과 발급 로직을 분리하여 Kafka로 비동기 처리**함으로써 처리량과 응답 속도를 개선했습니다.
- 검증 로직: Redis 집합(Set) 자료구조와 Lua Script를 활용하여 메모리/CPU 중심으로 고속 처리
- 발급 로직: Kafka 메시지를 컨슘하여 I/O 중심으로 처리, 발급 서버 수평 확장 가능

## 프로젝트 API 명세서

### Dashboard API 명세서

대시보드 API는 고객사 관리자가 상점, 쿠폰 템플릿, 발행 등을 관리하는 데 사용됩니다.

* **API 링크**: [Dashboard API 명세서](https://alstn113.github.io/goodpon/dashboard/)
* **인증 방식**: Bearer 토큰 인증
* **인증 필수 헤더**:
  ```http
  Authorization: Bearer {access_token}
  ```
* **활성화**: 이메일 인증을 통해 계정을 활성화해야 서비스를 이용할 수 있습니다.

### Partner OpenAPI 명세서

파트너 OpenAPI는 고객사의 커머스 서비스와 연동되어 사용자 쿠폰 발급, 조회, 사용을 지원하는 API입니다.

* **API 링크**: [Partner OpenAPI 명세서](https://alstn113.github.io/goodpon/partner/)
* **인증 방식**: 클라이언트 키 기반 인증
* **인증 필수 헤더**:
  ```http
  X-Goodpon-Client-Id: {발급받은 상점 클라이언트 ID} ex) ck_b27e0625957529412d4476b8210142f1
  X-Goodpon-Client-Secret: {발급받은 상점 클라이언트 시크릿} ex) sk_e062595751014b2294712d4476b822f1
  ```

#### 공통 헤더

모든 응답 헤더에는 `X-Goodpon-Trace-Id`가 포함됩니다. 이 고유한 식별자는 문제가 발생했을 때 디버깅 및 기술 지원에 활용할 수 있습니다.

* **응답 헤더 예시**:
  ```json
  {
    "X-Goodpon-Trace-Id": "3e153d8453ea8bafb2604ee86f732652"
  }
  ```

#### 멱등키(Idempotency Key) 헤더

쿠폰 발급, 사용, 사용 취소 등 POST 요청 시 중복 요청을 방지하기 위해 멱등성을 지원합니다. `Idempotency-Key` 헤더를 사용하면 동일한 요청을 여러 번 보내도 같은 결과를 반환합니다.

* **사용 헤더**:

  ```http
  Idempotency-Key: {고유한 키 값}
  ```

* **멱등키 규칙**:

  * 300자 이하의 문자열로 구성하며 **UUID** 형식을 권장합니다.
  * **24시간** 동안 유효하며, 이후에는 새로운 키를 사용해야 합니다.

* **멱등성 관련 예외 처리**:

| HTTP 상태 코드                   | 설명                    | 해결 방법                   |
|:-----------------------------|:----------------------|:------------------------|
| **400 Bad Request**          | 멱등키 길이 300자 초과        | 멱등키의 길이를 확인하고 수정        |
| **409 Conflict**             | 동일한 키로 처리 중인 요청 존재    | 잠시 후 재시도                |
| **422 Unprocessable Entity** | 동일한 멱등키, 이전과 다른 요청 본문 | 새로운 멱등키를 사용 또는 요청 본문 확인 |

## 모니터링 시스템 구조

애플리케이션의 관측성을 확보하기 위해 OpenTelemetry 기반의 모니터링 스택을 구축하였습니다. JVM 런타임 시점에 함께 실행되는 OpenTelemetry Java Agent를 통해 코드 수정 없이
애플리케이션의 메트릭, 로그, 트레이스를 자동 수집합니다. 수집된 데이터는 Prometheus(메트릭), Loki(로그), Tempo(트레이스)로 전달되며, Grafana를 통해 통합 대시보드로 시각화됩니다. 이를
통해 애플리케이션의 상태를 실시간으로 효과적으로 모니터링할 수 있습니다.

<img width="4076" height="1601" alt="모니터링 시스템 구조" src="https://github.com/user-attachments/assets/d43ea258-63f7-4536-ae93-89573b41f5ec" />

## 서비스 핵심 흐름도

<details>
  <summary>🎥 대시보드 - 계정 관리 및 인증 흐름</summary>

  <img width="3692" height="3840" alt="계정관리 및 인증 흐름" src="https://github.com/user-attachments/assets/3b3460ea-344e-44c7-bbeb-892b00f73b7b" />
</details>

<details>
  <summary>🎥 대시보드 - 상점 관리 흐름</summary>

  <img width="3840" height="2734" alt="상점 관리" src="https://github.com/user-attachments/assets/b05d5344-7d51-456b-86fc-e2b5b164337a" />
</details>

<details>
  <summary>🎥 대시보드 - 쿠폰 템플릿 관리 흐름</summary>

  <img width="3840" height="3693" alt="쿠폰 템플릿" src="https://github.com/user-attachments/assets/826022c0-38be-46a3-a89c-2a38641ad48d" />
</details>

<details>
  <summary>🎥 고객사 연동 - 쿠폰 발급 및 보유 쿠폰 조회 흐름</summary>

  <img width="3258" height="3840" alt="쿠폰 발급 조회" src="https://github.com/user-attachments/assets/596895bf-1ecb-4ec6-b563-e577c6546cc2" />
</details>

<details>
  <summary>🎥 고객사 연동 - 쿠폰 사용 및 결제 연동 흐름</summary>

  <img width="3840" height="2848" alt="쿠폰 사용" src="https://github.com/user-attachments/assets/a2f36d2d-ae89-44bd-b7d6-085bfec33ccb" />
</details>
