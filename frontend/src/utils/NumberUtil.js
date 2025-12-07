// src/utils/NumberUtil.js

/**
 * 숫자를 1,234 형태로 포맷
 */
export function formatNumber(value) {
    if (value === null || value === undefined || value === '') return '0';
    if (isNaN(value)) return String(value);

    return Number(value).toLocaleString('ko-KR');
}

/**
 * 숫자를 1,234원 형태로 포맷
 */
export function formatCurrency(value) {
    if (value === null || value === undefined || value === '') return '0원';
    if (isNaN(value)) return String(value);

    return Number(value).toLocaleString('ko-KR') + '원';
}

/**
 * "1,234원" → 1234 형태로 변환
 */
export function parseNumber(str) {
    if (!str) return 0;
    return Number(String(str).replace(/[^0-9\-]/g, ''));
}

/**
 * 금액에 단위 추가 (예: 10000 → "1만", 1500000 → "150만")
 * 단위: 만 / 억 / 조
 */
export function formatShort(value) {
    if (!value || isNaN(value)) return '0';

    const num = Number(value);

    if (num >= 1_0000_0000) return (num / 1_0000_0000).toFixed(1) + '억';
    if (num >= 1_0000) return (num / 10000).toFixed(1) + '만';

    return num.toLocaleString('ko-KR');
}

/**
 * 숫자 여부 체크
 */
export function isNumber(value) {
    return !isNaN(value) && value !== null && value !== '' && value !== false;
}
