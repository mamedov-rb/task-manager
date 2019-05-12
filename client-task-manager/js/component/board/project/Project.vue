<template>
  <div class="card text-white mb-2">
    <div class="card-header text-success text-center text-uppercase bg-secondary">
      {{ project.name }}
    </div>
    <div v-if="project.tasks.length" class="btn btn-light w-75 mt-1 pb-1 text-black-50" style="margin: 0 auto" @click="showTasks(project)">
      Tab to show <span class="text-success">{{ project.tasks.length }}</span> tasks
    </div>
    <div v-else>
      <div class="text-black-50 text-center mt-2">No tasks</div>
    </div>
    <div class="btn btn-sm btn-outline-secondary w-25 text-center m-2" @click="showProjectDetails(project)">
      more
    </div>
  </div>
</template>

<script>
export default {
  props: [
      'project',
      'showTasks',
      'showProjectDetails'
    ]
};
</script>

<style>
</style>
