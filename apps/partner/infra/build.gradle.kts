dependencies {
    implementation(project(":domain"))
    implementation(project(":infra:jpa"))
    implementation(project(":infra:flyway"))
    implementation(project(":infra:redis"))
    implementation(project(":infra:kafka"))
    implementation(project(":apps:partner:application"))

    testImplementation(testFixtures(project(":infra:jpa")))
    testImplementation(testFixtures(project(":infra:redis")))
    testImplementation(testFixtures(project(":infra:kafka")))
}