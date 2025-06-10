plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "goodpon"

include(
    "goodpon-api-core",
    "goodpon-api-dashboard",
    "goodpon-common",
    "goodpon-infra-security"
)
