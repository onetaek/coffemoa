<script setup lang="tsx">
import { ContentWrap } from '@/components/ContentWrap'
import { ref, unref } from 'vue'
import { ElDivider, ElRow, ElCol } from 'element-plus'
import { hasPermi } from '@/components/Permission'

const permission = ref('add')

setTimeout(() => {
  permission.value = 'view'
}, 3000)
</script>

<template>
  <ContentWrap>
    <ElDivider>Component mode judgment (has been globally registered and supports dynamic modification)</ElDivider>
    <ElRow :gutter="20">
      <ElCol :span="8">
        Add new permissions:
        <Permission permission="add">
          <BaseButton type="primary"> Add </BaseButton>
        </Permission>
      </ElCol>
      <ElCol :span="8">
        Delete permissions:
        <Permission permission="delete">
          <BaseButton type="danger"> Delete </BaseButton>
        </Permission>
      </ElCol>
      <ElCol :span="8">
        Switch viewing permissions after 3 seconds:
        <Permission :permission="permission">
          <BaseButton type="primary"> View </BaseButton>
        </Permission>
      </ElCol>
    </ElRow>

    <ElDivider>Judgment of command mode (has been globally registered and does not support dynamic modification)</ElDivider>
    <ElRow :gutter="20">
      <ElCol :span="8">
        Add new permissions:
        <BaseButton v-hasPermi="'add'" type="primary"> Add </BaseButton>
      </ElCol>
      <ElCol :span="8">
        Delete permissions:
        <BaseButton v-hasPermi="'delete'" type="danger"> Delete </BaseButton>
      </ElCol>
      <ElCol :span="8">
        Switch viewing permissions after 3 seconds (cannot be modified dynamically):
        <BaseButton v-hasPermi="permission" type="primary"> View </BaseButton>
      </ElCol>
    </ElRow>

    <ElDivider>Functional judgment</ElDivider>
    <ElRow :gutter="20">
      <ElCol :span="8">
        Add new permissions:
        <BaseButton v-if="hasPermi('add')" type="primary"> Add </BaseButton>
      </ElCol>
      <ElCol :span="8">
        Delete permissions:
        <BaseButton v-if="hasPermi('delete')" type="danger"> Delete </BaseButton>
      </ElCol>
      <ElCol :span="8">
        Switch viewing permissions after 3 seconds:
        <BaseButton v-if="hasPermi(unref(permission))" type="primary"> View </BaseButton>
      </ElCol>
    </ElRow>
  </ContentWrap>
</template>
