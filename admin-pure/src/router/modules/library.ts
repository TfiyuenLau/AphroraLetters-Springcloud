export default {
  path: "/library",
  redirect: "/library/index",
  meta: {
    icon: "mdi:library",
    title: "文库管理",
    rank: 4
  },
  children: [
    {
      path: "/library/index",
      name: "Library",
      component: () => import("@/views/library/index.vue"),
      meta: {
        title: "文库管理",
      }
    }
  ]
} as RouteConfigsTable;
