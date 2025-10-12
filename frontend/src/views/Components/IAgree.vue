<script setup lang="ts">
import { ContentWrap } from '@/components/ContentWrap'
import { useI18n } from '@/hooks/web/useI18n'
import { IAgree } from '@/components/IAgree'

const { t } = useI18n()
</script>

<template>
  <ContentWrap :title="t('router.iAgree')">
    <IAgree
      :link="[
        {
          text: 'Privacy Policy',
          url: 'https://www.baidu.com'
        }
      ]"
      text="I agree to the Privacy Policy"
    />
  </ContentWrap>
</template>
