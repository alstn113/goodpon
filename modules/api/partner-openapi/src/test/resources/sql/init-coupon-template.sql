INSERT INTO accounts (id, email, name, password, verified, verified_at, created_at, updated_at)
VALUES (1, 'test@example.com', '테스트 유저', 'encrypted-pw', 1, NOW(), NOW(), NOW());

INSERT INTO merchants (id, name, client_id, created_at, updated_at)
VALUES (10, '굿폰가맹점', 'client-abc-123', NOW(), NOW());

INSERT INTO merchant_accounts (id, account_id, merchant_id, role, created_at, updated_at)
VALUES (20, 1, 10, 'OWNER', NOW(), NOW());

INSERT INTO merchant_client_secrets (id, merchant_id, secret, expired_at, created_at, updated_at)
VALUES (30, 10, 'top-secret', '2099-12-31 23:59:59', NOW(), NOW());

INSERT INTO coupon_templates (id, merchant_id, name, description, discount_type, discount_value,
                              max_discount_amount, min_order_amount, limit_type, status,
                              validity_days, absolute_expires_at, issue_start_at, issue_end_at,
                              max_issue_count, max_redeem_count,
                              created_at, updated_at)
VALUES (100, 10, '테스트 쿠폰', '테스트 쿠폰입니다', 'FIXED_AMOUNT', 2000,
        null, 10000, 'ISSUE_COUNT', 'ISSUABLE',
        null, DATE_ADD(NOW(), INTERVAL 20 DAY)
           , NOW(), DATE_ADD(NOW(), INTERVAL 10 DAY),
        10, null,
        NOW(), NOW());

INSERT INTO coupon_template_stats (coupon_template_id, issue_count, redeem_count, created_at, updated_at)
VALUES (100, 0, 0, NOW(), NOW());
