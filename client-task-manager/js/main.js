import Vue from 'vue'
import router from 'router/router'
import BootstrapVue from 'bootstrap-vue'
import VueAxios from 'vue-axios'
import axios from 'axios/axios-config'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

import App from 'App.vue'

import Header from 'component/header-footer/Header.vue'
import Footer from 'component/header-footer/Footer.vue'

import ProjectCard from 'component/project/ProjectCard.vue'
import ProjectDetails from 'component/project/EmptyForm.vue'

import TaskPlanned from 'component/task/TaskPlanned.vue'
import TaskInProgress from 'component/task/TaskInProgress.vue'
import TaskPause from 'component/task/TaskPause.vue'
import TaskDone from 'component/task/TaskDone.vue'
import TaskCards from 'component/task/TaskCards.vue'

import UserRow from 'component/user/UserRow.vue'
import ProjectRow from 'component/project/ProjectRow.vue'
import TaskRow from 'component/task/TaskRow.vue'

Vue.component('app-header', Header)
Vue.component('app-footer', Footer)
Vue.component('project-card', ProjectCard)
Vue.component('project-details', ProjectDetails)
Vue.component('task-planned', TaskPlanned)
Vue.component('task-inProgress', TaskInProgress)
Vue.component('task-pause', TaskPause)
Vue.component('task-done', TaskDone)
Vue.component('task-cards', TaskCards)

Vue.component('user-row', UserRow)
Vue.component('project-row', ProjectRow)
Vue.component('task-row', TaskRow)

Vue.use(BootstrapVue)
Vue.use(VueAxios, axios)

new Vue({
  el: '#app',
  router,
  render: a => a(App)
})
