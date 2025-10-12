<script setup lang="ts">
import { ContentWrap } from '@/components/ContentWrap'
import { JsonEditor } from '@/components/JsonEditor'
import { useI18n } from '@/hooks/web/useI18n'
import { ref, watch } from 'vue'

const { t } = useI18n()

const defaultData = ref({
  title: 'title',
  content: 'content'
})

watch(
  () => defaultData.value,
  (val) => {
    console.log(val)
  },
  {
    deep: true
  }
)

setTimeout(() => {
  defaultData.value = {
    title: 'async header',
    content: 'Asynchronous content'
  }
}, 4000)
</script>

<template>
  <ContentWrap :title="t('richText.jsonEditor')" :message="t('richText.jsonEditorDes')">
    <JsonEditor v-model="defaultData" />
  </ContentWrap>
</template>
