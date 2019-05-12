<template>
    <div class="w-75 ml-5">
        <form class="mt-5">
            <div class="form-group">
                <h6 class="text-secondary font-italic">ID: {{ this.project.id }}</h6>
                <h6 class="text-secondary font-italic small">created: {{ this.project.created }}</h6>
                <h6 class="text-secondary font-italic small">updated: {{ this.project.updated }}</h6>
            </div>
            <div class="form-group">
                <label>Name</label>
                <input v-model="name" type="text" class="form-control" placeholder="Provide project name">
            </div>
            <div class="form-group">
                <label>Description</label>
                <input v-model="description" type="text" class="form-control" placeholder="Provide short description">
            </div>
            <div class="form-group">
                <label>Status</label>
                <select v-model="status" class="form-control" name="roles" required>
                    <option value="PLANNED">PLANNED</option>
                    <option value="IN_PROGRESS">IN PROGRESS</option>
                    <option value="PAUSED">PAUSE</option>
                    <option value="DONE">DONE</option>
                </select>
            </div>
            <div class="form-group">
                <label>Start date</label>
                <input v-model="startDate" type="date" class="form-control">
            </div>
            <div class="form-group">
                <label>End date</label>
                <input v-model="endDate" type="date" class="form-control">
            </div>
            <button @click="patchProject" class="btn btn-primary">Update</button>
            <button @click="dropProject" class="btn btn-warning">Drop</button>
            <button @click="deleteProject" class="btn btn-danger">Delete</button>
        </form>
    </div>
</template>

<script>
export default {
    props: [
        'project'
    ],
    data() {
        return {
            id: this.project.id,
            name: this.project.name,
            description: this.project.description,
            status: this.project.status,
            startDate: this.project.startDate,
            endDate: this.project.endDate
        }
    },
    methods: {
        dropProject: function() {
            this.axios
                .put('/user/drop-project/' + this.id)
                .then(response => {})
                .catch(e => {})
        },
        deleteProject: function() {
            this.axios
                .delete('/project/delete/' + this.id)
                .then(response => {})
                .catch(e => {})
        },
        patchProject: function() {
            this.axios
                .patch('/project/patch', {
                    id: this.id,
                    name: this.name,
                    description: this.description,
                    status: this.status,
                    startDate: this.startDate,
                    endDate: this.endDate
                })
            .then(response => {})
            .catch(e => {})
        }
    }
}
</script>

<style>

</style>
