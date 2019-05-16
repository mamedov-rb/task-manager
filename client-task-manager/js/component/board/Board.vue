<template>
    <div class="container-fluid">
        <div class="text-center shadow-lg p-3 mb-4 bg-white rounded logo">
            <h5>Task manager / <span class="text-secondary">Main board</span></h5>
        </div>
        <div class="row mt-3 mb-lg-5">

            <div class="col-2">
                <div v-if="user.username" class="col area">
                    <p class="card-header text-dark mb-3 text-capitalise text-center">Projects: {{ projects.length }}</p>
                    <hr>
                    <div class="w-100 mt-1 mb-3 btn btn-outline-secondary" @click="showEmptyForm(false)">
                        <div>new project</div>
                    </div>
                    <div v-if="projects.length">
                        <div v-for="project in projects" :key="project.id" class="card-deck">
                            <project-card :project="project"
                                     :showAllTasks="showAllTasks"
                                     :showCurrentProject="showCurrentProject"
                                     :showEmptyForm="showEmptyForm">
                            </project-card>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-10">
                <div v-if="tasksWillBeShown">
                    <task-cards
                            :plannedTasks="plannedTasks"
                            :inProgressTasks="inProgressTasks"
                            :pausedTasks="pausedTasks"
                            :doneTasks="doneTasks">
                    </task-cards>
                </div>
                <div v-if="currentProjectWillBeShown">
                    <project-details
                            :project="project">
                    </project-details>
                </div>
                <div v-if="taskWillBeCreated">
                    <project-details
                            :projectId="projectId"
                            :taskWillBeCreated="taskWillBeCreated">
                    </project-details>
                </div>
                <div v-if="projectWillBeCreated">
                    <project-details></project-details>
                </div>
                <div v-if="!user.username">
                    <h5 class="text-secondary">Nothing to show. Please <a href="/#/login">log in</a></h5>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
    export default {
        props: {
            user: {
                type: Object
            }
        },
        data() {
            return {
                projectId: '',

                tasksWillBeShown: false,
                currentProjectWillBeShown: false,
                taskWillBeCreated: false,
                projectWillBeCreated: false,

                projects: [],
                project: {},
                plannedTasks: [],
                inProgressTasks: [],
                pausedTasks: [],
                doneTasks: []
            };
        },
        methods: {
            showEmptyForm: function (decision, id) {
                this.projectId = id

                this.taskWillBeCreated = decision
                this.projectWillBeCreated = !decision
                this.currentProjectWillBeShown = false
                this.tasksWillBeShown = false

                // console.log('--showEmptyForm--')
                // console.log(this.projectWillBeCreated)
                // console.log(this.taskWillBeCreated)
                // console.log(this.tasksWillBeShown)
                // console.log(this.currentProjectWillBeShown)
                // console.log('--end--')
            },
            showCurrentProject: function (project) {
                this.project = project
                this.currentProjectWillBeShown = true
                this.tasksWillBeShown = false
                this.projectWillBeCreated = false
                this.taskWillBeCreated = false

                // console.log('--showCurrentProject--')
                // console.log(this.tasksWillBeShown)
                // console.log(this.taskWillBeCreated)
                // console.log(this.projectWillBeCreated)
                // console.log(this.currentProjectWillBeShown)
                // console.log('--end--')
            },
            showAllTasks: function (project) {
                this.tasksWillBeShown = true
                this.projectWillBeCreated = false
                this.taskWillBeCreated = false
                this.currentProjectWillBeShown = false

                // console.log('--showAllTasks--')
                // console.log(this.tasksWillBeShown)
                // console.log(this.taskWillBeCreated)
                // console.log(this.projectWillBeCreated)
                // console.log(this.currentProjectWillBeShown)
                // console.log('--end--')

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
        } catch (e) {
        }
        return false;
    }
</script>

<style>
</style>
