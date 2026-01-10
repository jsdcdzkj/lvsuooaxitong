import router from './router';
import store from './store';
import { Message } from 'element-ui';
import NProgress from 'nprogress'; // progress bar
import 'nprogress/nprogress.css'; // progress bar style
import { getToken, removeToken } from '@/utils/auth'; // get token from cookie
import getPageTitle from '@/utils/get-page-title';

NProgress.configure({ showSpinner: false }); // NProgress Configuration

const whiteList = ['/login']; // no redirect whitelist

// if (window.performance.navigation.type == 1) {console.log("页面被刷新")}else{console.log("首次被加载");removeToken();}

// console.log(window.name)
// //判断是否首次加载，首次加载清除token
// if (window.name == "") {
//   // console.log("首次被加载");
//   // 在首次进入页面时我们给window.name设置一个固定值(firstVisit)
//   window.name = "firstVisit";
//   removeToken();
// } else if (window.name == "firstVisit") {
//   // console.log("页面被刷新");
// }

router.beforeEach(async (to, from, next) => {
    // start progress bar
    NProgress.start();

    // set page title
    document.title = getPageTitle(to.meta.title);

    // determine whether the user has logged in
    const hasToken = getToken();
    // const hasToken = "vue-admin"
    // console.log("hasToken:",hasToken);
    if (hasToken) {
        if (to.path === '/login') {
            // if is logged in, redirect to the home page
            next({ path: '/' });
            NProgress.done();
        } else {
            // determine whether the user has obtained his permission roles through getInfo
            const hasRoles = store.getters.roles && store.getters.roles.length > 0;
            if (hasRoles) {
                next();
            } else {
                try {
                    // get user info
                    // note: roles must be a object array! such as: ['admin'] or ,['developer','editor']
                    //获取用户信息
                    const roles = await store.dispatch('user/getInfo');

                    // generate accessible routes map based on roles
                    const accessRoutes = await store.dispatch('permission/generateRoutes');

                    router.addRoutes(accessRoutes);
                    // hack method to ensure that addRoutes is complete
                    // set the replace: true, so the navigation will not leave a history record
                    next({ ...to, replace: true });
                } catch (error) {
                    // remove token and go to login page to re-login
                    await store.dispatch('user/resetToken');
                    // Message.error(error || 'Has Error')

                    next(`/login?redirect=${to.path}`);
                    NProgress.done();
                }
            }
        }
    } else {
        /* has no token*/

        if (whiteList.indexOf(to.path) !== -1) {
            // in the free login whitelist, go directly
            next();
        } else {
            // other pages that do not have permission to access are redirected to the login page.
            next(`/login?redirect=${to.path}`);
            NProgress.done();
        }
    }
});

router.afterEach(() => {
    // finish progress bar
    NProgress.done();
});
