import Vue from 'vue'
import VueRouter from 'vue-router'

import Board from 'component/board/Board.vue'
import Page_404 from 'component/404/Page_404.vue'
import LoginForm from 'component/auth/Login.vue'
import Registration from 'component/auth/Registration.vue'
import UserList from 'component/user/UsersList.vue'
import ProjectList from 'component/project/ProjectList.vue'
import TaskList from 'component/task/TaskList.vue'
import CurrentTask from 'component/task/TaskCurrent.vue'

Vue.use(VueRouter)

const routes = [
    {path: '*', component: Page_404},
    {path: '/', component: Board},
    {path: '/login', component: LoginForm},
    {path: '/registration', component: Registration},
    {path: '/users', component: UserList},
    {path: '/projects', component: ProjectList},
    {path: '/tasks', component: TaskList},
    {path: '/task-current', component: CurrentTask}
]

export default new VueRouter({
    routes
})