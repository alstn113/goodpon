plugins {
    alias(libs.plugins.epage.restdocs)
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":apps:dashboard:application"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation(libs.epage.restdocs.mockmvc)
}

openapi3 {
    this.setServer("https://dashboard-api.goodpon.site")
    title = "Goodpon Dashboard API"
    description = "Goodpon Dashboard API Documentation"
    version = "1.0.0"
    format = "yaml"
}
