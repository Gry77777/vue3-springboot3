import { createRouter, createWebHistory } from 'vue-router'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login',
    },
    {
      path: '/manager',  component: () => import('../views/Manager.vue'),
      children:[
          {path: 'home',
          name: 'home',
            meta:{
              title:'首页'
            },
        component: () => import('../views/Home.vue')
      },
        {
          path: 'test',
          name: 'test',
          meta:{
            title:'测试页面'
          },
          component: () => import('../views/Test.vue')
        },    {
          path: 'data',
          name: 'data',
          meta:{
            title:'数据展示页面'
          },
          component: () => import('../views/Data.vue')
        },
          {
            path: 'employee',
            name: '',
            meta:{
              title:'员工信息'
            },
            component: () => import('../views/Employee.vue')
          },
        {
          path: 'admin',
          name: 'Admin',
          meta:{
            title:'管理员信息'
          },
          component: () => import('../views/Admin.vue')
        },
        {
          path: 'person',
          name: '个人中心',
          meta:{
            title:'个人信息'
          },
          component: () => import('../views/Person.vue')
        },
        {
          path: 'password',
          name: '',
          meta:{
            title:'修改密码'
          },
          component: () => import('../views/Password.vue')
        },
        {
          path: 'article',
          name: '',
          meta:{
            title:'文章信息'
          },
          component: () => import('../views/Article.vue')
        },
        {
          path: 'department',
          name: '',
          meta:{
            title:'部门信息'
          },
          component: () => import('../views/Department.vue')
        }
        ]
    },//子路由的前面不能写斜杠
        {
          path: '/login',
          name: 'Login',
          meta:{
            title:'登录页面'
          },
          component: () => import('../views/Login.vue')
        },
    {
      path: '/register',
      name: 'Register',
      meta:{
        title:'注册页面'
      },
      component: () => import('../views/Register.vue')
    },
    {
      path: '/404',
      name: 'notfound',
      meta:{
        title:'404找不到页面'
      },
      component: () => import('../views/404.vue')
    },
    {
      path: '/:pathMatch(.*)*',redirect:'/404'
    }//定义404页面。



    // {path: '/home',
    //   name: 'home',
    //   component: () => import('../views/Home.vue')
    // },
    // {
    //   path: '/test',
    //   name: 'test',
    //   component: () => import('../views/Test.vue')
    // }
    //name不能重复
  ],

})
router.beforeEach((to,from,next)=>{
  document.title=to.meta.title
  next()//必须调用
})


export default router
