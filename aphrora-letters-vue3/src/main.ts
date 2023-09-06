import {createApp} from 'vue'
import {createPinia} from 'pinia'

import App from './App.vue'
import router from './router'

// component and css of antdv
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css';

// Bootstrap's package
import '/src/assets/styles.scss'
import "@popperjs/core"
import "bootstrap"

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(Antd)

app.mount('#app')
