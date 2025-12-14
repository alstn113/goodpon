dependencies {
    implementation(project(":domain"))
    implementation(project(":apps:coupon-issuer:application"))
    implementation(project(":infra:jpa"))
    implementation(project(":infra:flyway"))
    implementation(project(":infra:redis"))
    implementation("org.springframework.boot:spring-boot-starter")

    testImplementation(testFixtures(project(":infra:jpa")))
    testImplementation(testFixtures(project(":infra:redis")))
}