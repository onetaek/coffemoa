/* eslint-env node */
require('@rushstack/eslint-patch/modern-module-resolution');

module.exports = {
    root: true,
    env: {
        node: true
    },
    extends: [
        'plugin:vue/vue3-essential',
        'eslint:recommended',
        '@vue/eslint-config-prettier' // ⬅ Prettier와 충돌되는 eslint 규칙 자동 OFF
    ],
    parserOptions: {
        ecmaVersion: 'latest'
    },
    rules: {
        // Vue 규칙
        'vue/multi-word-component-names': 'off',
        'vue/no-reserved-component-names': 'off',
        'vue/component-tags-order': [
            'error',
            {
                order: ['script', 'template', 'style']
            }
        ],

        // ⬇⬇⬇ 핵심! Prettier 경고 완전 차단
        'prettier/prettier': 'off'
    }
};
