rootProject.name = "goodpon"

include(
    // inbound - web
    "modules:api:partner-openapi",
    "modules:api:dashboard-api",

    // inbound - messaging
    "modules:consumer:coupon-issuer-consumer",

    // application
    "modules:application:partner-application",
    "modules:application:dashboard-application",
    "modules:application:coupon-issuer-application",

    // domain
    "modules:domain",

    // outbound - infra
    "modules:infra:auth",
    "modules:infra:aws:ses",
    "modules:infra:redis",
    "modules:infra:db:jpa",
    "modules:infra:db:flyway",
    "modules:infra:kafka",

    // support
    "modules:support:logging",
)
