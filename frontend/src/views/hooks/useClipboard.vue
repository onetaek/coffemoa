<script setup lang="ts">
import { ContentWrap } from '@/components/ContentWrap'
import { useClipboard } from '@/hooks/web/useClipboard'
import { ElInput } from 'element-plus'
import { ref } from 'vue'

const { copy, copied, text, isSupported } = useClipboard()

const source = ref('')
</script>

<template>
  <ContentWrap title="useClipboard">
    <ElInput v-model="source" placeholder="Please enter the content you want to copy" />
    <div v-if="isSupported">
      <BaseButton @click="copy(source)" type="primary" class="mt-20px">
        <span v-if="!copied">copy</span>
        <span v-else>Copied</span>
      </BaseButton>
      <p>
        Currently copied: <code>{{ text || 'none' }}</code>
      </p>
    </div>
    <p v-else> Your browser does not support Clipboard API </p>
  </ContentWrap>
</template>
