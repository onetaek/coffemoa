<script setup lang="ts">
import { onBeforeUnmount, computed, PropType, unref, nextTick, ref, watch, shallowRef } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { IDomEditor, IEditorConfig, i18nChangeLanguage } from '@wangeditor/editor'
import { propTypes } from '@/utils/propTypes'
import { isNumber } from '@/utils/is'
import { ElMessage } from 'element-plus'
import { useLocaleStore } from '@/store/modules/locale'

const localeStore = useLocaleStore()

const currentLocale = computed(() => localeStore.getCurrentLocale)

i18nChangeLanguage(unref(currentLocale).lang)

const props = defineProps({
  editorId: propTypes.string.def('wangeEditor-1'),
  height: propTypes.oneOfType([Number, String]).def('500px'),
  editorConfig: {
    type: Object as PropType<IEditorConfig>,
    default: () => undefined
  },
  modelValue: propTypes.string.def('')
})

const emit = defineEmits(['change', 'update:modelValue'])

// Editor instance, must be used shallowRef
const editorRef = shallowRef<IDomEditor>()

const valueHtml = ref('')

watch(
  () => props.modelValue,
  (val: string) => {
    if (val === unref(valueHtml)) return
    valueHtml.value = val
  }
)

// monitor
watch(
  () => valueHtml.value,
  (val: string) => {
    emit('update:modelValue', val)
  }
)

const handleCreated = (editor: IDomEditor) => {
  editorRef.value = editor
  valueHtml.value = props.modelValue
}

// Editor configuration
const editorConfig = computed((): IEditorConfig => {
  return Object.assign(
    {
      readOnly: false,
      customAlert: (s: string, t: string) => {
        switch (t) {
          case 'success':
            ElMessage.success(s)
            break
          case 'info':
            ElMessage.info(s)
            break
          case 'warning':
            ElMessage.warning(s)
            break
          case 'error':
            ElMessage.error(s)
            break
          default:
            ElMessage.info(s)
            break
        }
      },
      autoFocus: false,
      scroll: true,
      uploadImgShowBase64: true
    },
    props.editorConfig || {}
  )
})

const editorStyle = computed(() => {
  return {
    height: isNumber(props.height) ? `${props.height}px` : props.height
  }
})

// callback function
const handleChange = (editor: IDomEditor) => {
  emit('change', editor)
}

// When the component is destroyed, destroy the editor in time
onBeforeUnmount(() => {
  const editor = unref(editorRef.value)

  // Destroy and remove editor
  editor?.destroy()
})

const getEditorRef = async (): Promise<IDomEditor> => {
  await nextTick()
  return unref(editorRef.value) as IDomEditor
}

defineExpose({
  getEditorRef
})
</script>

<template>
  <div class="border-1 border-solid border-[var(--el-border-color)] z-10">
    <!-- Toolbar -->
    <Toolbar
      :editor="editorRef"
      :editorId="editorId"
      class="border-0 b-b-1 border-solid border-[var(--el-border-color)]"
    />
    <!-- Editor -->
    <Editor
      v-model="valueHtml"
      :editorId="editorId"
      :defaultConfig="editorConfig"
      :style="editorStyle"
      @on-change="handleChange"
      @on-created="handleCreated"
    />
  </div>
</template>

<style src="@wangeditor/editor/dist/css/style.css"></style>
