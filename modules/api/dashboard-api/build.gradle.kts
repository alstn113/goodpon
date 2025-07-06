tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":modules:domain"))
    implementation(project(":modules:application:dashboard-application"))
    implementation(project(":modules:infra:jpa"))
    implementation(project(":modules:infra:aws"))
    implementation(project(":modules:infra:redis"))
    implementation(project(":modules:support:logging"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation(libs.jjwt.api)
    runtimeOnly(libs.jjwt.gson)
    runtimeOnly(libs.jjwt.impl)
}
