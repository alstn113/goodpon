import http from 'k6/http';
import { check } from 'k6';

export const options = {
    scenarios: {
        first_come_first_served: {
          executor: 'per-vu-iterations', // 각 가상 사용자가 지정된 횟수만큼 요청을 수행
          vus: 2000, // 가상 사용자 수
          iterations: 1, // 각 VU가 1번씩 요청
          maxDuration: '30s',
        },
    },

    thresholds: {
        'http_req_duration': ['p(95)<500'], // 95%의 요청 응답 시간이 500ms 미만
        'http_req_failed': ['rate<0.01'],   // 실패율이 1% 미만
    },
};

export default function () {
    const couponTemplateId = 5;
    const url = `http://host.docker.internal:8081/v1/coupon-templates/${couponTemplateId}/issue`;

    const payload = JSON.stringify({
        userId: `user20-${__VU}-${__ITER}`,
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
            'X-Goodpon-Client-Id': 'ck_38f3cc15ae014b1eb3028244904130e1',
            'X-Goodpon-Client-Secret': 'sk_25571911e8684e3491a8cbfcb6a7d54f',
        },
        tags: {
            k6_test_scenario: 'first_come_first_served_coupon_issue',
        }
    };

    const res = http.post(url, payload, params);

    check(res, {
        'is status 200': (r) => r.status === 200,
        'is coupon issued successfully or out of stock': (r) => {
            const responseBody = r.json();
            return responseBody && (responseBody.message === 'Coupon issued successfully' || responseBody.message === 'Out of stock');
        },
    });
}