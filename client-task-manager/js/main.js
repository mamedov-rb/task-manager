import Vue from 'vue'
import router from 'router/router'
import BootstrapVue from 'bootstrap-vue'
import VueAxios from 'vue-axios'
import axios from 'axios/axios-config'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

import App from 'App.vue'

import Header from 'component/Header.vue'
import Footer from 'component/Footer.vue'

import Project from 'component/board/project/Project.vue'
import ProjectDetails from 'component/board/project/ProjectDetails.vue'

import TaskPlanned from 'component/board/task/TaskPlanned.vue'
import TaskInProgress from 'component/board/task/TaskInProgress.vue'
import TaskPause from 'component/board/task/TaskPause.vue'
import TaskDone from 'component/board/task/TaskDone.vue'
import TaskList from 'component/board/task/TaskList.vue'

Vue.component('app-header', Header)
Vue.component('app-footer', Footer)
Vue.component('project', Project)
Vue.component('project-details', ProjectDetails)
Vue.component('task-planned', TaskPlanned)
Vue.component('task-inProgress', TaskInProgress)
Vue.component('task-pause', TaskPause)
Vue.component('task-done', TaskDone)
Vue.component('task-list', TaskList)
Vue.use(BootstrapVue)
Vue.use(VueAxios, axios)

new Vue({
  el: '#app',
  router,
  render: a => a(App)
})
