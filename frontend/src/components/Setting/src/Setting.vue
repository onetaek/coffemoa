<script setup lang="ts">
import { ElDrawer, ElDivider, ElMessage } from 'element-plus'
import { ref, unref } from 'vue'
import { useI18n } from '@/hooks/web/useI18n'
import { ThemeSwitch } from '@/components/ThemeSwitch'
import { useCssVar } from '@vueuse/core'
import { useAppStore } from '@/store/modules/app'
import { trim, setCssVar } from '@/utils'
import ColorRadioPicker from './components/ColorRadioPicker.vue'
import InterfaceDisplay from './components/InterfaceDisplay.vue'
import LayoutRadioPicker from './components/LayoutRadioPicker.vue'
import { useStorage } from '@/hooks/web/useStorage'
import { useClipboard } from '@vueuse/core'
import { useDesign } from '@/hooks/web/useDesign'

const { clear: storageClear } = useStorage('localStorage')

const { getPrefixCls } = useDesign()

const prefixCls = getPrefixCls('setting')

const appStore = useAppStore()

const { t } = useI18n()

const drawer = ref(false)

// Theme color related
const systemTheme = ref(appStore.getTheme.elColorPrimary)

const setSystemTheme = (color: string) => {
  setCssVar('--el-color-primary', color)
  appStore.setTheme({ elColorPrimary: color })
  const leftMenuBgColor = useCssVar('--left-menu-bg-color', document.documentElement)
  setMenuTheme(trim(unref(leftMenuBgColor) as string))
}

// Header theme related
const headerTheme = ref(appStore.getTheme.topHeaderBgColor || '')

const setHeaderTheme = (color: string) => {
  appStore.setHeaderTheme(color)
}

// Menu theme related
const menuTheme = ref(appStore.getTheme.leftMenuBgColor || '')

const setMenuTheme = (color: string) => {
  appStore.setMenuTheme(color)
}

// Monitor layout changes and reset some theme colors
// watch(
//   () => layout.value,
//   (n) => {
//     if (n === 'top' && !appStore.getIsDark) {
//       headerTheme.value = '#fff'
//       setHeaderTheme('#fff')
//     } else {
//       setMenuTheme(unref(menuTheme))
//     }
//   }
// )

// copy
const copyConfig = async () => {
  const { copy, copied, isSupported } = useClipboard({
    source: `
      // bread crumbs
      breadcrumb: ${appStore.getBreadcrumb},
      // Breadcrumbs icon
      breadcrumbIcon: ${appStore.getBreadcrumbIcon},
      // fold icon
      hamburger: ${appStore.getHamburger},
      // full screen icon
      screenfull: ${appStore.getScreenfull},
      // size icon
      size: ${appStore.getSize},
      // Multilingual icon
      locale: ${appStore.getLocale},
      // tab page
      tagsView: ${appStore.getTagsView},
      // tab icon
      getTagsViewIcon: ${appStore.getTagsViewIcon},
      // logo
      logo: ${appStore.getLogo},
      // menu accordion
      uniqueOpened: ${appStore.getUniqueOpened},
      // Fixed header
      fixedHeader: ${appStore.getFixedHeader},
      // footer
      footer: ${appStore.getFooter},
      // gray mode
      greyMode: ${appStore.getGreyMode},
      // layout layout
      layout: '${appStore.getLayout}',
      // dark mode
      isDark: ${appStore.getIsDark},
      // Component size
      currentSize: '${appStore.getCurrentSize}',
      // Topic related
      theme: {
        // theme color
        elColorPrimary: '${appStore.getTheme.elColorPrimary}',
        // Left menu border color
        leftMenuBorderColor: '${appStore.getTheme.leftMenuBorderColor}',
        // Left menu background color
        leftMenuBgColor: '${appStore.getTheme.leftMenuBgColor}',
        // Left menu light background color
        leftMenuBgLightColor: '${appStore.getTheme.leftMenuBgLightColor}',
        // Select the background color from the left menu
        leftMenuBgActiveColor: '${appStore.getTheme.leftMenuBgActiveColor}',
        // Left menu collapses selected background color
        leftMenuCollapseBgActiveColor: '${appStore.getTheme.leftMenuCollapseBgActiveColor}',
        // Left menu font color
        leftMenuTextColor: '${appStore.getTheme.leftMenuTextColor}',
        // Select the font color from the left menu
        leftMenuTextActiveColor: '${appStore.getTheme.leftMenuTextActiveColor}',
        // logo font color
        logoTitleTextColor: '${appStore.getTheme.logoTitleTextColor}',
        // logo border color
        logoBorderColor: '${appStore.getTheme.logoBorderColor}',
        // Head background color
        topHeaderBgColor: '${appStore.getTheme.topHeaderBgColor}',
        // Header font color
        topHeaderTextColor: '${appStore.getTheme.topHeaderTextColor}',
        // Head hover color
        topHeaderHoverColor: '${appStore.getTheme.topHeaderHoverColor}',
        // Header border color
        topToolBorderColor: '${appStore.getTheme.topToolBorderColor}'
      }
    `,
    legacy: true
  })
  if (!isSupported) {
    ElMessage.error(t('setting.copyFailed'))
  } else {
    await copy()
    if (unref(copied)) {
      ElMessage.success(t('setting.copySuccess'))
    }
  }
}

