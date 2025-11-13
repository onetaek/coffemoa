<script setup lang="tsx">
import { Icon } from '@/components/Icon'
import { Tree } from '@/components/Tree'
import { ContentWrap } from '@/components/ContentWrap'
import { useI18n } from 'vue-i18n'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ref } from 'vue'

const { t } = useI18n()
const treeData = ref([
  {
    id: 1,
    name: 'Beijing',
    children: [
      {
        id: 5,
        name: 'Chaoyang',
        children: [
          {
            id: 17,
            name: 'twin towers',
            children: []
          },
          {
            id: 18,
            name: 'dragon city',
            children: []
          }
        ]
      },
      {
        id: 6,
        name: 'Fengtai',
        children: [
          {
            id: 19,
            name: 'new village',
            children: []
          },
          {
            id: 20,
            name: 'Dahongmen',
            children: []
          },
          {
            id: 21,
            name: 'Changxindian',
            children: [
              {
                id: 22,
                name: 'dongshanshan',
                children: []
              },
              {
                id: 23,
                name: 'Beiguan',
                children: []
              },
              {
                id: 24,
                name: 'Guangmingli',
                children: []
              },
              {
                id: 25,
                name: 'Zhao Xindian',
                children: []
              },
              {
                id: 26,
                name: 'Xifeng Temple',
                children: []
              }
            ]
          }
        ]
      },
      {
        id: 7,
        name: 'Haidian',
        children: []
      },
      {
        id: 8,
        name: 'Fangshan',
        children: []
      },
      {
        id: 10,
        name: 'Shunyi',
        children: []
      }
    ]
  },
  {
    id: 2,
    name: 'Shanghai',
    children: [
      {
        id: 11,
        name: 'Huangpu',
        children: []
      },
      {
        id: 12,
        name: 'Xuhui',
        children: []
      }
    ]
  },
  {
    id: 3,
    name: 'Guangzhou',
    children: [
      {
        id: 13,
        name: 'Liwan',
        children: []
      },
      {
        id: 14,
        name: 'Baiyun',
        children: []
      },
      {
        id: 15,
        name: 'Yuexiu',
        children: []
      },
      {
        id: 16,
        name: 'Nansha',
        children: []
      }
    ]
  }
])

const handleNodeClick = (data: any) => {
  console.log('Node clicked:', data)
}

const addOrg = (node: any) => {
  ElMessageBox.prompt('Please enter a group name', 'Add subgroup', {
    confirmButtonText: 'Sure',
    cancelButtonText: 'Cancel',
    inputPattern: /\S/,
    inputErrorMessage: 'Group name cannot be empty'
  }).then(({ value }) => {
    node.children.push({
      id: node.children.length + 1,
      name: value,
      children: []
    })
    ElMessage.success('Added successfully')
  })
}
const editOrg = (node: any) => {
  ElMessageBox.prompt('Please enter a new group name', 'Modify group name', {
    confirmButtonText: 'Sure',
    cancelButtonText: 'Cancel',
    inputValue: node.name,
    inputPattern: /\S/,
    inputErrorMessage: 'Group name cannot be empty'
  }).then(({ value }) => {
    node.name = value
    ElMessage.success('Modification successful')
  })
}

const deleteOrg = (node: any) => {
  ElMessageBox.confirm(`delete [${node.name}] Groups, lower-level subgroups <br>Continue?`, 'Prompt', {
    dangerouslyUseHTMLString: true,
    confirmButtonText: 'Sure',
    cancelButtonText: 'Cancel',
    type: 'warning',
    center: true
  }).then(() => {
    const id = node.id
    // Find the corresponding node in treeData and delete it
    const deleteNode = (data: any) => {
      for (let i = 0; i < data.length; i++) {
        if (data[i].id === id) {
          data.splice(i, 1)
          return
        }
        if (data[i].children) {
          deleteNode(data[i].children)
        }
      }
    }
    deleteNode(treeData.value)
    ElMessage.success('Delete successfully')
  })
}
</script>

<template>
  <ContentWrap :title="t('treeDemo.treeTitle')" :message="t('qrcodeDemo.qrcodeDes')">
    <Tree
      :data="treeData"
      :tree-props="{
        highlightCurrent: true,
        nodeKey: 'id',
        props: {
          children: 'children',
          label: 'name'
        }
      }"
      width="300px"
      height="400px"
      @node-click="handleNodeClick"
    >
      <!-- Customize right-click menu -->
      <template #context-menu="{ node }">
        <div class="menuItem" @click="addOrg(node)">
          <Icon icon="ep:plus" style="color: #1e9fff" />
          <span>Add subgroup</span>
        </div>
        <div class="menuItem" @click="editOrg(node)">
          <Icon icon="ep:edit-pen" style="color: #1e9fff" />
          Modify group name
        </div>
        <div class="menuItem" @click="deleteOrg(node)">
          <Icon icon="ep:delete" style="color: #1e9fff" />
          Delete groups and subgroups
        </div>
      </template>

      <!-- Custom node display -->
      <!-- <template #render-node="{ node }">
      <span v-if="node.isLeaf">[FILE] {{ node.label }}</span>
      <span v-else>[FOLDER] {{ node.label }}</span>
    </template> -->
    </Tree>
  </ContentWrap>
</template>
<style lang="less" scoped>
.menuItem {
  display: flex;
  padding: 2px 10px;
  text-align: left;
  box-sizing: border-box;
  align-items: center; /* Center vertically */
  gap: 5px; /* The spacing between icons and text can be adjusted as needed */
}

.menuItem:hover {
  cursor: pointer;
  background-color: #eee;
}
</style>
