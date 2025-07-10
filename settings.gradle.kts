rootProject.name = "goodpon"

include(
    "modules:api:partner-openapi",
    "modules:api:dashboard-api",

    "modules:application:partner-application",
    "modules:application:dashboard-application",

    "modules:domain",

    "modules:infra:auth",
    "modules:infra:aws:ses",
    "modules:infra:redis",
    "modules:infra:db:jpa",
    "modules:infra:db:flyway",

    "modules:support:logging",
)
