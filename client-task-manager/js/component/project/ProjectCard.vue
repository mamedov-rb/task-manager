<template>
  <div class="card text-white mb-3">
    <div class="card-header text-success text-center text-uppercase bg-secondary">
      {{ project.name }}
    </div>
    <div v-if="project.tasks.length" class="btn btn-light w-75 mt-1 pb-1 text-black-50" style="margin: 0 auto" @click="showAllTasks(project)">
      Tab to show <span class="text-success">{{ project.tasks.length }}</span> tasks
    </div>
    <div v-else>
      <div class="text-black-50 text-center mt-2">No tasks</div>
    </div>
    <div class="ml-3">
      <div class="card-button btn btn-sm btn-outline-secondary text-center m-2" @click="showCurrentProject(project)">
        more
      </div>
      <div class="card-button btn btn-sm btn-outline-success text-center m-2" @click="showEmptyForm(true, project.id)">
        add task
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: [
      'id',
      'project',
      'showAllTasks',
      'showCurrentProject',
      'showEmptyForm'
    ]
};
</script>

<style scoped>
  .card-button {
    width: 80px;
  }
</style>
