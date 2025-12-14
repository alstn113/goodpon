dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation(project(":domain"))
    implementation(project(":apps:dashboard:application"))
    implementation(project(":infra:jpa"))
    implementation(project(":infra:flyway"))
    implementation(project(":infra:redis"))
    implementation(project(":infra:kafka"))

    // jwt
    implementation(libs.jjwt.api)
    runtimeOnly(libs.jjwt.gson)
    runtimeOnly(libs.jjwt.impl)

    // email
    implementation(platform(libs.aws.sdk.bom))
    implementation("software.amazon.awssdk:sesv2")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    testImplementation(testFixtures(project(":infra:jpa")))
    testImplementation(testFixtures(project(":infra:redis")))
    testImplementation(testFixtures(project(":infra:kafka")))
}