<script setup lang="tsx">
import { PropType, ref } from 'vue'
import { Descriptions, DescriptionsSchema } from '@/components/Descriptions'
import { Icon } from '@/components/Icon'
import { ElTag } from 'element-plus'

defineProps({
  currentRow: {
    type: Object as PropType<any>,
    default: () => undefined
  }
})

const renderTag = (enable?: boolean) => {
  return <ElTag type={!enable ? 'danger' : 'success'}>{enable ? 'enable' : 'Disable'}</ElTag>
}

const detailSchema = ref<DescriptionsSchema[]>([
  {
    field: 'type',
    label: 'Menu type',
    span: 24,
    slots: {
      default: (data) => {
        const type = data.type
        return <>{type === 1 ? 'menu' : 'Table of contents'}</>
      }
    }
  },
  {
    field: 'parentName',
    label: 'Parent menu'
  },
  {
    field: 'meta.title',
    label: 'Menu name'
  },
  {
    field: 'component',
    label: 'components',
    slots: {
      default: (data) => {
        const component = data.component
        return <>{component === '#' ? 'top level directory' : component === '##' ? 'subdirectory' : component}</>
      }
    }
  },
  {
    field: 'name',
    label: 'Component name'
  },
  {
    field: 'meta.icon',
    label: 'icon',
    slots: {
      default: (data) => {
        const icon = data.icon
        if (icon) {
          return (
            <>
              <Icon icon={icon} />
            </>
          )
        } else {
          return null
        }
      }
    }
  },
  {
    field: 'path',
    label: 'path'
  },
  {
    field: 'meta.activeMenu',
    label: 'Highlight menu'
  },
  {
    field: 'permissionList',
    label: 'Button permissions',
    span: 24,
    slots: {
      default: (data: any) => (
        <>
          {data?.permissionList?.map((v) => {
            return (
              <ElTag class="mr-1" key={v.value}>
                {v.label}
              </ElTag>
            )
          })}
        </>
      )
    }
  },
  {
    field: 'menuState',
    label: 'Menu status',
    slots: {
      default: (data) => {
        return renderTag(data.menuState)
      }
    }
  },
  {
    field: 'meta.hidden',
    label: 'Whether to hide',
    slots: {
      default: (data) => {
        return renderTag(data.enableHidden)
      }
    }
  },
  {
    field: 'meta.alwaysShow',
    label: 'Whether to always display',
    slots: {
      default: (data) => {
        return renderTag(data.enableDisplay)
      }
    }
  },
  {
    field: 'meta.noCache',
    label: 'Whether to clear cache',
    slots: {
      default: (data) => {
        return renderTag(data.enableCleanCache)
      }
    }
  },
  {
    field: 'meta.breadcrumb',
    label: 'Whether to display breadcrumbs',
    slots: {
      default: (data) => {
        return renderTag(data.enableShowCrumb)
      }
    }
  },
  {
    field: 'meta.affix',
    label: 'Whether to pin the tab',
    slots: {
      default: (data) => {
        return renderTag(data.enablePinnedTab)
      }
    }
  },
  {
    field: 'meta.noTagsView',
    label: 'Whether to hide tabs',
    slots: {
      default: (data) => {
        return renderTag(data.enableHiddenTab)
      }
    }
  },
  {
    field: 'meta.canTo',
    label: 'Is it possible to jump',
    slots: {
      default: (data) => {
        return renderTag(data.enableSkip)
      }
    }
  }
])
</script>

<template>
  <Descriptions :schema="detailSchema" :data="currentRow || {}" />
</template>
