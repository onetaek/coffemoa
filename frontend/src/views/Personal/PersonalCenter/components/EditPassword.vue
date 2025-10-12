<script setup lang="ts">
import { Form, FormSchema } from '@/components/Form'
import { useForm } from '@/hooks/web/useForm'
import { reactive, ref } from 'vue'
import { useValidator } from '@/hooks/web/useValidator'
import { ElMessage, ElMessageBox, ElDivider } from 'element-plus'

const { required } = useValidator()

const formSchema = reactive<FormSchema[]>([
  {
    field: 'password',
    label: 'Old Password',
    component: 'InputPassword',
    colProps: {
      span: 24
    }
  },
  {
    field: 'newPassword',
    label: 'New Password',
    component: 'InputPassword',
    colProps: {
      span: 24
    },
    componentProps: {
      strength: true
    }
  },
  {
    field: 'newPassword2',
    label: 'Confirm new password',
    component: 'InputPassword',
    colProps: {
      span: 24
    },
    componentProps: {
      strength: true
    }
  }
])

const rules = reactive({
  password: [required()],
  newPassword: [
    required(),
    {
      asyncValidator: async (_, val, callback) => {
        const formData = await getFormData()
        const { newPassword2 } = formData
        if (val !== newPassword2) {
          callback(new Error('The new password is inconsistent with the confirmed new password'))
        } else {
          callback()
        }
      }
    }
  ],
  newPassword2: [
    required(),
    {
      asyncValidator: async (_, val, callback) => {
        const formData = await getFormData()
        const { newPassword } = formData
        if (val !== newPassword) {
          callback(new Error('Confirm that the new password is inconsistent with the new password'))
        } else {
          callback()
        }
      }
    }
  ]
})

const { formRegister, formMethods } = useForm()
const { getFormData, getElFormExpose } = formMethods

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
          // Here you can call the interface to change the password
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
  <BaseButton type="primary" @click="save">Confirm changes</BaseButton>
</template>
