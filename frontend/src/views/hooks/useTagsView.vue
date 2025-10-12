<script setup lang="ts">
import { ContentWrap } from '@/components/ContentWrap'
import { useTagsView } from '@/hooks/web/useTagsView'
import { useRouter } from 'vue-router'

const { push } = useRouter()

const { closeAll, closeLeft, closeRight, closeOther, closeCurrent, refreshPage, setTitle } =
  useTagsView()

const closeAllTabs = () => {
  closeAll(() => {
    push('/dashboard/analysis')
  })
}

const closeLeftTabs = () => {
  closeLeft()
}

const closeRightTabs = () => {
  closeRight()
}

const closeOtherTabs = () => {
  closeOther()
}

const refresh = () => {
  refreshPage()
}

const closeCurrentTab = () => {
  closeCurrent(undefined, () => {
    push('/dashboard/analysis')
  })
}

const setTabTitle = () => {
  setTitle(new Date().getTime().toString())
}

const setAnalysisTitle = () => {
  setTitle(`Analysis page-${new Date().getTime().toString()}`, '/dashboard/analysis')
}
</script>

<template>
  <ContentWrap title="useTagsView">
    <BaseButton type="primary" @click="closeAllTabs"> Close all tabs </BaseButton>
    <BaseButton type="primary" @click="closeLeftTabs"> Close left tab </BaseButton>
    <BaseButton type="primary" @click="closeRightTabs"> Close the tab on the right </BaseButton>
    <BaseButton type="primary" @click="closeOtherTabs"> Close other tabs </BaseButton>
    <BaseButton type="primary" @click="closeCurrentTab"> Close current tab </BaseButton>
    <BaseButton type="primary" @click="refresh"> Refresh the current tab page </BaseButton>
    <BaseButton type="primary" @click="setTabTitle"> Modify current title </BaseButton>
    <BaseButton type="primary" @click="setAnalysisTitle"> Modify analysis page title </BaseButton>
  </ContentWrap>
</template>
