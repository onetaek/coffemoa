<script lang="tsx" setup>
import { defineProps, defineEmits, ref, CSSProperties } from 'vue'
import { ElTree } from 'element-plus'

interface TreeProps {
  data: any[]
  treeProps?: Record<string, any>
  width?: string
  height?: string
}
const props = defineProps<TreeProps>()

const emit = defineEmits<{
  (e: 'node-click', nodeData: any): void
  (e: 'node-expand', nodeData: any): void
  (e: 'node-collapse', nodeData: any): void
}>()

const treeContainer = ref<any>(null)
const showTreeMenu = ref(false)
const contextNode = ref<any>(null)

const menuStyle = ref<any>({})

const defaultWidth = '300px'
const defaultHeight = '400px'

// Close menu
const closeTreeMenu = () => {
  showTreeMenu.value = false
  document.removeEventListener('click', closeTreeMenu)
  document.removeEventListener('contextmenu', closeTreeMenu)
}

// Right-click menu event handler function
const openTreeMenu = (event: MouseEvent, data: any, _node: any, _target: HTMLElement) => {
  contextNode.value = data
  if (!treeContainer.value) return

  const containerRect = treeContainer.value.getBoundingClientRect()
  const nodeRect = (event.target as HTMLElement).getBoundingClientRect()

  // Calculate the coordinates of the menu's positioning relative to its parent container
  const top = nodeRect.top - containerRect.top + treeContainer.value.scrollTop
  const left = nodeRect.left - containerRect.left + treeContainer.value.scrollLeft

  menuStyle.value = {
    position: 'absolute',
    top: `${top + 20}px`,
    left: `${left + 20}px`
  }

  showTreeMenu.value = true

  // Click elsewhere or right-click again to close the menu
  document.addEventListener('click', closeTreeMenu)
  document.addEventListener('contextmenu', closeTreeMenu)
}

// Node click event
const handleNodeClick = (data: any) => {
  emit('node-click', data)
  closeTreeMenu()
}

// Node expansion event
const handleNodeExpand = (data: any) => {
  emit('node-expand', data)
  closeTreeMenu()
}

// Node shutdown event
const handleNodeCollapse = (data: any) => {
  emit('node-collapse', data)
  closeTreeMenu()
}

// Compute container style
const containerStyle: CSSProperties = {
  position: 'relative',
  overflow: 'auto',
  width: props.width ?? defaultWidth,
  height: props.height ?? defaultHeight
}
</script>
<template>
  <div class="tree-container" ref="treeContainer" :style="containerStyle">
    <ElTree
      v-bind="treeProps"
      :data="data"
      @node-click="handleNodeClick"
      @node-expand="handleNodeExpand"
      @node-collapse="handleNodeCollapse"
      @node-contextmenu="openTreeMenu"
    >
      <template #default="{ node }">
        <!-- If the user provides slot, the user's content is rendered.ot, then render the user's content -->
        <template v-if="$slots['render-node']">
          <slot name="render-node" :node="node"></slot>
        </template>
        <!-- Otherwise the default node display is used (e.g. using node.label ）-->
        <template v-else>
          <span>{{ node.label }}</span>
        </template>
      </template>
    </ElTree>
    <div class="treeMenu" v-show="showTreeMenu" :style="menuStyle">
      <!-- User passes context-menu slot to customize menu content -->
      <slot name="context-menu" :node="contextNode" :data="contextNode">
        <!-- If the user does not slot, which can give a default contentxt-menu slot, which can give a default content -->
        <div style="padding: 8px">No menu defined</div>
      </slot>
    </div>
    <slot></slot>
  </div>
</template>
<style scoped lang="less">
.treeMenu {
  position: absolute;
  padding: 5px;
  font-size: 14px;
  color: #606266;
  background-color: rgb(255 255 255 / 90%);
  border: 1px solid #dcdcdc;
  border-radius: 5px;
  box-shadow: 0 4px 10px rgb(0 0 0 / 40%);

  /* Remove overflow: hidden; Or try not usvalueng negative top value */

  /* overflow: hidden; */

  &::after {
    position: absolute;

    /* Move the arrow up outside the menu */
    top: -6px;
    left: 50%;
    border-right: 6px solid transparent;
    border-bottom: 6px solid rgb(206 194 194);

    /* Create an upward arrow */
    border-left: 6px solid transparent;
    content: '';
    transform: translateX(-50%);
  }
}
</style>
