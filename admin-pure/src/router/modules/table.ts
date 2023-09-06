export default {
  path: "/table",
  redirect: '/table/admin',
  meta: {
    icon: "mdi:package-variant-closed-minus",
    title: "综合管理",
    rank: 1
  },
  children: [
    {
      path: "/table/admin",
      name: "Admin",
      component: () => import("@/views/table/admin.vue"),
      meta: {
        icon: "mdi:account-details-outline",
        title: "管理员列表",
      }
    },
    {
      path: "/table/log",
      name: "Log",
      component: () => import("@/views/table/log.vue"),
      meta: {
        icon: "mdi:clipboard-multiple-outline",
        title: "日志列表",
      }
    }
  ]
} as RouteConfigsTable;
