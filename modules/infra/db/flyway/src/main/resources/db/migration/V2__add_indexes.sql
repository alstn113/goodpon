ALTER TABLE merchant_accounts
    ADD INDEX idx_merchant_accounts_merchant_id_account_id (merchant_id, account_id);

ALTER TABLE merchant_client_secrets
    ADD INDEX idx_merchant_client_secrets_secret (secret);

ALTER TABLE coupon_templates
    ADD INDEX idx_coupon_templates_status_absolute_expires_at (status, absolute_expires_at);

ALTER TABLE user_coupons
    ADD INDEX idx_user_coupons_status_expires_at (status, expires_at);
ALTER TABLE user_coupons
    ADD INDEX idx_user_coupons_user_id_status (user_id, status);

ALTER TABLE coupon_histories
    ADD INDEX idx_coupon_histories_user_coupon_id_recorded_at (user_coupon_id, recorded_at DESC);
ALTER TABLE coupon_histories
    ADD INDEX idx_coupon_histories_recorded_at_id (recorded_at, id);
ALTER TABLE coupon_histories
    ADD INDEX idx_coupon_histories_order_id (order_id);