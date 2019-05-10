<template>
  <div class="container">
      <div class="text-center shadow-lg p-3 mb-4 bg-white rounded logo">
        <h5>Task manager / <span class="text-secondary">Add project</span></h5>
      </div>
    <form class="form-signin" @submit="addProject">

      <div class="form-group">
        <input
          type="text"
          v-model="name"
          class="form-control"
          autofocus="autofocus"
          placeholder="Name"
          required
        >
      </div>
      <div class="form-group">
        <input
          type="text"
          v-model="description"
          class="form-control"
          placeholder="Description"
          required
        >
      </div>
      <div class="form-group">
        <input
          type="date"
          v-model="startDate"
          class="form-control"
          required
        >
      </div>
      <div class="form-group">
        <input
          type="date"
          v-model="endDate"
          class="form-control"
          required
        >
      </div>

      <div class="form-group">
        <div class="row">
          <div class="col-sm-6 col-sm-offset-3">
            <input
              type="submit"
              class="form-control btn btn-success"
              value="Create"
            >
          </div>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  data: {
    name: '',
    description: '',
    startDate: '',
    endDate: ''
  },
  methods: {
    addProject: function () {
      this.axios
        .post('/project/save', {
              name: this.name,
              description: this.description,
              startDate: this.startDate,
              endDate: this.endDate
           })
        .then(response => {
            window.location.replace('/');
          })
        .catch(e => {
          this.errors.push(e)
        })
    }
  }
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
