export interface DescriptionsSchema {
  span?: number // How many points does it account for?
  field: string // Field name
  label?: string // label name
  width?: string | number
  minWidth?: string | number
  align?: 'left' | 'center' | 'right'
  labelAlign?: 'left' | 'center' | 'right'
  className?: string
  labelClassName?: string
  slots?: {
    default?: (...args: any[]) => JSX.Element | null
    label?: (...args: any[]) => JSX.Element | null
  }
}