// Clear cache
const clear = () => {
  storageClear()
  window.location.reload()
}
</script>

<template>
  <div
    :class="prefixCls"
    class="fixed top-[45%] right-0 w-40px h-40px flex items-center justify-center bg-[var(--el-color-primary)] cursor-pointer z-10"
    @click="drawer = true"
  >
    <Icon icon="vi-ant-design:setting-outlined" color="#fff" />
  </div>

  <ElDrawer v-model="drawer" direction="rtl" size="350px" :z-index="4000">
    <template #header>
      <span class="text-16px font-700">{{ t('setting.projectSetting') }}</span>
    </template>

    <div class="text-center">
      <!-- theme -->
      <ElDivider>{{ t('setting.theme') }}</ElDivider>
      <ThemeSwitch />

      <!-- layout -->
      <ElDivider>{{ t('setting.layout') }}</ElDivider>
      <LayoutRadioPicker />

      <!-- System theme -->
      <ElDivider>{{ t('setting.systemTheme') }}</ElDivider>
      <ColorRadioPicker
        v-model="systemTheme"
        :schema="[
          '#409eff',
          '#009688',
          '#536dfe',
          '#ff5c93',
          '#ee4f12',
          '#0096c7',
          '#9c27b0',
          '#ff9800'
        ]"
        @change="setSystemTheme"
      />

      <!-- Header theme -->
      <ElDivider>{{ t('setting.headerTheme') }}</ElDivider>
      <ColorRadioPicker
        v-model="headerTheme"
        :schema="[
          '#fff',
          '#151515',
          '#5172dc',
          '#e74c3c',
          '#24292e',
          '#394664',
          '#009688',
          '#383f45'
        ]"
        @change="setHeaderTheme"
      />

      <!-- Menu theme -->
      <ElDivider>{{ t('setting.menuTheme') }}</ElDivider>
      <ColorRadioPicker
        v-model="menuTheme"
        :schema="[
          '#fff',
          '#001529',
          '#212121',
          '#273352',
          '#191b24',
          '#383f45',
          '#001628',
          '#344058'
        ]"
        @change="setMenuTheme"
      />
    </div>

    <!-- Interface display -->
    <ElDivider>{{ t('setting.interfaceDisplay') }}</ElDivider>
    <InterfaceDisplay />

    <ElDivider />
    <div>
      <BaseButton type="primary" class="w-full" @click="copyConfig">{{
        t('setting.copy')
      }}</BaseButton>
    </div>
    <div class="mt-5px">
      <BaseButton type="danger" class="w-full" @click="clear">
        {{ t('setting.clearAndReset') }}
      </BaseButton>
    </div>
  </ElDrawer>
</template>

<style lang="less" scoped>
@prefix-cls: ~'@{adminNamespace}-setting';

.@{prefix-cls} {
  border-radius: 6px 0 0 6px;
}
</style>
