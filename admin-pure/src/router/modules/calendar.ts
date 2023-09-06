export default {
  path: "/calendar",
  redirect: "/calendar/index",
  meta: {
    icon: "mdi:calendar-month",
    title: "日历行程",
    rank: 8
  },
  children: [
    {
      path: "/calendar/index",
      name: "Calendar",
      component: () => import("@/views/calendar/index.vue"),
      meta: {
        title: "日历行程",
      }
    }
  ]
} as RouteConfigsTable;
