// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
    ssr: true,
    devtools: {enabled: false},
    css: [
        '@/assets/css/common.css',
        '@/assets/css/index.css',
        '@/assets/scss/main.scss',
    ],
    modules: [
        '@nuxt/devtools',
        '@ant-design-vue/nuxt',
        'nuxt-bootstrap-icons',
        // '@nuxtjs/eslint-module',
        // '@pinia/nuxt',
    ],
    vite: {
        vue: {
            customElement: true
        },
        vueJsx: {
            mergeProps: true
        },
        css: {
            preprocessorOptions: {
                scss: {
                    additionalData: '@import "@/assets/scss/variable.scss";'
                },
                less: {
                    modifyVars: { // antdv3主题色配置
                        'primary-color': '#DC3545',
                        'link-color': '#8E354A',
                        'success-color': '#198754',
                        'warning-color': '#ffc107',
                    },
                    javascriptEnabled: true, // less的js编译
                }
            }
        },
        define: {
            'process.env.DEBUG': false,
        },
        server: {
            proxy: { // 配置反向代理服务器
                '/api': {
                    target: 'http://localhost:8080',
                    changeOrigin: true, // 跨域
                    rewrite: path => path.replace('/api', ''),
                }
            },
        }
    },
    nitro: {
        // 开发代理服务器配置
        devProxy: {
            '/api': {
                target: 'http://localhost:8080',
                changeOrigin: true, // 跨域
            }
        }
    }
})
