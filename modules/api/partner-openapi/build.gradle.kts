plugins {
    alias(libs.plugins.epage.restdocs)
}

dependencies {
    implementation(project(":modules:domain"))
    implementation(project(":modules:application:partner-application"))
    implementation(project(":modules:support:logging"))

    runtimeOnly(project(":modules:infra:db:jpa"))
    implementation(project(":modules:infra:db:flyway"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    testImplementation(testFixtures(project(":modules:infra:db:jpa")))

    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation(libs.epage.restdocs.mockmvc)
}

openapi3 {
    this.setServer("http://localhost:8080")
    title = "Goodpon Partner OpenAPI"
    description = "Goodpon Partner OpenAPI Documentation"
    version = "1.0.0"
    format = "yaml"
}

tasks.resolveMainClassName {
    dependsOn(copyOpenapiToSwagger)
}

val copyOpenapiToSwagger = tasks.register("copyOpenapiToSwagger") {
    group = "documentation"
    description = "Copy OpenAPI YAML to Swagger directory with security options"

    dependsOn("openapi3")

    doLast {
        val openapiSourceFile = file("build/api-spec/openapi3.yaml")
        val swaggerOptionsFile = file("build/resources/main/static/api-docs/swagger-options.yml")

        if (swaggerOptionsFile.exists() && openapiSourceFile.exists()) {
            openapiSourceFile.appendText(swaggerOptionsFile.readText(Charsets.UTF_8))
        }

        copy {
            from(openapiSourceFile)
            into("build/resources/main/static/api-docs")
        }

        delete(swaggerOptionsFile)
    }
}

tasks.bootJar {
    enabled = true

    dependsOn(copyOpenapiToSwagger)
}

tasks.jar {
    enabled = false
}
