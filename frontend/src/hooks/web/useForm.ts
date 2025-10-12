import type { Form, FormExpose } from '@/components/Form'
import type { ElForm, ElFormItem } from 'element-plus'
import { ref, unref, nextTick } from 'vue'
import { FormSchema, FormSetProps, FormProps } from '@/components/Form'
import { isEmptyVal, isObject } from '@/utils/is'

export const useForm = () => {
  // From instance
  const formRef = ref<typeof Form & FormExpose>()

  // ElFormInstance
  const elFormRef = ref<ComponentRef<typeof ElForm>>()

  /**
   * @param ref Form instance
   * @param elRef ElFormInstance
   */
  const register = (ref: typeof Form & FormExpose, elRef: ComponentRef<typeof ElForm>) => {
    formRef.value = ref
    elFormRef.value = elRef
  }

  const getForm = async () => {
    await nextTick()
    const form = unref(formRef)
    if (!form) {
      console.error('The form is not registered. Please use the register method to register')
    }
    return form
  }

  // some built-in methods
  const methods = {
    /**
     * @description Set the props of the form component
     * @param props props of form component
     */
    setProps: async (props: FormProps = {}) => {
      const form = await getForm()
      form?.setProps(props)
      if (props.model) {
        form?.setValues(props.model)
      }
    },

    /**
     * @description Set form value
     * @param data Data that needs to be set
     */
    setValues: async (data: Recordable) => {
      const form = await getForm()
      form?.setValues(data)
    },

    /**
     * @description Set schema
     * @param schemaProps schemaProps that need to be set
     */
    setSchema: async (schemaProps: FormSetProps[]) => {
      const form = await getForm()
      form?.setSchema(schemaProps)
    },

    /**
     * @description Add schema
     * @param formSchema Need to add new data
     * @param index where to add
     */
    addSchema: async (formSchema: FormSchema, index?: number) => {
      const form = await getForm()
      form?.addSchema(formSchema, index)
    },

    /**
     * @description Delete schema
     * @param field Which data to delete
     */
    delSchema: async (field: string) => {
      const form = await getForm()
      form?.delSchema(field)
    },

    /**
     * @description Get form data
     * @returns form data
     */
    getFormData: async <T = Recordable>(filterEmptyVal = true): Promise<T> => {
      const form = await getForm()
      const model = form?.formModel as any
      if (filterEmptyVal) {
        // Use reduce to filter out null values ​​and return a new object
        return Object.keys(model).reduce((prev, next) => {
          const value = model[next]
          if (!isEmptyVal(value)) {
            if (isObject(value)) {
              if (Object.keys(value).length > 0) {
                prev[next] = value
              }
            } else {
              prev[next] = value
            }
          }
          return prev
        }, {}) as T
      } else {
        return model as T
      }
    },

    /**
     * @description Get an instance of a form component
     * @param field Form item unique identifier
     * @returns component instance
     */
    getComponentExpose: async (field: string) => {
      const form = await getForm()
      return form?.getComponentExpose(field)
    },

    /**
     * @description Get an instance of the formItem component
     * @param field Form item unique identifier
     * @returns formItem instance
     */
    getFormItemExpose: async (field: string) => {
      const form = await getForm()
      return form?.getFormItemExpose(field) as ComponentRef<typeof ElFormItem>
    },

    /**
     * @description Get an instance of the ElForm component
     * @returns ElForm instance
     */
    getElFormExpose: async () => {
      await getForm()
      return unref(elFormRef)
    },

    getFormExpose: async () => {
      await getForm()
      return unref(formRef)
    }
  }

  return {
    formRegister: register,
    formMethods: methods
  }
}
