val jjwtVersion by extra("0.12.6")
val testcontainersVersion by extra("1.21.1")

dependencies {
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
    implementation("jakarta.validation:jakarta.validation-api")

    // db
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.hypersistence:hypersistence-utils-hibernate-63:3.9.10")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    // jjwt
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-gson:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.mindrot:jbcrypt:0.4")

    // aws
    implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:3.3.1"))
    implementation("io.awspring.cloud:spring-cloud-aws-starter-ses")

    // mail
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("jakarta.mail:jakarta.mail-api")
    implementation("org.eclipse.angus:jakarta.mail")

    // testcontainers
    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
    testImplementation("org.testcontainers:mysql:$testcontainersVersion")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}
