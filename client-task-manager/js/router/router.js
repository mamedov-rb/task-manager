import Vue from 'vue'
import VueRouter from 'vue-router'

import Board from 'component/Board.vue'
import AddProject from 'component/AddProjectForm.vue'
import ProjectDetails from 'component/ProjectDetails.vue'
import Page_404 from 'component/Page_404.vue'
import LoginForm from 'component/LoginForm.vue'
import RegistrationForm from 'component/RegistrationForm.vue'

Vue.use(VueRouter)

const routes = [
    {path: '*', component: Page_404},
    {path: '/', component: Board},
    {path: '/login', component: LoginForm},
    {path: '/registration', component: RegistrationForm},
    {path: '/add-project', component: AddProject},
    {path: '/project-details', component: ProjectDetails}
]

export default new VueRouter({
    routes
})