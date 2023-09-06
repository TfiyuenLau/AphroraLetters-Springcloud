export default {
  path: "/article",
  redirect: "/article/index",
  meta: {
    icon: "mdi:book-open-page-variant-outline",
    title: "文章管理",
    rank: 4
  },
  children: [
    {
      path: "/article/index",
      name: "Article",
      component: () => import("@/views/article/index.vue"),
      meta: {
        title: "文章管理",
      }
    }
  ]
} as RouteConfigsTable;
