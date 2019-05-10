<template>
  <div class="container">
    <div class="text-center shadow-lg p-3 mb-4 bg-white rounded logo">
        <h5>Task manager / <span class="text-secondary">Registration page</span></h5>
      </div>
    <form class="form-signin" method="POST" action="/login">

      <div class="form-group">
        <select class="form-control" name="roles" required style="height: 50px;">
          <option value>Who you wonna be?</option>
          <option value="ROLE_ADMIN">Admin</option>
          <option value="ROLE_DEVELOPER">Developer</option>
          <option value="ROLE_TESTER">Tester</option>
        </select>
      </div>
      <div class="form-group">
        <input
          type="text"
          name="username"
          class="form-control"
          autofocus="autofocus"
          placeholder="Username"
          required
        >
      </div>
      <div class="form-group">
        <input
          type="password"
          name="password"
          class="form-control"
          placeholder="Password"
          required
        >
      </div>
      <div class="form-group">
        <input
          type="text"
          name="firstName"
          class="form-control"
          placeholder="First name"
          required
        >
      </div>
      <div class="form-group">
        <input
          type="text"
          name="lastName"
          class="form-control"
          placeholder="Last name"
          required
        >
      </div>
      <div class="form-group">
        <input
          type="email"
          name="email"
          class="form-control"
          placeholder="Email"
          required
        >
      </div>
      <div class="form-group">
        <input
          type="text"
          name="phone"
          class="form-control"
          placeholder="Phone number"
          required
        >
      </div>
      <div class="form-group">
        <input type="number" name="age" class="form-control" placeholder="Age" required>
      </div>
      <div class="form-group">
        <div class="row">
          <div class="col-sm-6 col-sm-offset-3">
            <input
              type="submit"
              name="login-submit"
              class="form-control btn btn-success"
              value="Register"
            >
          </div>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
export default {

};
</script>

<style>
.form-signin {
  max-width: 430px;
  padding: 15px;
  margin: 0 auto;
  margin-top: 10%;
}
.form-signin .form-signin-heading,
.form-signin .checkbox {
  margin-bottom: 10px;
}
.form-signin .checkbox {
  font-weight: 400;
}
.form-signin .form-control {
  position: relative;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
  height: auto;
  padding: 10px;
  font-size: 16px;
}
.form-signin .form-control:focus {
  z-index: 2;
}
.form-signin input[type="email"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}
.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}
</style>
