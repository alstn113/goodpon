rootProject.name = "goodpon"

include(
    // api
    "modules:api-dashboard",
    "modules:api-core",

    // core
    "modules:core",

    // infra
    "modules:infra:jpa",
    "modules:infra:security",
    "modules:infra:redis",
    "modules:infra:aws",

    // support
    "modules:support:logging",
)
