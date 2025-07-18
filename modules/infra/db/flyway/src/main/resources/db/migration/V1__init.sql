CREATE TABLE IF NOT EXISTS accounts
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    email       VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    verified    BIT          NOT NULL,
    verified_at DATETIME(6),
    created_at  DATETIME(6)  NOT NULL,
    updated_at  DATETIME(6)  NOT NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS merchants
(
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL,
    client_id  VARCHAR(35)  NOT NULL,
    created_at DATETIME(6)  NOT NULL,
    updated_at DATETIME(6)  NOT NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS merchant_accounts
(
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    account_id  BIGINT      NOT NULL,
    merchant_id BIGINT      NOT NULL,
    role        VARCHAR(25) NOT NULL,
    created_at  DATETIME(6) NOT NULL,
    updated_at  DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB;

ALTER TABLE merchant_accounts
    ADD CONSTRAINT fk_merchant_accounts_account_id_XXXX
        FOREIGN KEY (account_id) REFERENCES accounts (id);

CREATE TABLE IF NOT EXISTS merchant_client_secrets
(
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    merchant_id BIGINT      NOT NULL,
    secret      VARCHAR(35) NOT NULL,
    expired_at  DATETIME(6),
    created_at  DATETIME(6) NOT NULL,
    updated_at  DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS coupon_templates
(
    id                  BIGINT       NOT NULL AUTO_INCREMENT,
    name                VARCHAR(255) NOT NULL,
    description         VARCHAR(255) NOT NULL,
    discount_type       VARCHAR(30)  NOT NULL,
    discount_value      INTEGER      NOT NULL,
    max_discount_amount INTEGER,
    min_order_amount    INTEGER,
    status              VARCHAR(30)  NOT NULL,
    validity_days       INTEGER,
    absolute_expires_at DATETIME(6),
    issue_start_at      DATETIME(6)  NOT NULL,
    issue_end_at        DATETIME(6),
    limit_type          VARCHAR(30)  NOT NULL,
    max_issue_count     BIGINT,
    max_redeem_count    BIGINT,
    merchant_id         BIGINT       NOT NULL,
    created_at          DATETIME(6)  NOT NULL,
    updated_at          DATETIME(6)  NOT NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS coupon_template_stats
(
    coupon_template_id BIGINT      NOT NULL,
    issue_count        BIGINT      NOT NULL,
    redeem_count       BIGINT      NOT NULL,
    created_at         DATETIME(6) NOT NULL,
    updated_at         DATETIME(6) NOT NULL,
    PRIMARY KEY (coupon_template_id)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS user_coupons
(
    id                 VARCHAR(32)  NOT NULL,
    user_id            VARCHAR(255) NOT NULL,
    coupon_template_id BIGINT       NOT NULL,
    status             VARCHAR(30)  NOT NULL,
    expires_at         DATETIME(6),
    issued_at          DATETIME(6)  NOT NULL,
    redeemed_at        DATETIME(6),
    created_at         DATETIME(6)  NOT NULL,
    updated_at         DATETIME(6)  NOT NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS coupon_histories
(
    id             VARCHAR(32)  NOT NULL,
    user_coupon_id VARCHAR(255) NOT NULL,
    action_type    VARCHAR(30)  NOT NULL,
    order_id       VARCHAR(255),
    reason         VARCHAR(255),
    recorded_at    DATETIME(6)  NOT NULL,
    created_at     DATETIME(6)  NOT NULL,
    updated_at     DATETIME(6)  NOT NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB;
