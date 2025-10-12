<script lang="tsx">
import { PropType, defineComponent, ref, computed, unref, watch, onMounted } from 'vue'
import {
  ElForm,
  ElFormItem,
  ElRow,
  ElCol,
  FormRules,
  ComponentSize
  // FormItemProp
} from 'element-plus'
import { componentMap } from './helper/componentMap'
import { propTypes } from '@/utils/propTypes'
import { getSlot } from '@/utils/tsxHelper'
import {
  setTextPlaceholder,
  setGridProp,
  setComponentProps,
  setItemComponentSlots,
  initModel
} from './helper'
import { useRenderSelect } from './components/useRenderSelect'
import { useRenderRadio } from './components/useRenderRadio'
import { useRenderCheckbox } from './components/useRenderCheckbox'
import { useDesign } from '@/hooks/web/useDesign'
import { findIndex } from '@/utils'
import { get, set } from 'lodash-es'
import { FormProps } from './types'
import {
  FormSchema,
  FormSetProps,
  ComponentNameEnum,
  SelectComponentProps,
  RadioGroupComponentProps,
  CheckboxGroupComponentProps
} from './types'

const { renderSelectOptions } = useRenderSelect()
const { renderRadioOptions } = useRenderRadio()
const { renderCheckboxOptions } = useRenderCheckbox()

const { getPrefixCls } = useDesign()

const prefixCls = getPrefixCls('form')

