<script setup lang="ts">
// Head配置
useHead({
  title: "Aphrora Letters | 一个刊载社哲时评的文库社区",
  meta: [{
    name: "description",
    content: "Aphrora Letters是一个分享哲学社科文章的个人社区，我们以致力于推广左翼进步文化为宗旨，刊载哲学社科类社评文章、国内外社会运动报导与其他文体；并对当今各主流学派的学术观点与经典著作进行收录整理。"
  }],
  link: [
    { rel: "icon", type: "image/x-icon", href: "/favicon.ico" }
  ]
})

// SEO Meta优化
useSeoMeta({
  title: "Aphrora Letters | 一个刊载社哲时评的文库社区",
  ogTitle: "一个刊载社哲时评的文库社区",
  description: "Aphrora Letters是一个分享哲学社科文章的个人社区，我们以致力于推广左翼进步文化为宗旨，刊载哲学社科类社评文章、国内外社会运动报导与其他文体；并对当今各主流学派的学术观点与经典著作进行收录整理。",
  ogDescription: "Aphrora Letters是一个分享哲学社科文章的个人社区，我们以致力于推广左翼进步文化为宗旨，刊载哲学社科类社评文章、国内外社会运动报导与其他文体；并对当今各主流学派的学术观点与经典著作进行收录整理。",
  ogImage: "https://aphrora-letters.hk.cpolar.io/img/newlogo.png",
  twitterCard: "summary_large_image",
})
</script>

<template>
  <!-- 配置Antdv4全局主题 -->
  <a-config-provider :theme="{
      token: {
        colorPrimary: '#dc3545',
        colorLink: '#8E354A',
        colorSuccess: '#198754',
        colorWarning: '#ffc107',
      },
    }"
  >
    <NuxtPage/>
  </a-config-provider>
</template>

<style>

</style>
