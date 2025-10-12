import { FormSchema, ComponentNameEnum, CheckboxGroupComponentProps } from '../types'
import { ElCheckbox, ElCheckboxButton } from 'element-plus'
import { defineComponent } from 'vue'

export const useRenderCheckbox = () => {
  const renderCheckboxOptions = (item: FormSchema) => {
    // If there is an alias, use the alias
    const componentProps = item?.componentProps as CheckboxGroupComponentProps
    const valueAlias = componentProps?.props?.value || 'value'
    const labelAlias = componentProps?.props?.label || 'label'
    const disabledAlias = componentProps?.props?.disabled || 'disabled'
    const Com = (
      item.component === ComponentNameEnum.CHECKBOX_GROUP ? ElCheckbox : ElCheckboxButton
    ) as ReturnType<typeof defineComponent>
    return componentProps?.options?.map((option) => {
      const { ...other } = option
      return (
        <Com
          {...other}
          disabled={option[disabledAlias || 'disabled']}
          label={option[labelAlias || 'label']}
          value={option[valueAlias || 'value']}
        ></Com>
      )
    })
  }

  return {
    renderCheckboxOptions
  }
}
