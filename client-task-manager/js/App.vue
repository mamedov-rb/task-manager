<template>
  <div>
    <app-header :user="user" :isAdmin="isAdmin"></app-header>
    <router-view :user="user" :isAdmin="isAdmin"></router-view>
    <app-footer></app-footer>
  </div>
</template>

<script>
export default {
  data: function () {
    return {
      user: {},
      isAdmin: false
    };
  },
  created: function () {
    this.axios
            .get("/api/user/current")
            .then(response => (this.user = response.data))
            .catch(error => console.log(error));
  },
  updated: function () {
    this.user.authorities.forEach(data => {
      if (data.authority === 'ROLE_ADMIN') {
        this.isAdmin = true
      }
    })
  }
}
</script>

<style>
  .logo {
    margin: 0 auto;
    margin-top: 15px;
    max-width: 450px;
    height: 55px;
  }
</style>
