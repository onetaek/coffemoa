import 'vue/jsx'

// Introduce windi css
import '@/plugins/unocss'

// Import global svg icon
import '@/plugins/svgIcon'

// Initialize multiple languages
import { setupI18n } from '@/plugins/vueI18n'

// Introducing state management
import { setupStore } from '@/store'

// global components
import { setupGlobCom } from '@/components'

// Introduce element-plus
import { setupElementPlus } from '@/plugins/elementPlus'

// Introduce global styles
import '@/styles/index.less'

// Introduce animation
import '@/plugins/animate.css'

// routing
import { setupRouter } from './router'

// Permissions
import { setupPermission } from './directives'

import { createApp } from 'vue'

import App from './App.vue'

import './permission'

// Create instance
const setupAll = async () => {
  const app = createApp(App)

  await setupI18n(app)

  setupStore(app)

  setupGlobCom(app)

  setupElementPlus(app)

  setupRouter(app)

  setupPermission(app)

  app.mount('#app')
}

setupAll()
