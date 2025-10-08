# 🎨 Vue 코드 스타일 규칙

본 문서는 Vue 프로젝트의 코드 스타일 및 포맷팅 규칙을 정의합니다.  
목표는 **일관된 코드 스타일**, **가독성 향상**, **자동화된 품질 관리**를 통해 유지보수를 쉽게 하는 것입니다.

---

## 🧩 코드 스타일 개요

Vue 프로젝트는 다음 도구들을 이용해 코드 품질과 스타일을 관리합니다.

| 구분 | 도구 | 역할 |
|------|------|------|
| **JavaScript / TypeScript** | ESLint | 코드 문법 및 스타일 검증 |
| **CSS / Less / SCSS** | Stylelint | 스타일 시트 규칙 검증 |
| **모든 코드 형식** | Prettier | 코드 자동 포맷터 |
| **Git 커밋 메시지** | Commitlint | 커밋 메시지 규칙 검증 |
| **Git 훅** | Husky + lint-staged | 커밋 시 코드 자동 검사 및 수정 |

---

## 📘 ESLint

### 🔧 역할
ESLint는 JavaScript 및 TypeScript 코드의 **문법 오류와 스타일 불일치**를 검사합니다.  
프로젝트 팀의 사양에 맞게 설정되어 있으며, 자동 수정 기능도 제공합니다.

### ⚙️ 구성 위치
- 설정 파일: `.eslintrc.js`
- 명령어 실행:
  ```bash
  pnpm run lint:eslint
  ```
  → 자동으로 수정 가능한 부분은 ESLint가 포맷을 적용합니다.

---

## 🎨 Stylelint

### 🔧 역할
Stylelint는 CSS, Less, SCSS 파일의 **스타일 규칙 일관성**을 유지하기 위한 도구입니다.  
Vue의 `<style>` 블록 내부 스타일도 검사 대상에 포함됩니다.

### ⚙️ 구성 위치
- 설정 파일: `stylelint.config.js`

### 💡 추천 VSCode 플러그인
> 저장 시 자동 포맷 기능을 사용하려면 다음 플러그인을 설치하세요.
- **Stylelint**

---

## ✨ Prettier

### 🔧 역할
Prettier는 프로젝트 전반의 코드 스타일을 통일합니다.  
들여쓰기, 따옴표(`'` vs `"`), 세미콜론, 쉼표 스타일 등 다양한 포맷을 자동으로 맞춰줍니다.

### ⚙️ 구성 위치
- 설정 파일: `prettier.config.js`

### 💡 추천 VSCode 플러그인
> 저장 시 자동 포맷 기능을 사용하려면 다음 플러그인을 설치하세요.
- **Prettier - Code Formatter**

---

## 🪝 Git Hook (Husky + lint-staged)

### 🔧 역할
코드 커밋 시 자동으로 Lint 검증을 수행하며,  
문제가 있으면 커밋이 차단되어 코드 품질이 유지됩니다.

### ⚙️ lint-staged 구성
- 설정 파일: `.husky/lintstagedrc.js`
- 주요 설정 예시:
  ```js
  module.exports = {
    '*.{js,jsx,ts,tsx}': ['eslint --fix', 'prettier --write'],
    '*.vue': ['eslint --fix', 'stylelint --fix', 'prettier --write', 'git add .'],
    '*.{scss,less,styl,css,html}': ['stylelint --fix', 'prettier --write', 'git add .'],
    '*.md': ['prettier --write'],
  };
  ```

---

## 🚫 검증 건너뛰기 (선택 사항)

Lint 검증을 임시로 생략하려면 커밋 시 `--no-verify` 옵션을 사용합니다.

```bash
git commit -m "feat: add new component" --no-verify
```

---

## ✅ 요약

| 항목 | 도구 | 설정 파일 | 주요 목적 |
|------|------|------------|------------|
| JS/TS 코드 검사 | ESLint | `.eslintrc.js` | 문법 및 스타일 검사 |
| CSS/Less 검사 | Stylelint | `stylelint.config.js` | 스타일 일관성 유지 |
| 코드 포맷 | Prettier | `prettier.config.js` | 자동 코드 정렬 |
| 커밋 메시지 규칙 | Commitlint | `commitlint.config.js` | 커밋 메시지 형식 검증 |
| Git 훅 | Husky + lint-staged | `.husky/` | 커밋 전 코드 검사 및 자동 수정 |
