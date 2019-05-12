<template>
  <div class="container-fluid">
    <div class="text-center shadow-lg p-3 mb-4 bg-white rounded logo">
      <h5>Task manager / <span class="text-secondary">Main board</span></h5>
    </div>
    <div class="row mt-3">

      <div class="col-2">
        <div class="col area">
          <p class="card-header text-dark mb-3 text-capitalise text-center">Projects: {{ projects.length }}</p>
          <hr>
          <div class="card mt-2 mb-2">
              <input type="button" value="Add new" onClick="location.replace('/#/add-project')" class="card-header small">
          </div>
          <div v-if="this.projects.length">
            <div v-for="project in projects" :key="project.id" class="card-deck">
              <project :project="project" :showTasks="showTasks" :showProjectDetails="showProjectDetails"></project>
            </div>
          </div>
        </div>
      </div>

      <div class="col-10">
        <div v-if="this.taskIsShown">
          <task-list 
            :plannedTasks="plannedTasks" 
            :inProgresTasks="inProgresTasks"
            :pausedTasks="pausedTasks"
            :doneTasks="doneTasks">
          </task-list>
        </div>
        <div v-if="this.projectDetailsIsShown">
          <project-details 
            :project="project">
          </project-details>
        </div>
        
      </div>

    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      taskIsShown: false,
      projectDetailsIsShown: false,
      projects: [],
      project: null,
      plannedTasks: [],
      inProgresTasks: [],
      pausedTasks: [],
      doneTasks: []
    };
  },
  methods: {
    showProjectDetails: function(project) {
      this.project = project
      this.projectDetailsIsShown = true
      this.taskIsShown = false
    },
    showTasks: function(project) {
      this.projectDetailsIsShown = false
      this.taskIsShown = true
      this.plannedTasks = [];
      this.inProgressTasks = [];
      this.pausedTasks = [];
      this.doneTasks = [];

      project.tasks.forEach(element => {
        if (element.status === "PLANNED") {
          this.plannedTasks.push(element);
        } else if (element.status === "IN_PROGRESS") {
          this.inProgressTasks.push(element);
        } else if (element.status === "PAUSED") {
          this.pausedTasks.push(element);
        } else if (element.status === "DONE") {
          this.doneTasks.push(element);
        }
      })
    }
  },
  created() {
    this.axios
      .get("/project/all-of-user")
      .then(response => {
        let p = response.data;
        if (isJson(p)) {
          this.projects = p;
        }
      })
      .catch(error => console.log(error));
  }
};
function isJson(jsonString) {
  try {
    if (jsonString && typeof jsonString === "object") {
      return true;
    }
  } catch (e) {}
  return false;
}
</script>

<style>
</style>
