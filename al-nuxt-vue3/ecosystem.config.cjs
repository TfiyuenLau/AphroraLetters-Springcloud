/**
 * pm2 node进程管理工具配置文件
 * 编译输出.output文件夹后使用 `pm2 start .\ecosystem.config.cjs` 启动项目
 *
 * @type {{apps: [{port: string, instances: string, name: string, exec_mode: string, script: string}]}}
 */
module.exports = {
    apps: [
        {
            name: 'AlNuxtVue3',
            port: '3000',
            exec_mode: 'cluster',
            instances: '4',
            script: './.output/server/index.mjs'
        }
    ]
}
