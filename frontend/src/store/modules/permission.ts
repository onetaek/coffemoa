import { defineStore } from 'pinia'
import { asyncRouterMap, constantRouterMap } from '@/router'
import {
  generateRoutesByFrontEnd,
  generateRoutesByServer,
  flatMultiLevelRoutes
} from '@/utils/routerHelper'
import { store } from '../index'
import { cloneDeep } from 'lodash-es'

export interface PermissionState {
  routers: AppRouteRecordRaw[]
  addRouters: AppRouteRecordRaw[]
  isAddRouters: boolean
  menuTabRouters: AppRouteRecordRaw[]
}

export const usePermissionStore = defineStore('permission', {
  state: (): PermissionState => ({
    routers: [],
    addRouters: [],
    isAddRouters: false,
    menuTabRouters: []
  }),
  getters: {
    getRouters(): AppRouteRecordRaw[] {
      return this.routers
    },
    getAddRouters(): AppRouteRecordRaw[] {
      return flatMultiLevelRoutes(cloneDeep(this.addRouters))
    },
    getIsAddRouters(): boolean {
      return this.isAddRouters
    },
    getMenuTabRouters(): AppRouteRecordRaw[] {
      return this.menuTabRouters
    }
  },
  actions: {
    generateRoutes(
      type: 'server' | 'frontEnd' | 'static',
      routers?: AppCustomRouteRecordRaw[] | string[]
    ): Promise<unknown> {
      return new Promise<void>((resolve) => {
        let routerMap: AppRouteRecordRaw[] = []
        if (type === 'server') {
          // Simulate backend filter menu
          routerMap = generateRoutesByServer(routers as AppCustomRouteRecordRaw[])
        } else if (type === 'frontEnd') {
          // Simulate front-end filter menu
          routerMap = generateRoutesByFrontEnd(cloneDeep(asyncRouterMap), routers as string[])
        } else {
          // Directly read the static routing table
          routerMap = cloneDeep(asyncRouterMap)
        }
        // Dynamic routing, 404 must be placed at the end
        this.addRouters = routerMap.concat([
          {
            path: '/:path(.*)*',
            redirect: '/404',
            name: '404Page',
            meta: {
              hidden: true,
              breadcrumb: false
            }
          }
        ])
        // Render all routes of the menu
        this.routers = cloneDeep(constantRouterMap).concat(routerMap)
        resolve()
      })
    },
    setIsAddRouters(state: boolean): void {
      this.isAddRouters = state
    },
    setMenuTabRouters(routers: AppRouteRecordRaw[]): void {
      this.menuTabRouters = routers
    }
  },
  persist: [
    {
      pick: ['routers'],
      storage: localStorage
    },
    {
      pick: ['addRouters'],
      storage: localStorage
    },
    {
      pick: ['menuTabRouters'],
      storage: localStorage
    }
  ]
})

export const usePermissionStoreWithOut = () => {
  return usePermissionStore(store)
}
