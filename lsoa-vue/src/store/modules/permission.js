import { constantRoutes } from '@/router';
// import { getroutes } from '@/api/permission'
import Layout from '@/layout';
import EmptyLayout from '@/layout/EmptyLayout';
/**
 * 循环生成异步路由
 * @Author   JSDINGCHI-LKS
 * @DateTime 2024-04-16
 * @param    {[type]}    routes [description]
 * @return   {[type]}           [description]
 */
function loopCreateRouter(routes) {
    const res = [];
    routes.forEach((route) => {
        const tmp = { ...route };
        const tpl = Object.assign({}, tmp);
        if (typeof tmp['component'] !== 'undefined' && tmp['component']) {
            if (tmp['component'] === 'Layout') {
                tpl['component'] = Layout;
            } else if (tmp['component'] === 'EmptyLayout') {
                tpl['component'] = EmptyLayout;
            } else {
                tpl['component'] = (resolve) => require([`../../${tmp['component']}.vue`], resolve);
            }
        }
        if (tmp['children']) {
            tpl['children'] = loopCreateRouter(tmp['children']);
        }
        res.push(tpl);
    });
    return res;
}

const state = {
    routes: [],
    second_routes: [],
    third_routes: [],
};

const mutations = {
    SET_ROUTES: (state, routes) => {
        state.routes = routes;
    },
    SET_SECOND_ROUTES: (state, routes) => {
        state.second_routes = routes;
    },
    SET_THIRD_ROUTES: (state, routes) => {
        state.third_routes = routes;
    },
};

const actions = {
    generateRoutes({ commit }) {
        return new Promise((resolve, reject) => {
            // getroutes({useType:1}).then(response => {
            //   const { data } = response
            //   console.log(data)
            let ids = [];
            //   treeForeach(data, node => { ids.push(node.id) });
            //   localStorage.setItem('ids',JSON.stringify(ids));
            const newroutes = [
                {
                    path: '/',
                    component: 'Layout',
                    redirect: '/caseManage',
                    name: 'Home',
                    id: 1,
                    // meta: { title: '案件管理' },
                    children: [
                        {
                            path: '/caseManage',
                            id: 11,
                            name: 'CaseManage',
                            component: 'views/caseManage/index',
                            meta: { title: '案件管理', icon: 'icon_1' },
                        },
                    ],
                },
                {
                    path: '/invoiceManage',
                    component: 'Layout',
                    redirect: '/invoiceManage',
                    name: 'menu',
                    id: 2,
                    // meta: { title: '开票管理' },
                    children: [
                        {
                            path: '/invoiceManage',
                            id: 21,
                            name: 'invoiceManage',
                            component: 'views/invoiceManage/index',
                            meta: { title: '开票管理', icon: 'icon_2' },
                        },
                    ],
                },
                {
                    path: '/letterManage',
                    component: 'Layout',
                    redirect: '/letterManage',
                    name: 'letterManage',
                    id: 3,
                    // meta: { title: '开函管理' },
                    children: [
                        {
                            path: '/letterManage',
                            id: 31,
                            name: 'letterManage',
                            component: 'views/letterManage/index',
                            meta: { title: '开函管理', icon: 'icon_3' },
                        },
                    ],
                },
                {
                    path: '/statistics',
                    component: 'Layout',
                    redirect: '/statistics',
                    name: 'statistics',
                    id: 4,
                    // meta: { title: '统计分析', icon: 'icon_4' },
                    children: [
                        {
                            path: '/statistics',
                            id: 41,
                            name: 'statistics',
                            component: 'views/statistics/index',
                            meta: { title: '统计分析', icon: 'icon_4' },
                        },
                    ],
                },
                {
                    path: '/agreementCustomerIndex',
                    component: 'Layout',
                    redirect: '/agreementCustomerIndex',
                    name: 'agreementCustomerIndex',
                    id: 4,
                    children: [
                        {
                            path: '/agreementCustomerIndex',
                            id: 41,
                            name: 'agreementCustomerIndex',
                            component: 'views/agreementCustomer/index',
                            meta: { title: '顾问单位', icon: 'icon_6' },
                        },
                    ],
                },
                // 非诉案件
                {
                    path: '/noLitigationIndex',
                    component: 'Layout',
                    redirect: '/noLitigationIndex',
                    name: 'noLitigationIndex',
                    id: 4,
                    children: [
                        {
                            path: '/noLitigationIndex',
                            id: 41,
                            name: 'noLitigationIndex',
                            component: 'views/noLitigation/index',
                            meta: { title: '非诉业务', icon: 'icon_7' },
                        },
                    ],
                },
                {
                    path: '/sysManage',
                    component: 'Layout',
                    redirect: '/sysManage',
                    name: 'sysManage',
                    id: 5,
                    meta: { title: '系统管理', icon: 'icon_5' },
                    children: [
                        {
                            path: '/deptManagementIndex',
                            id: 51,
                            name: 'deptManagementIndex',
                            component: 'views/sysManage/dept/index',
                            meta: { title: '组织管理' },
                        },
                        {
                            path: '/RoleIndex',
                            id: 52,
                            name: 'RoleIndex',
                            component: 'views/sysManage/role/index',
                            meta: { title: '角色管理' },
                        },
                        {
                            path: '/UserIndex',
                            id: 53,
                            name: 'UserIndex',
                            component: 'views/sysManage/user/index',
                            meta: { title: '用户管理' },
                        },
                        {
                            path: '/attachmentManage',
                            id: 54,
                            name: 'attachmentManage',
                            component: 'views/sysManage/attachmentManage/index',
                            meta: { title: '附件管理' },
                        },
                        {
                            path: '/processConfig',
                            id: 55,
                            name: 'processConfig',
                            component: 'views/sysManage/processConfig/index',
                            meta: { title: '流程配置'},
                        },
                        
                    ],
                },
            ];
            const newarry = newroutes;
            for (let i = 0; i < newarry.length; i++) {
                // newarry[i].children = newarry[i].children
                newarry[i].redirect = newarry[i].children[0].path;
            }
            // console.log(newarry)
            const asyncRouterMap = newarry;
            const accessedRoutes = loopCreateRouter(asyncRouterMap);
            // accessedRoutes.push({ path: '*', redirect: '/404', hidden: true })
            commit('SET_ROUTES', constantRoutes.concat(accessedRoutes));
            resolve(accessedRoutes);
            // }).catch(error => {
            //   reject(error)
            // })
        });
    },
    changeSecondRoutes({ commit, state }, data) {
        commit('SET_SECOND_ROUTES', []);
        const second_routes = state.routes.find((item) => {
            return (
                !item.hidden && (item.path === data.path || (data.path === '' && item.path === '/'))
            );
        });
        if (typeof second_routes !== 'undefined' && second_routes.children) {
            const res = second_routes.children.filter((item) => {
                return !item.hidden && item.path;
            });
            if (res.length > 1) {
                commit('SET_SECOND_ROUTES', res);
            }
        }
    },
    changeThirdRoutes({ commit, state }, data) {
        commit('SET_THIRD_ROUTES', []);
        const third_routes = state.second_routes.find((item) => {
            return (
                !item.hidden && (item.path === data.path || (data.path === '' && item.path === '/'))
            );
        });
        if (typeof third_routes !== 'undefined' && third_routes.children) {
            const res = third_routes.children.filter((item) => {
                return !item.hidden && item.path;
            });
            if (res.length > 1) {
                commit('SET_THIRD_ROUTES', res);
            }
        }
    },
};

export default {
    namespaced: true,
    state,
    mutations,
    actions,
};
