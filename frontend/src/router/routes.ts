import { RouteConfig } from 'vue-router'

const routes: RouteConfig[] = [{
  path: '/',
  component: () => import('layouts/MainLayout.vue'),
  children: [
    {
      path: '/',
      redirect: '/main'
    },
    {
      path: '/main',
      component: () => import('src/pages/Main.vue')
    },
    {
      path: '/state',
      component: () => import('src/pages/PageSearchState.vue')
    },
  ]
},
  {
    path: '/login',
    component: () => import('layouts/LoginLayout.vue'),
    children: [
      {
        path: '/login',
        component: () => import('src/pages/Login.vue')
      }]
  },
  {
    path: '*',
    component: () => import('pages/Error404.vue')
  }
]
export default routes
