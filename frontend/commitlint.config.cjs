module.exports = {
  extends: ['@commitlint/config-conventional'],
  rules: {
    'type-enum': [
      2,
      'always',
      [
        'feat', // new features(feature)
        'fix', // fix bugs
        'docs', // document(documentation)
        'style', // Format, style (changes that do not affect code operation)
        'refactor', // Refactoring (that is, not adding new functions or modifying BUG code)
        'perf', // Optimization related, such as improving performance and experience
        'test', // Add test
        'ci', // Continuous integration modifications
        'chore', // Changes to the build process or auxiliary tools
        'revert', // Roll back to previous version
        'workflow', // Workflow improvements
        'mod', // Uncertain classification changes
        'wip', // Under development
        'types', // Type modification
        'release' // version release
      ]
    ],
    'subject-full-stop': [0, 'never'],
    'subject-case': [0, 'never']
  }
}
