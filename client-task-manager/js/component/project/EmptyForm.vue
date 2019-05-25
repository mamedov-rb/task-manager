<template>
    <div class="w-75 ml-5">
        <form class="mt-5">
            <div class="form-group">
                <h6 class="text-secondary font-italic">ID: {{ id }}</h6>
                <h6 class="text-secondary font-italic small">created: {{ created }}</h6>
                <h6 class="text-secondary font-italic small">updated: {{ updated }}</h6>
            </div>
            <div class="form-group">
                <label>Name</label>
                <input v-model="name" type="text" class="form-control" placeholder="Provide name" required />
            </div>
            <div class="form-group">
                <label>Description</label>
                <input v-model="description" type="text" class="form-control" placeholder="Provide short description" required />
            </div>
            <div class="form-group">
                <label>Status</label>
                <select v-model="status" class="form-control">
                    <option value="" selected disabled>Provide status</option>
                    <option value="PLANNED">PLANNED</option>
                    <option value="IN_PROGRESS">IN PROGRESS</option>
                    <option value="PAUSED">PAUSED</option>
                    <option value="DONE">DONE</option>
                </select>
            </div>
            <div class="form-group">
                <label>Start date</label>
                <input v-model="startDate" type="date" class="form-control" required />
            </div>
            <div class="form-group">
                <label>End date</label>
                <input v-model="endDate" type="date" class="form-control" required />
            </div>
                <span v-if="id">
                    <button @click="patchProject" class="btn btn-outline-primary">Update</button>
                    <button @click="dropProject" class="btn btn-outline-warning">Drop</button>
                    <button @click="deleteProject" class="btn btn-outline-danger">Delete</button>
                </span>
                <span v-else>
                    <button @click="createProjectOrTask" class="btn btn-outline-success">Create</button>
                </span>
        </form>
    </div>
</template>

<script>
export default {
    props: {
        taskWillBeCreated: {
            type: Boolean
        },
        projectId: {
            type: String
        },
        project: {
            type: Object,
            default: function() {
                return {
                    id: '',
                    name: '',
                    description: '',
                    status: '',
                    startDate: '',
                    endDate: ''
                }
            }
        }
    },
    data: function () {
        return {
            id: this.project.id,
            name: this.project.name,
            description: this.project.description,
            status: this.project.status,
            startDate: this.project.startDate,
            endDate: this.project.endDate,
            created: this.project.created,
            updated: this.project.updated
        }
    },
    methods: {
        createProjectOrTask: function () {
            if (this.taskWillBeCreated) {
                this.axios
                    .post('/api/task/save/' + this.projectId, {
                        name: this.name,
                        description: this.description,
                        startDate: this.startDate,
                        endDate: this.endDate,
                        status: this.status
                    })
                    .then(response => {
                        document.location.reload(true);
                    })
                    .catch(e => {})
            } else {
                this.axios
                    .post('/api/project/save', {
                        name: this.name,
                        description: this.description,
                        startDate: this.startDate,
                        endDate: this.endDate,
                        status: this.status
                    })
                    .then(response => {
                        document.location.reload(true);
                    })
                    .catch(e => {})
            }
        },
        dropProject: function() {
            this.axios
                .put('/api/user/drop-project/' + this.id)
                .then(response => {
                    document.location.reload(true);
                })
                .catch(e => {})
        },
        deleteProject: function() {
            this.axios
                .delete('/api/project/delete/' + this.id)
                .then(response => {
                    document.location.reload(true);
                })
                .catch(e => {})
        },
        patchProject: function() {
            this.axios
                .patch('/api/project/patch', {
                    id: this.id,
                    name: this.name,
                    description: this.description,
                    status: this.status,
                    startDate: this.startDate,
                    endDate: this.endDate
                })
            .then(response => {
                document.location.reload(true);
            })
            .catch(e => {})
        }
    }
}
</script>

<style scoped>
    button {
        width:100px;
    }
</style>
