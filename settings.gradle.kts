rootProject.name = "goodpon"

include(
    "modules:api-dashboard",
    "modules:api-core",

    "modules:core",

    "modules:infra:jpa",
    "modules:infra:redis",
    "modules:infra:aws",

    "modules:support:logging",
)
