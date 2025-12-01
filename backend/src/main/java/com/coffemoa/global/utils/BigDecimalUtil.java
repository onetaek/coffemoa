package com.coffemoa.global.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtil {

  /**
   * BigDecimal 값을 원하는 자리수까지 반올림하고 뒤의 불필요한 0을 제거한 문자열로 반환한다.
   * <p>
   * 예: format(1216.6666667, 2) → "1216.67" format(1.00, 2)         → "1" format(1.50, 2)         →
   * "1.5"
   *
   * @param value BigDecimal 값
   * @param scale 원하는 소수점 자리수
   * @return 포맷된 문자열
   */
  public static String format(BigDecimal value, int scale) {
    if (value == null) {
      return null;
    }

    return value
        .setScale(scale, RoundingMode.HALF_UP)   // 자리수 반올림
        .stripTrailingZeros()                    // 뒤 0 제거
        .toPlainString();                        // 지수표기 방지
  }

  /**
   * String 입력도 지원
   */
  public static String format(String value, int scale) {
    if (value == null || value.isBlank()) {
      return null;
    }
    return format(new BigDecimal(value), scale);
  }

}
