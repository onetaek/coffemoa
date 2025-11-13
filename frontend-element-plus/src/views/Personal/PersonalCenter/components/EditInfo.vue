<script lang="ts" setup>
import { FormSchema, Form } from '@/components/Form'
import { useForm } from '@/hooks/web/useForm'
import { useValidator } from '@/hooks/web/useValidator'
import { reactive, ref, watch } from 'vue'
import { ElDivider, ElMessage, ElMessageBox } from 'element-plus'

const props = defineProps({
  userInfo: {
    type: Object,
    default: () => ({})
  }
})

const { required, phone, maxlength, email } = useValidator()

const formSchema = reactive<FormSchema[]>([
  {
    field: 'realName',
    label: 'Nick name',
    component: 'Input',
    colProps: {
      span: 24
    }
  },
  {
    field: 'phoneNumber',
    label: 'phone number',
    component: 'Input',
    colProps: {
      span: 24
    }
  },
  {
    field: 'email',
    label: 'Mail',
    component: 'Input',
    colProps: {
      span: 24
    }
  }
])

const rules = reactive({
  realName: [required(), maxlength(50)],
  phoneNumber: [phone()],
  email: [email()]
})

const { formRegister, formMethods } = useForm()
const { setValues, getElFormExpose } = formMethods

watch(
  () => props.userInfo,
  (value) => {
    setValues(value)
  },
  {
    immediate: true,
    deep: true
  }
)

const saveLoading = ref(false)
const save = async () => {
  const elForm = await getElFormExpose()
  const valid = await elForm?.validate().catch((err) => {
    console.log(err)
  })
  if (valid) {
    ElMessageBox.confirm('Do you want to confirm the modification?', 'hint', {
      confirmButtonText: 'confirm',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
      .then(async () => {
        try {
          saveLoading.value = true
          // Here you can call the modify user information interface
          ElMessage.success('Modification successful')
        } catch (error) {
          console.log(error)
        } finally {
          saveLoading.value = false
        }
      })
      .catch(() => {})
  }
}
</script>

<template>
  <Form :rules="rules" @register="formRegister" :schema="formSchema" />
  <ElDivider />
  <BaseButton type="primary" @click="save">keep</BaseButton>
</template>
