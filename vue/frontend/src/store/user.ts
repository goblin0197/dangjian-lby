import {StoreOptions} from "vuex";
import ACCESS_ENUM from "@/access/accessEnum";
import {Service} from "../../generated";

export default {
    namespaced: true,
    state: () => ({
        loginUser: {
            userName: "未登录",
            id: 0,
            // userRole: ACCESS_ENUM.NOT_LOGIN,
        },
    }),
    /**
     * action 的第一个参数是一个 context 对象，它包含了一些常用的 Vuex 方法，如 commit、dispatch 和 state 等。
     * 在 context 对象中，state 是作为属性访问的，而不是作为参数传递的。
     */
    actions: {
        async getLoginUser({commit, state}, payload) {
            /**
             * commit 方法用于触发一个 mutation;
             * 它接受两个参数，第一个是 mutation 的名称，第二个是传递给 mutation 的数据（即 payload）。
             * 在 action 中使用 commit 方法时，不需要显式传递 state 参数。
             * state 是作为第二个参数传递给 action 的，而 commit 方法则可以直接使用该参数。
             */
                // 从远程请求获取登录信息
            const res = await Service.getLoginUserUsingGet();
            if (res.code === 0) {
                commit("updateUser", res.data);
            } else {
                commit("updateUser", {
                    ...state.loginUser,
                    userRole: ACCESS_ENUM.NOT_LOGIN,
                });
            }
        },
    },
    /**
     * mutation 的第一个参数则是当前的状态对象 state，它是响应式的数据，当它发生变化时会自动触发视图更新。
     * 在 mutation 中，state 是作为参数传递给方法的(即用于修改 state 的方法)。
     */
    mutations: {
        updateUser(state, payload) {
            state.loginUser = payload;
        },
    },
} as StoreOptions<any>;
