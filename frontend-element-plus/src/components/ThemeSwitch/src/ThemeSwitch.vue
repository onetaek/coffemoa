<script setup lang="ts">
import { computed } from 'vue'
import { useAppStore } from '@/store/modules/app'
import { ElSwitch } from 'element-plus'
import { useIcon } from '@/hooks/web/useIcon'
import { useDesign } from '@/hooks/web/useDesign'
import { getCssVar } from '@/utils'

const { getPrefixCls } = useDesign()

const emit = defineEmits(['change'])

const prefixCls = getPrefixCls('theme-switch')

const Sun = useIcon({ icon: 'vi-emojione-monotone:sun', color: '#fde047' })

const CrescentMoon = useIcon({ icon: 'vi-emojione-monotone:crescent-moon', color: '#fde047' })

const appStore = useAppStore()

// Initialize to get whether it is a dark theme
const isDark = computed({
  get() {
    return appStore.getIsDark
  },
  set(val: boolean) {
    appStore.setIsDark(val)
    const color = getCssVar('--el-bg-color')
    appStore.setMenuTheme(color)
    appStore.setHeaderTheme(color)
    emit('change', val)
  }
})

// Set the background color of switch
const blackColor = 'var(--el-color-black)'
</script>

<template>
  <ElSwitch
    :class="prefixCls"
    v-model="isDark"
    inline-prompt
    :border-color="blackColor"
    :inactive-color="blackColor"
    :active-color="blackColor"
    :active-icon="Sun"
    :inactive-icon="CrescentMoon"
  />
</template>

<style lang="less" scoped>
:deep(.el-switch__core .el-switch__inner .is-icon) {
  overflow: visible;
}
</style>
