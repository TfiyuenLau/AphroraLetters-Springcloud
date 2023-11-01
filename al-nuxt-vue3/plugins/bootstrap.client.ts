import * as bootstrap from 'bootstrap'

/**
 * 使用bootstrap.js插件
 */
export default defineNuxtPlugin((nuxtApp: any) => {
    nuxtApp.provide('bootstrap', bootstrap)
});
