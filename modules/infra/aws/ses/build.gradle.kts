dependencies {
    implementation(project(":modules:application:dashboard-application"))

    implementation(platform(libs.aws.sdk.bom))
    implementation("software.amazon.awssdk:sesv2")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}