val testcontainersVersion by extra("1.21.1")

dependencies {
    implementation(project(":modules:core"))
    implementation("jakarta.validation:jakarta.validation-api")

    // aws
    implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:3.3.1"))

    // ses & mail
    implementation("io.awspring.cloud:spring-cloud-aws-starter-ses")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("jakarta.mail:jakarta.mail-api")
    implementation("org.eclipse.angus:jakarta.mail")
}