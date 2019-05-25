<template>
    <div style="width: 90%; margin: 0 auto;">
        <div class="text-center shadow-lg p-3 mb-4 bg-white rounded logo">
            <h5>Task manager / <span class="text-secondary">All users</span></h5>
        </div>
        <div v-if="user.username && isAdmin && users.length">
            <table class="table small">
                <thead class="thead-light">
                <tr>
                    <th scope="col">id</th>
                    <th scope="col">Full</th>
                    <th scope="col">Roles</th>
                    <th scope="col">Username</th>
                    <th scope="col">Email</th>
                    <th scope="col">Phone</th>
                    <th scope="col">Age</th>
                    <th scope="col">Registered</th>
                    <th scope="col">Updated</th>
                </tr>
                </thead>
                <tbody v-for="user in users">
                    <user-row :user="user"></user-row>
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
                users: []
            }
        },
        created: function () {
            this.axios
                .get("/api/user/all")
                .then(response => {
                    this.users = response.data
                })
                .catch(error => console.log(error))
        }
    }
</script>

<style>

</style>