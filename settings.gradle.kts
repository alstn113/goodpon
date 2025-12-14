rootProject.name = "goodpon"

include(
    // dashboard
    "apps:dashboard:bootstrap",
    "apps:dashboard:api",
    "apps:dashboard:scheduler",
    "apps:dashboard:application",
    "apps:dashboard:infra",

    // partner
    "apps:partner:bootstrap",
    "apps:partner:api",
    "apps:partner:application",
    "apps:partner:infra",

    // coupon-issuer
    "apps:coupon-issuer:bootstrap",
    "apps:coupon-issuer:worker",
    "apps:coupon-issuer:application",
    "apps:coupon-issuer:infra",

    "domain",

    "infra:redis",
    "infra:jpa",
    "infra:flyway",
    "infra:kafka",

    "supports:logging",
)
