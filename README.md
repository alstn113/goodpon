# 굿폰(Goodpon), 쿠폰/프로모션 발행 및 관리 B2B 서비스

**▷ 개발 기간 : 2025.06 ~ 진행 중** </br>
**▷ 개발 인원 : 1명**

## 프로젝트 아키텍처

Goodpon은 `Dashboard API`와 `Partner OpenAPI`로 서버가 분리되어 있습니다.
프로젝트가 진화함에 따라, 처리량이 많은 `Partner OpenAPI`는 향후 **Spring WebFlux**로 전환될 예정입니다.

이를 위해서는 기존 **Spring Web MVC** 기반에서 **WebFlux** 기반으로의 전환뿐만 아니라, **JPA에서 R2DBC로의 변경**도 수반되어야 합니다. 이러한 변경에 유연하게 대응할 수 있도록, 헥사고날 아키텍처를 적용하여 인프라에 대한 의존성을 최소화했습니다.

`api`, `application`, `domain` 모듈은 모두 컴파일 타임에 `infra`에 대한 의존성을 가지지 않으며, `infra`는 `api`의 **런타임 시점에만 주입**됩니다.

현재 다이어그램에는 `infra-db-jpa` 모듈만 표시되어 있으며, **지면의 제한**으로 인해 다른 인프라 모듈은 생략되었습니다.

![goodpon0713 drawio](https://github.com/user-attachments/assets/b1ad3c21-0a33-4673-b6b4-f30903748fc0)

## 프로젝트 API 명세서

- ### [Dashboard API 명세서](https://alstn113.github.io/goodpon/dashboard/)   
- ### [Partner OpenAPI 명세서](https://alstn113.github.io/goodpon/partner/)

## 프로젝트 기술 스택

- Kotlin
- Spring Boot
- Spring Web MVC
- Spring WebFlux
- Spring Data JPA
- R2DBC
- Spring Security
- MySQL
- Redis
- AWS SES
- Testcontainers
