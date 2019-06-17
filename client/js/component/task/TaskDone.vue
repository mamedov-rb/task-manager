<template>
    <div class="card mb-3" @click="showCurrentTask">
      <div class="card-header" style="background: #7aef9d;">{{ task.name }}</div>
        <div class="card-body" style="border: solid 2px #7aef9d;">
          <h6 class="card-title">{{ task.status }}</h6>
          <div>
            <div class="small card-subtitle">starts: {{ task.startDate }}</div>
            <div class="small card-subtitle">ends: {{ task.endDate }}</div>
          </div>
          <p class="card-text font-italic text-secondary">{{ task.description }}</p>
        </div>
    </div>
</template>

<script>
export default {
  props:[
    'task'
  ],
  methods: {
    showCurrentTask: function () {
      this.$route.params.t = this.task
      document.location.href = '/#/task-current'
    }
  }
}
</script>

<style>

</style>
