<template>
  <header>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <button
        class="navbar-toggler"
        type="button"
        data-toggle="collapse"
        data-target="#navbarColor01"
        aria-controls="navbarColor01"
        aria-expanded="false"
        aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item active">
            <a class="nav-link text-secondary" href="/#/">Home</a>
          </li>
          <li class="nav-item">
            <a class="nav-link text-secondary" href="/#/registration">Register</a>
          </li>
          <li class="nav-item">
            <span v-if="user.username">
              <a class="nav-link text-secondary" href="/logout">Logout</a>
            </span>
            <span v-else>
              <a class="nav-link text-secondary" href="/#/login">Login</a>
            </span>
          </li>
        </ul>

        <div v-if="user.username" class="nav-item text-white font-italic small font-weight-light user-info">
          <span class="text-secondary">Username:</span>
          <span class="text-success">{{ user.username }}</span>
          <br>
          <span class="text-secondary">Roles:</span>
          <span v-for="auth in user.authorities" class="text-white-50" :key="auth.id">{{ auth.authority }}, </span>
        </div>

      </div>
    </nav>
  </header>
</template>

<script>
export default {
  data() {
    return {
      user: {}
    };
  },
  created() {
    this.axios
      .get("/user/current")
      .then(response => (this.user = response.data))
      .catch(error => console.log(error));
  }
};
</script>
  
<style>
.user-info {
    margin-left: 15px;
  }
</style>
