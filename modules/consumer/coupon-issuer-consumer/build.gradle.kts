dependencies {
    runtimeOnly(project(":modules:infra:db:jpa"))
    runtimeOnly(project(":modules:infra:db:flyway"))
    runtimeOnly(project(":modules:infra:redis"))
    runtimeOnly(project(":modules:infra:kafka"))

    implementation(project(":modules:domain"))
    implementation(project(":modules:application:coupon-issuer-application"))
    implementation(project(":modules:support:logging"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-json")

    testImplementation(project(":modules:infra:db:jpa"))
    testImplementation(project(":modules:infra:db:flyway"))
    testImplementation(project(":modules:infra:redis"))
    testImplementation(project(":modules:infra:kafka"))
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation(testFixtures(project(":modules:infra:db:jpa")))
    testImplementation(testFixtures(project(":modules:infra:redis")))
    testImplementation(testFixtures(project(":modules:infra:kafka")))
}

tasks.bootJar {
    enabled = true
}

tasks.jar {
    enabled = false
}