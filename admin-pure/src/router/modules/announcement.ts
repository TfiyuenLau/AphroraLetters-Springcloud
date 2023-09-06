export default {
  path: "/announcement",
  redirect: "/announcement/index",
  meta: {
    icon: "mdi:newspaper-variant-multiple",
    title: "公告编辑",
    rank: 2
  },
  children: [
    {
      path: "/announcement/index",
      name: "Announcement",
      component: () => import("@/views/announcement/index.vue"),
      meta: {
        title: "公告编辑",
      }
    }
  ]
} as RouteConfigsTable;
