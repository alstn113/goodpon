plugins {
    alias(libs.plugins.epage.restdocs)
}

dependencies {
    implementation(project(":modules:domain"))
    implementation(project(":modules:application:partner-application"))
    implementation(project(":modules:support:logging"))

    runtimeOnly(project(":modules:infra:db:jpa"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    testImplementation(testFixtures(project(":modules:infra:db:jpa")))

    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation(libs.epage.restdocs.mockmvc)
}

openapi3 {
    this.setServer("https://api.goodpon.site")
    title = "Goodpon Partner OpenAPI"
    description = "Goodpon Partner OpenAPI Documentation"
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
