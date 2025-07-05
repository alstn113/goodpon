tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

val jjwtVersion by extra("0.12.6")

dependencies {
    implementation(project(":modules:domain"))
    implementation(project(":modules:infra:jpa"))
    implementation(project(":modules:infra:aws"))
    implementation(project(":modules:infra:redis"))
    implementation(project(":modules:support:logging"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-gson:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
}
