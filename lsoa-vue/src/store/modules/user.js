import { login, getInfo, logout } from '@/api/user';
import { getToken, setToken, removeToken } from '@/utils/auth';
import { resetRouter } from '@/router';
import { dialogFactoryInstance } from '@/components/DialogFactory/factory';

const getDefaultState = () => {
    return {
        token: getToken(),
        name: '',
        avatar: '',
        roles: [],
        userInfo: {},
        roleIds: [],
    };
};

const state = getDefaultState();

const mutations = {
    RESET_STATE: (state) => {
        Object.assign(state, getDefaultState());
    },
    SET_TOKEN: (state, token) => {
        state.token = token;
    },
    SET_NAME: (state, name) => {
        state.name = name;
    },
    SET_AVATAR: (state, avatar) => {
        state.avatar = avatar;
    },
    SET_ROLES: (state, roles) => {
        state.roles = roles;
    },
    RESET_USER: (state) => {
        removeToken();
        resetRouter();
        Object.assign(state, getDefaultState());
        // 销毁所有的弹窗实例
        dialogFactoryInstance.destoryAll();
    }
};

const actions = {
    // user login
    login({ commit, dispatch }, userInfo) {
        const { account, password } = userInfo;
        return new Promise((resolve, reject) => {
            login({ account: account.trim(), password: password })
                .then(async (response) => {
                    const { data } = response;
                    commit('SET_TOKEN', data.tokenValue);
                    setToken(data.tokenValue);
                    // const roles = await dispatch('getInfo');
                    resolve();
                })
                .catch((error) => {
                    reject(error);
                });
            // commit('SET_TOKEN', "admin")
            // setToken("admin")
            // resolve()
        });
    },

    // get user info
    getInfo({ commit, state }) {
        return new Promise((resolve, reject) => {
            // const data = {
            //     roles: ['admin'],
            //     introduction: 'I am a super administrator',
            //     avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
            //     name: 'Super Admin',
            // };
            // const { roles, name, avatar, introduction } = data;
            // commit('SET_ROLES', roles);
            // commit('SET_NAME', name);
            // commit('SET_AVATAR', avatar);
            // //commit('SET_INTRODUCTION', introduction)
            // resolve(data);
            getInfo()
                .then((response) => {
                    const { data } = response;
                    if (!data) {
                        return reject('Verification failed, please Login again.');
                    }

                    const { realName, avatar, roleList } = data;
                    localStorage.setItem('user', JSON.stringify(data));
                    state.userInfo = data;
                    state.roleIds = roleList.map(res => res.id)
                    commit('SET_NAME', realName);
                    commit('SET_AVATAR', avatar);
                    commit('SET_ROLES', roleList);
                    resolve(data);
                })
                .catch((error) => {
                    reject(error);
                });
        });
    },

    // user logout
    logout({ commit, state }) {
        return new Promise((resolve, reject) => {
            logout()
                .then(() => {
                    removeToken(); // must remove  token  first
                    resetRouter();
                    commit('RESET_STATE');
                    resolve();
                    // 销毁所有已创建的弹框
                    dialogFactoryInstance.destoryAll();
                })
                .catch((error) => {
                    reject(error);
                });
        });
    },

    // remove token
    resetToken({ commit }) {
        return new Promise((resolve) => {
            removeToken(); // must remove  token  first
            commit('RESET_STATE');
            resolve();
        });
    },
};

export default {
    namespaced: true,
    state,
    mutations,
    actions,
};
