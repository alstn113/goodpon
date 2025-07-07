tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":modules:domain"))
    implementation(project(":modules:application:dashboard-application"))

    runtimeOnly(project(":modules:infra:jpa"))
    runtimeOnly(project(":modules:infra:aws"))
    runtimeOnly(project(":modules:infra:redis"))
    runtimeOnly(project(":modules:support:logging"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation(libs.jjwt.api)
    runtimeOnly(libs.jjwt.gson)
    runtimeOnly(libs.jjwt.impl)

    testImplementation(testFixtures(project(":modules:infra:jpa")))
    testImplementation(testFixtures(project(":modules:infra:redis")))
}
