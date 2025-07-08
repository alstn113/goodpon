dependencies {
    implementation(project(":modules:application:dashboard-application"))

    implementation("org.springframework.boot:spring-boot-starter")

    implementation(libs.jjwt.api)
    runtimeOnly(libs.jjwt.gson)
    runtimeOnly(libs.jjwt.impl)
}