export default defineComponent({
  name: 'Form',
  props: {
    // Generate Form layout structure array
    schema: {
      type: Array as PropType<FormSchema[]>,
      default: () => []
    },
    // Do you need a grid layout?
    isCol: propTypes.bool.def(true),
    // form data object
    model: {
      type: Object as PropType<any>,
      default: () => ({})
    },
    // Whether to automatically set placeholder
    autoSetPlaceholder: propTypes.bool.def(true),
    // Whether to customize content
    isCustom: propTypes.bool.def(false),
    // Form label width
    labelWidth: propTypes.oneOfType([String, Number]).def('auto'),
    rules: {
      type: Object as PropType<FormRules>,
      default: () => ({})
    },
    labelPosition: propTypes.oneOf(['left', 'right', 'top']).def('right'),
    labelSuffix: propTypes.string.def(''),
    hideRequiredAsterisk: propTypes.bool.def(false),
    requireAsteriskPosition: propTypes.oneOf(['left', 'right']).def('left'),
    showMessage: propTypes.bool.def(true),
    inlineMessage: propTypes.bool.def(false),
    statusIcon: propTypes.bool.def(false),
    validateOnRuleChange: propTypes.bool.def(true),
    size: {
      type: String as PropType<ComponentSize>,
      default: undefined
    },
    disabled: propTypes.bool.def(false),
    scrollToError: propTypes.bool.def(false),
    scrollToErrorOffset: propTypes.oneOfType([Boolean, Object]).def(undefined)
    // onValidate: {
    //   type: Function as PropType<(prop: FormItemProp, isValid: boolean, message: string) => void>,
    //   default: () => {}
    // }
  },
  emits: ['register'],
  setup(props, { slots, expose, emit }) {
    // element form Example
    const elFormRef = ref<ComponentRef<typeof ElForm>>()

    const mergeProps = ref<FormProps>({})

    const getProps = computed(() => {
      const propsObj = { ...props }
      Object.assign(propsObj, unref(mergeProps))
      return propsObj
    })

    // Store form instance
    const formComponents = ref({})

    // Store form-item instances
    const formItemComponents = ref({})

    // form data
    const formModel = ref<Recordable>(props.model)

    onMounted(() => {
      emit('register', unref(elFormRef)?.$parent, unref(elFormRef))
    })

    // Assign values ​​to the form
    const setValues = (data: Recordable = {}) => {
      formModel.value = Object.assign(unref(formModel), data)
    }

    const setProps = (props: FormProps = {}) => {
      mergeProps.value = Object.assign(unref(mergeProps), props)
    }

    const delSchema = (field: string) => {
      const { schema } = unref(getProps)

      const index = findIndex(schema, (v: FormSchema) => v.field === field)
      if (index > -1) {
        schema.splice(index, 1)
      }
    }

    const addSchema = (formSchema: FormSchema, index?: number) => {
      const { schema } = unref(getProps)
      if (index !== void 0) {
        schema.splice(index, 0, formSchema)
        return
      }
      schema.push(formSchema)
    }

    const setSchema = (schemaProps: FormSetProps[]) => {
      const { schema } = unref(getProps)
      for (const v of schema) {
        for (const item of schemaProps) {
          if (v.field === item.field) {
            set(v, item.path, item.value)
          }
        }
      }
    }

    const getOptions = async (fn: Function, item: FormSchema) => {
      const options = await fn()
      if (!options || options.length == 0) return
      setSchema([
        {
          field: item.field,
          path:
            item.component === ComponentNameEnum.TREE_SELECT ||
            item.component === ComponentNameEnum.TRANSFER
              ? 'componentProps.data'
              : 'componentProps.options',
          value: options
        }
      ])
    }

    /**
     * @description: Get form component instance
     * @param filed form fields
     */
    const getComponentExpose = (filed: string) => {
      return unref(formComponents)[filed]
    }

    /**
     * @description: Get formItem instance
     * @param filed form fields
     */
    const getFormItemExpose = (filed: string) => {
      return unref(formItemComponents)[filed]
    }

    const setComponentRefMap = (ref: any, filed: string) => {
      formComponents.value[filed] = ref
    }

    const setFormItemRefMap = (ref: any, filed: string) => {
      formItemComponents.value[filed] = ref
    }

    expose({
      setValues,
      formModel,
      setProps,
      delSchema,
      addSchema,
      setSchema,
      getComponentExpose,
      getFormItemExpose
    })

    // Listen to the form structured array and regenerate formModel
    watch(
      () => unref(getProps).schema,
      (schema = []) => {
        formModel.value = initModel(schema, unref(formModel))
      },
      {
        immediate: true,
        deep: true
      }
    )

    // Render package tags, whether to use grid layout
    const renderWrap = () => {
      const { isCol } = unref(getProps)
      const content = isCol ? (
        <ElRow gutter={20}>{renderFormItemWrap()}</ElRow>
      ) : (
        renderFormItemWrap()
      )
      return content
    }

    // Whether to render el-col
    const renderFormItemWrap = () => {
      // The hidden attribute means hiding and not rendering
      const { schema = [], isCol } = unref(getProps)

      return schema
        .filter((v) => !v.remove)
        .map((item) => {
          // in the case of Divider Component needs to occupy its own line
          const isDivider = item.component === 'Divider'
          const Com = componentMap['Divider'] as ReturnType<typeof defineComponent>
          return isDivider ? (
            <Com {...{ contentPosition: 'left', ...item.componentProps }}>{item?.label}</Com>
          ) : isCol ? (
            // If you need a grid, you need to wrap it ElCol
            <ElCol {...setGridProp(item.colProps)}>{renderFormItem(item)}</ElCol>
          ) : (
            renderFormItem(item)
          )
        })
    }

    // Render formItem
    const renderFormItem = (item: FormSchema) => {
      // If there is optionApi, use optionApi first, and options does not exist or is an empty array.
      if (
        item.optionApi &&
        (!item.componentProps?.options || !item.componentProps?.options.length)
      ) {
        // The interface is automatically called internally and does not affect other renderings.
        getOptions(item.optionApi, item)
      }
      const formItemSlots: Recordable = {
        default: () => {
          if (item?.formItemProps?.slots?.default) {
            return item?.formItemProps?.slots?.default(formModel.value)
          } else {
            const Com = componentMap[item.component as string] as ReturnType<typeof defineComponent>

            const { autoSetPlaceholder } = unref(getProps)

            const componentSlots = (item?.componentProps as any)?.slots || {}
            const slotsMap: Recordable = {
              ...setItemComponentSlots(componentSlots)
            }
            // // If it is a select component and there is no custom template, options will be automatically rendered.
            if (item.component === ComponentNameEnum.SELECT) {
              slotsMap.default = !componentSlots.default
                ? () => renderSelectOptions(item)
                : () => {
                    return componentSlots.default(
                      unref((item?.componentProps as SelectComponentProps)?.options)
                    )
                  }
            }

            // virtual list
            if (item.component === ComponentNameEnum.SELECT_V2 && componentSlots.default) {
              slotsMap.default = ({ item }) => {
                return componentSlots.default(item)
              }
            }

            // Radio group and button styles
            if (
              item.component === ComponentNameEnum.RADIO_GROUP ||
              item.component === ComponentNameEnum.RADIO_BUTTON
            ) {
              slotsMap.default = !componentSlots.default
                ? () => renderRadioOptions(item)
                : () => {
                    return componentSlots.default(
                      unref((item?.componentProps as CheckboxGroupComponentProps)?.options)
                    )
                  }
            }

            // Checkbox groups and button styles
            if (
              item.component === ComponentNameEnum.CHECKBOX_GROUP ||
              item.component === ComponentNameEnum.CHECKBOX_BUTTON
            ) {
              slotsMap.default = !componentSlots.default
                ? () => renderCheckboxOptions(item)
                : () => {
                    return componentSlots.default(
                      unref((item?.componentProps as RadioGroupComponentProps)?.options)
                    )
                  }
            }

            const Comp = () => {
              // If the field is a multi-layer path, it needs to be converted into an object.
              const itemVal = computed({
                get: () => {
                  return get(formModel.value, item.field)
                },
                set: (val) => {
                  set(formModel.value, item.field, val)
                }
              })

              return item.component === ComponentNameEnum.UPLOAD ? (
                <Com
                  vModel:file-list={itemVal.value}
                  ref={(el: any) => setComponentRefMap(el, item.field)}
                  {...(autoSetPlaceholder && setTextPlaceholder(item))}
                  {...setComponentProps(item)}
                  style={
                    item.componentProps?.style || {
                      width: '100%'
                    }
                  }
                >
                  {{ ...slotsMap }}
                </Com>
              ) : (
                <Com
                  vModel={itemVal.value}
                  ref={(el: any) => setComponentRefMap(el, item.field)}
                  {...(autoSetPlaceholder && setTextPlaceholder(item))}
                  {...setComponentProps(item)}
                  style={
                    item.componentProps?.style || {
                      width: '100%'
                    }
                  }
                >
                  {{ ...slotsMap }}
                </Com>
              )
            }

            return <>{Comp()}</>
          }
        }
      }
      if (item?.formItemProps?.slots?.label) {
        formItemSlots.label = (...args: any[]) => {
          return (item?.formItemProps?.slots as any)?.label(...args)
        }
      }
      if (item?.formItemProps?.slots?.error) {
        formItemSlots.error = (...args: any[]) => {
          return (item?.formItemProps?.slots as any)?.error(...args)
        }
      }
      return (
        <ElFormItem
          v-show={!item.hidden}
          ref={(el: any) => setFormItemRefMap(el, item.field)}
          {...(item.formItemProps || {})}
          prop={item.field}
          label={item.label || ''}
        >
          {formItemSlots}
        </ElFormItem>
      )
    }

    // Filter the properties passed into the Form component
    const getFormBindValue = () => {
      // Avoid redundant attributes on tags
      const delKeys = ['schema', 'isCol', 'autoSetPlaceholder', 'isCustom', 'model']
      const props = { ...unref(getProps) }
      for (const key in props) {
        if (delKeys.indexOf(key) !== -1) {
          delete props[key]
        }
      }
      return props as FormProps
    }

    return () => (
      <ElForm
        ref={elFormRef}
        {...getFormBindValue()}
        model={unref(getProps).isCustom ? unref(getProps).model : formModel}
        class={prefixCls}
        // @ts-ignore
        onSubmit={(e: Event) => {
          e.preventDefault()
        }}
      >
        {{
          // If customization is required, nothing will be rendered and default slots will be provided instead
          default: () => {
            const { isCustom } = unref(getProps)
            return isCustom ? getSlot(slots, 'default') : renderWrap()
          }
        }}
      </ElForm>
    )
  }
})
</script>

<style lang="less" scoped>
.@{elNamespace}-form.@{adminNamespace}-form .@{elNamespace}-row {
  margin-right: 0 !important;
  margin-left: 0 !important;
}

.@{elNamespace}-form--inline {
  :deep(.el-form-item__content) {
    & > :first-child {
      min-width: 229.5px;
    }
  }
  .@{elNamespace}-input-number {
    // 229.5px is the minimum width compatible with el-input-number,
    min-width: 229.5px;
  }
}
</style>
