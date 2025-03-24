import { fileURLToPath, URL } from 'node:url'
import path from 'path';
import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'

//按需加载
import Components from 'unplugin-vue-components/vite'
import { AntDesignVueResolver } from 'unplugin-vue-components/resolvers'
const { getThemeVariables } = require('ant-design-vue/dist/theme');

import autoprefixer from 'autoprefixer'


// https://vitejs.dev/config/
export default ({ mode }) => {
  return defineConfig({
    // esbuild: {
    //   pure: ['console.log'], // 删除 console.log
    //   drop: ['debugger'], // 删除 debugger
    // },
    plugins: [
      vue(),
      Components({
        resolvers: [
          AntDesignVueResolver({
            importStyle: 'less', // 一定要开启这个配置项
          }),
        ],
      }),
    ],
    css: {
      preprocessorOptions: {
        less: {
          modifyVars: {
            'primary-color': '#5657F6',
            'link-color': '#5657F6',
            'border-radius-base': '2px',
          },
          javascriptEnabled: true,
          additionalData: `@import "${path.resolve(__dirname, 'src/assets/css/base.less')}";`
        },
      },
      postcss: {
        plugins: [
          autoprefixer({
            overrideBrowserslist: [
              "Android 4.1",
              "iOS 7.1",
              "Chrome > 31",
              "ff > 31",
              "ie >= 8"
              //'last 2 versions', // 所有主流浏览器最近2个版本
            ],
            grid: true
          })
        ]
      }
    },
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      }
    },
    //修改打包目录
    base: `${loadEnv(mode, process.cwd()).VITE_API_BASE_HISTORY == '' ? '/' : loadEnv(mode, process.cwd()).VITE_API_BASE_HISTORY}`,

  })
}

