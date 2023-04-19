import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import * as ElementPlusIconVue from "@element-plus/icons-vue"

import 'element-plus/dist/index.css'
import './assets/main.css'

const app = createApp(App)

app.use(router)
app.use(ElementPlus)
for(const [key,component] of Object.entries(ElementPlusIconVue)){
    app.component(key, component)
}


app.mount('#app')
