dependencies {
    implementation(project(":domain"))
    implementation(project(":apps:coupon-issuer:application"))
    implementation(project(":infra:jpa"))
    implementation(project(":infra:flyway"))
    implementation(project(":infra:redis"))
    implementation("org.springframework.boot:spring-boot-starter")

    testImplementation(project(":infra:jpa"))
    testImplementation(project(":infra:flyway"))
    testImplementation(project(":infra:redis"))
    testImplementation(project(":infra:kafka"))
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation(testFixtures(project(":infra:jpa")))
    testImplementation(testFixtures(project(":infra:redis")))
    testImplementation(testFixtures(project(":infra:kafka")))
}