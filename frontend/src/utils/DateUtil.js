// src/utils/DateUtil.js

/**
 * 오늘 날짜를 yyyy-MM-dd 형식으로 반환
 */
export function today() {
    return new Date().toISOString().substring(0, 10);
}

/**
 * Date → yyyy-MM-dd 형식
 */
export function formatDate(date) {
    if (!date) return '';
    return new Date(date).toISOString().substring(0, 10);
}

/**
 * yyyy-MM-dd 문자열을 Date 객체로 변환
 */
export function toDate(str) {
    return new Date(str);
}

/**
 * 날짜 더하기(일 기준)
 * @param {*} date yyyy-MM-dd 또는 Date 객체
 * @param {*} days 더할 일 수
 */
export function addDays(date, days) {
    const d = new Date(date);
    d.setDate(d.getDate() + days);
    return formatDate(d);
}

/**
 * 날짜 범위 생성 (yyyy-MM-dd 배열)
 */
export function dateRange(start, end) {
    const result = [];
    let cur = new Date(start);

    while (cur <= new Date(end)) {
        result.push(formatDate(cur));
        cur.setDate(cur.getDate() + 1);
    }
    return result;
}
