plugins {
    alias(libs.plugins.epage.restdocs)
}

dependencies {
    implementation(project(":modules:domain"))
    implementation(project(":modules:application:dashboard-application"))
    implementation(project(":modules:support:logging"))

    runtimeOnly(project(":modules:infra:jpa"))
    runtimeOnly(project(":modules:infra:aws:ses"))
    runtimeOnly(project(":modules:infra:redis"))
    runtimeOnly(project(":modules:infra:auth"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    testImplementation(testFixtures(project(":modules:infra:jpa")))
    testImplementation(testFixtures(project(":modules:infra:redis")))

    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation(libs.epage.restdocs.mockmvc)
}

openapi3 {
    this.setServer("https://dashboard.goodpon.site")
    title = "Goodpon Dashboard API"
    description = "Goodpon Dashboard API Documentation"
    version = "1.0.0"
    format = "yaml"
}

tasks.resolveMainClassName {
    dependsOn(copyOpenapiToSwagger)
}

val copyOpenapiToSwagger = tasks.register<Copy>("copyOpenapiToSwagger") {
    group = "documentation"
    description = "Copy OpenAPI generated documentation to Swagger directory"

    from("build/api-spec/openapi3.yaml")
    into("build/resources/main/static/api-docs")

    dependsOn("openapi3")
}

tasks.bootJar {
    enabled = true

    dependsOn(copyOpenapiToSwagger)
}

tasks.jar {
    enabled = false
}
