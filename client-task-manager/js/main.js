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

Vue.component('app-header', Header)
Vue.component('app-footer', Footer)
Vue.use(BootstrapVue)
Vue.use(VueAxios, axios)

new Vue({
  el: '#app',
  router,
  render: a => a(App)
})
