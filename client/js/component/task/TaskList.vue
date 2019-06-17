<template>
    <div style="width: 90%; margin: 0 auto;">
        <div class="text-center shadow-lg p-3 mb-4 bg-white rounded logo">
            <h5>Task manager / <span class="text-secondary">All tasks</span></h5>
        </div>
        <div v-if="user.username && isAdmin && tasks.length">
            <table class="table small">
                <thead class="thead-light">
                <tr>
                    <th scope="col">id</th>
                    <th scope="col">Name</th>
                    <th scope="col">Description</th>
                    <th scope="col">Status</th>
                    <th scope="col">Start</th>
                    <th scope="col">End</th>
                    <th scope="col">Created</th>
                    <th scope="col">Updated</th>
                </tr>
                </thead>
                <tbody v-for="task in tasks">
                    <task-row :task="task"></task-row>
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
                tasks: []
            }
        },
        created: function () {
            this.axios
                .get("/task/all")
                .then(response => {
                    this.tasks = response.data
                })
                .catch(error => console.log(error))
        }
    }
</script>

<style>

</style>