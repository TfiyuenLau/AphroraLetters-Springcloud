import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import ArticleListView from "@/views/ArticleListView.vue";
import AuthorIndexView from "@/views/AuthorIndexView.vue";
import LibraryView from "@/views/LibraryView.vue";
import AssistanceView from "@/views/AsistanceView.vue";
import ArticleView from "@/views/ArticleView.vue";
import ArticleCategory from "@/views/ArticleCategory.vue";
import AnnouncementView from "@/views/AnnouncementView.vue";
import LiteratureView from "@/views/LiteratureView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [ // 路由映射
    {
      path: '/',
      name: 'index',
      component: HomeView
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView
    },
    {
      path: '/announcement/:id',
      name: 'announcement',
      component: AnnouncementView
    },
    {
      path: '/article_list/:page',
      name: 'article_list',
      component: ArticleListView
    },
    {
      path: '/article/:id',
      name: 'article',
      component: ArticleView
    },
    {
      path: '/article_category/:categoryId/:page',
      name: 'article_category',
      component: ArticleCategory
    },
    {
      path: '/library',
      name: 'library',
      component: LibraryView
    },
    {
      path: '/author_index/:id',
      name: 'author_index',
      component: AuthorIndexView
    },
    {
      path: '/literature/:url',
      name: 'literature',
      component: LiteratureView
    },
    {
      path: '/assistance',
      name: 'assistance',
      component: AssistanceView
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('@/views/AboutView.vue')
    }
  ]
})

export default router
