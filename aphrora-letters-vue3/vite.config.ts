import {fileURLToPath, URL} from 'node:url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
    css: {
        preprocessorOptions: {
            less: {
                modifyVars: { // antdv3主题色配置
                    'primary-color': '#DC3545',
                    'link-color': '#8E354A',
                    'success-color': '#198754',
                    'warning-color': '#ffc107',
                },
                javascriptEnabled: true, // less的js编译
            }
        },
    },
    plugins: [
        vue(),
    ],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
    },
    server: {
        host: true,
        port: 80,
        proxy: {// 配置反向代理服务器
            '/api': {
                target: 'http://localhost:8080',
                changeOrigin: true, // 跨域
                rewrite: path => path.replace('/api', ''),
            }
        },
    }
})
