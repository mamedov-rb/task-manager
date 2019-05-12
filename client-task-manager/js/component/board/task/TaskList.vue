<template>
  <div class="row">
    <div class="text-center col-sm area border-right">
      <p class="card-header mb-3" style="border: solid 2px #7490bc;">PLANNED</p>
      <hr>
      <div v-if="plannedTasks.length">
        <div v-for="task in plannedTasks" :key="task.id" class="card-deck">
          <task-planned :task="task"></task-planned>
        </div>
      </div>
    </div>

    <div class="text-center col-sm area border-right">
      <p class="card-header mb-3" style="border: solid 2px #eaed65;">IN PROGRESS</p>
      <hr>
      <div v-if="inProgresTasks.length">
        <div v-for="task in inProgresTasks" :key="task.id" class="card-deck">
          <task-inProgress :task="task"></task-inProgress>
        </div>
      </div>
    </div>

    <div class="text-center col-sm area border-right">
      <p class="card-header mb-3" style="border: solid 2px #afbcb3;">PAUSE</p>
      <hr>
      <div v-if="pausedTasks.length">
        <div v-for="task in pausedTasks" :key="task.id" class="card-deck">
          <task-pause :task="task"></task-pause>
        </div>
      </div>
    </div>

    <div class="text-center col-sm area border-right">
      <p class="card-header mb-3" style="border: solid 2px #7aef9d;">DONE</p>
      <hr>
      <div v-if="doneTasks.length">
        <div v-for="task in doneTasks" :key="task.id" class="card-deck">
          <task-done :task="task"></task-done>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
    props: [
        'plannedTasks',
        'inProgresTasks',
        'pausedTasks',
        'doneTasks'
    ]
};
</script>

<style>
</style>
