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
      component: () => import('pages/PageMain.vue')
    },
    {
      path: '/search-state',
      component: () => import('src/pages/PageSearchState.vue')
    },
    {
      path: '/create-state',
      component: () => import('pages/PageCreateState.vue')
    },
    {
      path: '/search-city',
      component: () => import('src/pages/PageSearchCity.vue')
    },
    {
      path: '/create-city',
      component: () => import('pages/PageCreateCity.vue')
    },
  ]
},
  {
    path: '/login',
    component: () => import('layouts/LoginLayout.vue'),
    children: [
      {
        path: '/login',
        component: () => import('pages/PageLogin.vue')
      }]
  },
  {
    path: '*',
    component: () => import('pages/Error404.vue')
  }
]
export default routes
