import { createRouter, createWebHistory } from 'vue-router'
import Post from '/src/views/Post.vue'
import Home from '/src/views/Home.vue'
import Account from '/src/views/Account.vue'
import About from '/src/views/About.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/post',
      name: 'post',
      component: Post
    },
    {
      path: '/account',
      name: 'account',
      component: Account
    },
    {
      path: '/about',
      name: 'about',
      component: About
    }
  ]
})

export default router
