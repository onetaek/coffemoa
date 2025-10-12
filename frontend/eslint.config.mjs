// Introducing eslint of vue template
import pluginVue from 'eslint-plugin-vue'
import eslint from '@eslint/js'
// ts-eslint parser, which enables eslint to parse ts grammar
import tseslint from 'typescript-eslint'
// vue file parser
import vueParser from 'vue-eslint-parser'
import prettier from 'eslint-plugin-prettier'

export default tseslint.config({
  // ignores: ['node_modules', 'prettier.config.cjs', 'dist*'],
  files: ['src/**/*.ts', 'src/**/*.tsx', 'src/**/*.vue'],
  // tseslint.config adds the extends flat function, which can be used directly. Otherwise, the eslint9.0 version does not have extends.
  extends: [
    eslint.configs.recommended,
    ...tseslint.configs.recommended,
    ...pluginVue.configs['flat/essential']
  ],
  plugins: {
    prettier
  },
  languageOptions: {
    parser: vueParser, // Use the vue parser, which can identify vue files
    parserOptions: {
      parser: tseslint.parser, // Using ts parser on vue files
      sourceType: 'module',
      ecmaVersion: 2020,
      ecmaFeatures: {
        jsx: true
      }
    }
  },
  rules: {
    'prettier/prettier': 'error',
    'no-useless-escape': 0,
    'no-undef': 0,
    '@typescript-eslint/no-unused-expressions': 0,
    '@typescript-eslint/no-unsafe-function-type': 0,
    'vue/no-setup-props-destructure': 0,
    'vue/script-setup-uses-vars': 1,
    'vue/no-reserved-component-names': 0,
    '@typescript-eslint/ban-ts-ignore': 0,
    '@typescript-eslint/explicit-function-return-type': 0,
    '@typescript-eslint/no-explicit-any': 0,
    '@typescript-eslint/no-var-requires': 0,
    '@typescript-eslint/no-empty-function': 0,
    'vue/custom-event-name-casing': 0,
    'no-use-before-define': 0,
    '@typescript-eslint/no-use-before-define': 0,
    '@typescript-eslint/ban-ts-comment': 0,
    '@typescript-eslint/ban-types': 0,
    '@typescript-eslint/no-non-null-assertion': 0,
    '@typescript-eslint/explicit-module-boundary-types': 0,
    '@typescript-eslint/no-unused-vars': 0,
    'no-unused-vars': 0,
    'space-before-function-paren': 0,
    'vue/attributes-order': 0,
    'vue/one-component-per-file': 0,
    'vue/html-closing-bracket-newline': 0,
    'vue/max-attributes-per-line': 0,
    'vue/multiline-html-element-content-newline': 0,
    'vue/singleline-html-element-content-newline': 0,
    'vue/attribute-hyphenation': 0,
    'vue/require-default-prop': 0,
    'vue/require-explicit-emits': 0,
    'vue/html-self-closing': [
      1,
      {
        html: {
          void: 'always',
          normal: 'never',
          component: 'always'
        },
        svg: 'always',
        math: 'always'
      }
    ],
    'vue/multi-word-component-names': 0,
    'vue/no-v-html': 0,
    'vue/require-toggle-inside-transition': 0
  }
})
