<template>
    <div style="width: 90%; margin: 0 auto;">
        <div class="text-center shadow-lg p-3 mb-4 bg-white rounded logo">
            <h5>Task manager / <span class="text-secondary">All projects</span></h5>
        </div>
        <div v-if="user.username && isAdmin && projects.length">
            <table class="table small">
                <thead class="thead-light">
                <tr>
                    <th scope="col">id</th>
                    <th scope="col">Name</th>
                    <th scope="col">Description</th>
                    <th scope="col">Status</th>
                    <th scope="col">Start</th>
                    <th scope="col">End</th>
                    <th scope="col">Tasks</th>
                    <th scope="col">Created</th>
                    <th scope="col">Updated</th>
                </tr>
                </thead>
                <tbody v-for="project in projects">
                    <project-row :project="project"></project-row>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
    export default {
        props: {
            user: {
                type: Object
            },
            isAdmin: {
                type: Boolean
            }
        },
        data: function () {
            return {
                projects: []
            }
        },
        created: function () {
            this.axios
                .get("/project/all")
                .then(response => {
                    this.projects = response.data
                })
                .catch(error => console.log(error))
        }
    }
</script>

<style scoped>
</style>