/* eslint-env node */
require('@rushstack/eslint-patch/modern-module-resolution')

module.exports = {
  "root": true,
  "parser": "@babel/eslint-parser",
  "parserOptions": {
    "sourceType": "module"
  },
  "extends": ["@nuxt/eslint-module"],
}
