
const routes = [
  {
    path: '/',
    component: 'Layout',
    redirect: '/dashboard',
    name: 'Home',
    id: 2,
    // alwaysShow: true,
    children: [{
      path: '/dashboard',
      id: 2,
      name: 'Dashboard',
      component: 'views/dashboard/index',
      meta: { title: '监控中心' }
    }]
  },
  {
    path: '/warn',
    component: 'Layout',
    redirect: '/warn/realwarn',
    name: 'warn',
    id: 3,
    meta: { title: '告警管理' },
    children: [
      {
        path: '/Warn/RealWarn',
        name: 'RealWarn',
        id: 31,
        component: 'views/Warn/RealWarn/index',
        meta: { title: '实时告警',icon:'form' }
      },
      {
        path: '/warn/historywarn',
        name: 'historywarn',
        id: 32,
        component: 'views/Warn/HistoryWarn/index',
        meta: { title: '已处置告警',icon:'form' }
      }
    ]
  },
  {
    path: '/equipment',
    component: 'Layout',
    redirect: '/equipment/basestation',
    name: 'Equipment',
    id: 4,
    meta: { title: '设备管理' },
    children: [
      {
        path: '/equipment/basestation',
        name: 'basestation',
        id: 41,
        component: 'views/equipment/BaseStation/index',
        meta: { title: '基站管理',icon:'form' }
      },
      {
        path: '/equipment/Bracelet',
        name: 'Bracelet',
        id: 42,
        component: 'views/equipment/Bracelet/index',
        meta: { title: '手环管理',icon:'form' }
      }
    ]
  },
  {
    path: '/prison',
    component: 'Layout',
    redirect: '/prison/area',
    name: 'prison',
    id: 5,
    meta: { title: '监狱管理' },
    children: [
      {
        path: '/prison/area',
        name: 'area',
        id: 51,
        component: 'views/prison/area/index',
        meta: { title: '监区管理',icon:'form' }
      },
      {
        path: '/prison/room',
        name: 'room',
        id: 52,
        component: 'views/prison/room/index',
        meta: { title: '监舍管理',icon:'form' }
      },
      {
        path: '/prison/beds',
        name: 'beds',
        id: 53,
        component: 'views/prison/beds/index',
        meta: { title: '床位管理',icon:'form' }
      }

    ]
  },

  {
    path: '/setting',
    component: 'Layout',
    redirect: '/setting/user',
    name: 'Setting',
    id: 6,
    meta: { title: '系统管理' },
    children: [
      {
        path: '/setting/user',
        name: 'User',
        id: 61,
        component: 'views/systemSet/user/index',
        meta: { title: '用户管理',icon:'form' }
      },
      {
        path: '/setting/role',
        name: 'Role',
        id: 62,
        component: 'views/systemSet/role/index',
        meta: { title: '角色管理',icon:'peoples' }
      },
      {
        path: '/setting/warnset',
        name: 'warnset',
        id: 63,
        component: 'views/systemSet/warnset/index',
        meta: { title: '告警设置',icon:'tree' }
      },
      {
        path: '/setting/person',
        name: 'person',
        id: 64,
        component: 'views/systemSet/userinfo/index',
        meta: { title: '个人中心',icon:'user' }
      }
    ]
  }
]

module.exports = [
  // get routes
  {
    url: './permission/getroutes',
    type: 'get',
    response: () => {
      return {
        code: 20000,
        data: { list: routes }
      }
    }
  }
]
