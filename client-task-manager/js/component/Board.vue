<template>
  <div class="container-fluid">
    <div class="text-center shadow-lg p-3 mb-4 bg-white rounded logo">
      <h5>
        Task manager / <span class="text-secondary">Main board</span>
      </h5>
    </div>
    <div class="row mt-3">
      <div class="col-2">
        <div class="col area">
          <p class="card-header text-dark mb-3 text-capitalise text-center">Projects: {{ projects.length }}</p>
          <hr>
          <div v-if="this.projects.length">
            <div v-for="project in projects" :key="project.id" class="card-deck">
              <div class="card text-white mb-2">                  
                <div class="card-header text-success text-center text-uppercase bg-secondary">{{ project.name }}</div>
                <div v-if="project.tasks.length" class="btn btn-light w-75 mt-1 pb-1 text-black-50" style="margin: 0 auto" @click="projectTasks(project)">
                  Tab to show <span class="text-success">{{ project.tasks.length }}</span> tasks
                </div>
                <div v-else>
                  <div class="text-black-50 text-center mt-2">No tasks</div>
                </div>
                <div class="btn btn-sm btn-outline-secondary w-25 text-center m-2" style="" @click="projectDetails(project)">
                  more
                </div>
              </div>
            </div>
            <div class="card mt-2">
              <input type="button" value="Add new" onClick="location.replace('/#/add-project')" class="card-header small" />
            </div>
          </div>
        </div>
      </div>

      <div class="col-10">
        <div class="row">
          <div class="text-center col-sm area border-right">
            <p class="card-header mb-3" style="border: solid 2px #7490bc;">PLANNED</p>
            <hr>
            <div v-if="plannedTasks.length">
              <div v-for="task in plannedTasks" :key="task.id" class="card-deck">
                <div class="card mb-3">
                  <div class="card-header" style="background: #7490bc;">{{ task.name }}</div>
                  <div class="card-body" style="border: solid 2px #7490bc;">
                    <h6 class="card-title">{{ task.status }}</h6>
                    <div>
                      <div class="small card-subtitle">starts: {{ task.startDate }}</div>
                      <div class="small card-subtitle">ends: {{ task.endDate }}</div>
                    </div>
                    <p class="card-text font-italic">{{ task.description }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="text-center col-sm area border-right">
            <p class="card-header mb-3" style="border: solid 2px #eaed65;">IN PROGRESS</p>
            <hr>
            <div v-if="inProgresTasks.length">
              <div v-for="task in inProgresTasks" :key="task.id" class="card-deck">
                <div class="card mb-3">
                  <div class="card-header" style="background: #eaed65;">{{ task.name }}</div>
                  <div class="card-body" style="border: solid 2px #eaed65;">
                    <h6 class="card-title">{{ task.status }}</h6>
                    <div>
                      <div class="small card-subtitle">starts: {{ task.startDate }}</div>
                      <div class="small card-subtitle">ends: {{ task.endDate }}</div>
                    </div>
                    <p class="card-text font-italic">{{ task.description }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="text-center col-sm area border-right">
            <p class="card-header mb-3" style="border: solid 2px #afbcb3;">PAUSED</p>
            <hr>
            <div v-if="pausedTasks.length">
              <div v-for="task in pausedTasks" :key="task.id" class="card-deck">
                <div class="card mb-3">
                  <div class="card-header" style="background: #afbcb3;">{{ task.name }}</div>
                  <div class="card-body" style="border: solid 2px #afbcb3;">
                    <h6 class="card-title">{{ task.status }}</h6>
                    <div>
                      <div class="small card-subtitle">starts: {{ task.startDate }}</div>
                      <div class="small card-subtitle">ends: {{ task.endDate }}</div>
                    </div>
                    <p class="card-text font-italic">{{ task.description }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="text-center col-sm area border-right">
            <p class="card-header mb-3" style="border: solid 2px #7aef9d;">DONE</p>
            <hr>
            <div v-if="doneTasks.length">
              <div v-for="task in doneTasks" :key="task.id" class="card-deck">
                <div class="card mb-3">
                  <div class="card-header" style="background: #7aef9d;">{{ task.name }}</div>
                  <div class="card-body" style="border: solid 2px #7aef9d;">
                    <h6 class="card-title">{{ task.status }}</h6>
                    <div>
                      <div class="small card-subtitle">starts: {{ task.startDate }}</div>
                      <div class="small card-subtitle">ends: {{ task.endDate }}</div>
                    </div>
                    <p class="card-text font-italic">{{ task.description }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      clicks: 0,
      timer: null,
      projects: [],
      plannedTasks: [],
      inProgresTasks: [],
      pausedTasks: [],
      doneTasks: []
    };
  },
  methods: {
    projectDetails: function(project) {
      location.replace('/#/project-details')
    },
    projectTasks: function(project) {
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
      });
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
