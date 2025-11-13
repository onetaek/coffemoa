import type { RouteRecordRaw } from 'vue-router'
import { defineComponent } from 'vue'

/**
* redirect: noredirect        When noredirect is set, the route cannot be clicked in the breadcrumb navigation.
* name:'router-name'          Set the name of the route. Be sure to fill it in. Otherwise, various problems will occur when using <keep-alive>.
* meta : {
    hidden: true              When true is set, the route will not appear on the sidebar such as 404, login and other pages (default false)

    alwaysShow: true          When you declare more than 1 route as children under a route, it will automatically become a nested mode.
                              When there is only one, that sub-route will be displayed in the sidebar as the root route.
                              If you want to display your root route regardless of the number of children declared under the route,
                              You can set alwaysShow: true so that it ignores previously defined rules,
                              Always show the root route (default false)

    title: 'title'            Set the name of the route displayed in the sidebar and breadcrumbs

    icon: 'svg-name'          Set the icon for this route

    noCache: true             If set to true, it will not be cached by <keep-alive> (default false)

    breadcrumb: false         If set to false, it will not be displayed in breadcrumbs (default true)

    affix: true               If set to true, it will always be fixed in the tag item (default false)

    noTagsView: true          If set to true, it will not appear in the tag (default false)

    activeMenu: '/dashboard'  Show highlighted routing paths

    canTo: true               Set to true, even if hidden is true, routing jumps can still be performed (default false)

    permission: ['edit','add', 'delete']    Set permissions for this route
  }
**/

interface RouteMetaCustom extends Record<string | number | symbol, unknown> {
  hidden?: boolean
  alwaysShow?: boolean
  title?: string
  icon?: string
  noCache?: boolean
  breadcrumb?: boolean
  affix?: boolean
  activeMenu?: string
  noTagsView?: boolean
  canTo?: boolean
  permission?: string[]
}

declare module 'vue-router' {
  interface RouteMeta extends RouteMetaCustom {}
}

type Component<T = any> =
  | ReturnType<typeof defineComponent>
  | (() => Promise<typeof import('*.vue')>)
  | (() => Promise<T>)

declare global {
  declare interface AppRouteRecordRaw extends Omit<RouteRecordRaw, 'meta' | 'children'> {
    name: string
    meta: RouteMetaCustom
    component?: Component | string
    children?: AppRouteRecordRaw[]
    props?: Recordable
    fullPath?: string
  }

  declare interface AppCustomRouteRecordRaw
    extends Omit<RouteRecordRaw, 'meta' | 'component' | 'children'> {
    name: string
    meta: RouteMetaCustom
    component: string
    path: string
    redirect: string
    children?: AppCustomRouteRecordRaw[]
  }
}
