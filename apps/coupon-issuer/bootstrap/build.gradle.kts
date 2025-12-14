dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation(project(":apps:coupon-issuer:worker"))
    runtimeOnly(project(":apps:coupon-issuer:infra"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation(project(":domain"))
    testImplementation(project(":apps:coupon-issuer:application"))
    testImplementation(project(":infra:jpa"))
    testImplementation(project(":infra:flyway"))
    testImplementation(project(":infra:redis"))
    testImplementation(project(":infra:kafka"))
    testImplementation(testFixtures(project(":infra:jpa")))
    testImplementation(testFixtures(project(":infra:redis")))
    testImplementation(testFixtures(project(":infra:kafka")))
}

tasks.bootJar {
    enabled = true
}

tasks.jar {
    enabled = false
}