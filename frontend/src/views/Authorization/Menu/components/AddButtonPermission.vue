<script setup lang="ts">
import { FormSchema, Form } from '@/components/Form'
import { ElDrawer } from 'element-plus'
import { reactive } from 'vue'
import { useForm } from '@/hooks/web/useForm'
import { useValidator } from '@/hooks/web/useValidator'

const modelValue = defineModel<boolean>()

const { required } = useValidator()

const formSchema = reactive<FormSchema[]>([
  {
    field: 'label',
    label: 'label',
    component: 'Input',
    colProps: {
      span: 24
    }
  },
  {
    field: 'value',
    label: 'value',
    component: 'Input',
    colProps: {
      span: 24
    }
  }
])

const { formRegister, formMethods } = useForm()
const { getFormData, getElFormExpose } = formMethods

const emit = defineEmits(['confirm'])

const rules = reactive({
  label: [required()],
  value: [required()]
})

const confirm = async () => {
  const elFormExpose = await getElFormExpose()
  if (!elFormExpose) return
  const valid = await elFormExpose?.validate().catch((err) => {
    console.log(err)
  })
  if (valid) {
    const formData = await getFormData()
    formData.id = Date.now()
    emit('confirm', formData)
    modelValue.value = false
  }
}
</script>

<template>
  <ElDrawer v-model="modelValue" title="Add button permissions">
    <template #default>
      <Form :rules="rules" @register="formRegister" :schema="formSchema" />
    </template>
    <template #footer>
      <div>
        <BaseButton @click="() => (modelValue = false)">cancel</BaseButton>
        <BaseButton type="primary" @click="confirm">confirm</BaseButton>
      </div>
    </template>
  </ElDrawer>
</template>
