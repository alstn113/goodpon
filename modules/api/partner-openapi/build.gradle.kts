plugins {
    alias(libs.plugins.epage.restdocs)
}

dependencies {
    runtimeOnly(project(":modules:infra:db:jpa"))
    runtimeOnly(project(":modules:infra:db:flyway"))

    implementation(project(":modules:domain"))
    implementation(project(":modules:application:partner-application"))
    implementation(project(":modules:support:logging"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    testImplementation(project(":modules:infra:db:jpa"))
    testImplementation(project(":modules:infra:db:flyway"))
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation(libs.epage.restdocs.mockmvc)
    testImplementation(libs.rest.assured)
    // 생성 후 조회 로직이 없는 것들을 테스트하기 위해 의존함. 코드 레벨에서 의존성을 분리했음.
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation(testFixtures(project(":modules:infra:db:jpa")))
}

openapi3 {
    this.setServer("http://localhost:8081")
    title = "Goodpon Partner OpenAPI"
    description = "Goodpon Partner OpenAPI Documentation"
    version = "1.0.0"
    format = "yaml"
}

tasks.bootJar {
    enabled = true

    dependsOn("openapi3")
}

tasks.jar {
    enabled = false
}
