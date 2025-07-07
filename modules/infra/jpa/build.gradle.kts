dependencies {
    implementation(project(":modules:domain"))
    implementation(project(":modules:application:partner-application"))
    implementation(project(":modules:application:dashboard-application"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")

    testImplementation(libs.testcontainers)
    testImplementation(libs.testcontainers.mysql)
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}