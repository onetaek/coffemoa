/**
 *
 * @param component Components that need to be registered
 * @param alias component alias
 * @returns any
 */
export const withInstall = <T>(component: T, alias?: string) => {
  const comp = component as any
  comp.install = (app: any) => {
    app.component(comp.name || comp.displayName, component)
    if (alias) {
      app.config.globalProperties[alias] = component
    }
  }
  return component as T & Plugin
}

/**
 * @param str CamelCase string that needs to be underlined
 * @returns String underscore
 */
export const humpToUnderline = (str: string): string => {
  return str.replace(/([A-Z])/g, '-$1').toLowerCase()
}

/**
 * @param str Underscore string that needs to be converted to camel case
 * @returns string hump
 */
export const underlineToHump = (str: string): string => {
  if (!str) return ''
  return str.replace(/\-(\w)/g, (_, letter: string) => {
    return letter.toUpperCase()
  })
}

/**
 * Hump ​​to bar
 */
export const humpToDash = (str: string): string => {
  return str.replace(/([A-Z])/g, '-$1').toLowerCase()
}

export const setCssVar = (prop: string, val: any, dom = document.documentElement) => {
  dom.style.setProperty(prop, val)
}

export const getCssVar = (prop: string, dom = document.documentElement) => {
  return getComputedStyle(dom).getPropertyValue(prop)
}

/**
 * Find a subscript of an array object
 * @param {Array} ary array to search for
 * @param {Functon} fn method of judgment
 */
export const findIndex = <T = Recordable>(ary: Array<T>, fn: Fn): number => {
  if (ary.findIndex) {
    return ary.findIndex(fn)
  }
  let index = -1
  ary.some((item: T, i: number, ary: Array<T>) => {
    const ret: T = fn(item, i, ary)
    if (ret) {
      index = i
      return ret
    }
  })
  return index
}

export const trim = (str: string) => {
  return str.replace(/(^\s*)|(\s*$)/g, '')
}

/**
 * @param {Date | number | string} time Time required to convert
 * @param {String} fmt The format that needs to be converted is as follows: yyyy-MM-dd、yyyy-MM-dd HH:mm:ss
 */
export function formatTime(time: Date | number | string, fmt: string) {
  if (!time) return ''
  else {
    const date = new Date(time)
    const o = {
      'M+': date.getMonth() + 1,
      'd+': date.getDate(),
      'H+': date.getHours(),
      'm+': date.getMinutes(),
      's+': date.getSeconds(),
      'q+': Math.floor((date.getMonth() + 3) / 3),
      S: date.getMilliseconds()
    }
    if (/(y+)/.test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
    }
    for (const k in o) {
      if (new RegExp('(' + k + ')').test(fmt)) {
        fmt = fmt.replace(
          RegExp.$1,
          RegExp.$1.length === 1 ? o[k] : ('00' + o[k]).substr(('' + o[k]).length)
        )
      }
    }
    return fmt
  }
}

/**
 * Generate random string
 */
export function toAnyString() {
  const str: string = 'xxxxx-xxxxx-4xxxx-yxxxx-xxxxx'.replace(/[xy]/g, (c: string) => {
    const r: number = (Math.random() * 16) | 0
    const v: number = c === 'x' ? r : (r & 0x3) | 0x8
    return v.toString()
  })
  return str
}

/**
 * Capitalize first letter
 */
export function firstUpperCase(str: string) {
  return str.toLowerCase().replace(/( |^)[a-z]/g, (L) => L.toUpperCase())
}

/**
 * Convert object to formData
 */
export function objToFormData(obj: Recordable) {
  const formData = new FormData()
  Object.keys(obj).forEach((key) => {
    formData.append(key, obj[key])
  })
  return formData
}
