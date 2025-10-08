# 🏷 Java 코드 규칙

본 프로젝트에서는 다음 규칙을 준수한다:

---

## 📐 코드 스타일

- IntelliJ에서 **`intellij-java-google-style.xml`** 파일을 코드 스타일 정의로 사용한다.
- IDE 설정 시 해당 스타일 파일을 가져와야 한다.
- 코드 포맷팅은 IDE 자동 포맷 기능이나 저장 시 포맷팅 플러그인을 활용해 일관되게 유지한다.

---

## 📚 스타일 가이드 기본 원칙

- 코드 작성 시 [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) 규칙을 기본으로 따른다.  
  주요 규칙 예시:

  1. 소스 파일은 `UTF-8` 인코딩 사용  
  2. 탭 문자 대신 공백만 사용 (들여쓰기 등)  
  3. 최대 열 길이(Column limit): 100자  
  4. 중괄호 스타일: K&R 스타일 등  
  5. 한 줄에 하나의 문장  
  6. 와일드카드(import *) 사용 금지  
  7. 클래스, 메서드, 필드, 변수 등 네이밍 규칙 준수  
     - 클래스명: UpperCamelCase  
     - 메서드/변수: lowerCamelCase  
     - 상수: UPPER_SNAKE_CASE  
  8. `@Override` 애너테이션은 가능한 사용  
  9. 예외 처리 시 빈 catch 블록 허용 시 명확한 주석 필요  
  10. Javadoc 및 주석 작성 규칙 준수  

- 위 링크의 완전한 가이드 내용을 기준으로 예외 없이 적용한다.

---

## 🧩 적용 및 예외 처리

- 특정 상황에서 Google Style 규칙이 비현실적이거나 가독성 저해 요소가 있을 경우 팀 또는 본인이 판단하여 예외 적용 가능  
- 예외를 적용할 경우, 코드 내부 또는 PR 설명에 **왜 예외를 허용했는지**를 주석 또는 문서로 남긴다  
- 스타일 파일 또는 포맷터(예: IntelliJ 자동 포맷, Save Actions 등)를 사용해 수동 포맷 차이 최소화

---

## ✅ 요약

| 항목 | 규칙 |
|---|---|
| 스타일 설정 | `intellij-java-google-style.xml` 사용 |
| 기준 가이드 | Google Java Style Guide 준수 |
| 예외 허용 | 가능하나 명시적 주석 또는 문서화 필요 |

---

> 📎 참고  
> - Google Java Style Guide: https://google.github.io/styleguide/javaguide.html  
> - IntelliJ 스타일 XML 설정 파일은 프로젝트에 포함하여 버전 관리 가능  
