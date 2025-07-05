val testcontainersVersion by extra("1.21.1")

dependencies {
    implementation(project(":modules:domain"))
    implementation("jakarta.validation:jakarta.validation-api")

    implementation(platform("software.amazon.awssdk:bom:2.31.72"))
    implementation("software.amazon.awssdk:sesv2")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
}