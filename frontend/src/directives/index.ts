import type { App } from 'vue'
import { setupPermissionDirective } from './permission/hasPermi'

/**
 * Export command: v-xxx
 * @methods hasPermi Button permissions, usage: v-hasPermi
 */
export const setupPermission = (app: App<Element>) => {
  setupPermissionDirective(app)
}
