import http from 'k6/http';
import {check} from 'k6';

export const options = {
    scenarios: {
        first_come_first_served: {
            executor: 'per-vu-iterations', // 각 가상 사용자가 지정된 횟수만큼 요청을 수행
            vus: 1000, // 가상 사용자 수
            iterations: 1, // 각 VU가 1번씩 요청
            maxDuration: '30s',
        },
    },

    thresholds: {
        'http_req_duration': ['p(95)<1000'], // 95%의 요청 응답 시간이 500ms 미만
        'http_req_failed': ['rate<0.01'],   // 실패율이 1% 미만
    },
};

export default function () {
    const couponTemplateId = 1;
    const url = `http://host.docker.internal:8081/v1/coupon-templates/${couponTemplateId}/issue`;

    const payload = JSON.stringify({
        userId: `test-2025-08-24-15-59-${__VU}-${__ITER}`,
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
            'X-Goodpon-Client-Id': 'ck_9bb5db89263741de9829c66ae19e321f',
            'X-Goodpon-Client-Secret': 'sk_37b1e5e153444d558e03dca5a2e82290',
        },
        tags: {
            k6_test_scenario: 'test-2025-08-24-15-59',
        }
    };

    const res = http.post(url, payload, params);

    check(res, {
        'is status 200': (r) => r.status === 200,
    });
}