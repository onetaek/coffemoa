import { ref, unref, nextTick } from 'vue'
import { FormSchema, FormSetProps } from '@/components/Form'
import { SearchExpose, SearchProps } from '@/components/Search'

export const useSearch = () => {
  // Search instance
  const searchRef = ref<SearchExpose>()

  /**
   * @param ref Search instance
   * @param elRef ElFormInstance
   */
  const register = (ref: SearchExpose) => {
    searchRef.value = ref
  }

  const getSearch = async () => {
    await nextTick()
    const search = unref(searchRef)
    if (!search) {
      console.error('The Search is not registered. Please use the register method to register')
    }
    return search
  }

  // some built-in methods
  const methods = {
    /**
     * @description Set the props of the search component
     * @param field FormItem field
     */
    setProps: async (props: SearchProps = {}) => {
      const search = await getSearch()
      search?.setProps(props)
      if (props.model) {
        search?.setValues(props.model)
      }
    },

    /**
     * @description Set form value
     * @param data Data that needs to be set
     */
    setValues: async (data: Recordable) => {
      const search = await getSearch()
      search?.setValues(data)
    },

    /**
     * @description Set schema
     * @param schemaProps schemaProps that need to be set
     */
    setSchema: async (schemaProps: FormSetProps[]) => {
      const search = await getSearch()
      search?.setSchema(schemaProps)
    },

    /**
     * @description Add schema
     * @param formSchema Need to add new data
     * @param index where to add
     */
    addSchema: async (formSchema: FormSchema, index?: number) => {
      const search = await getSearch()
      search?.addSchema(formSchema, index)
    },

    /**
     * @description Delete schema
     * @param field Which data to delete
     */
    delSchema: async (field: string) => {
      const search = await getSearch()
      search?.delSchema(field)
    },

    /**
     * @description Get form data
     * @returns form data
     */
    getFormData: async <T = Recordable>(): Promise<T> => {
      const search = await getSearch()
      return search?.getFormData() as T
    }
  }

  return {
    searchRegister: register,
    searchMethods: methods
  }
}
