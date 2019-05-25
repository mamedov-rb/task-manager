<template>
    <div>
        <div class="container">
            <div class="text-center shadow-lg p-3 mb-4 bg-white rounded logo">
                <h5>Task manager / <span class="text-secondary">Main board</span></h5>
            </div>
            <div class="row">
                <div class="col-md-4 order-md-2 mb-4" style="margin-top: 113px;">
                    <h4 class="d-flex justify-content-between align-items-center mb-3">
                        <span class="text-muted">Comments</span>
                        <span class="badge badge-secondary badge-pill">3</span>
                    </h4>
                    <ul class="list-group mb-3">
                        <li class="list-group-item d-flex justify-content-between lh-condensed">
                            <div>
                                <h6 class="my-0">Product name</h6>
                                <small class="text-muted">Brief description</small>
                            </div>
                            <span class="text-muted">$12</span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between lh-condensed">
                            <div>
                                <h6 class="my-0">Second product</h6>
                                <small class="text-muted">Brief description</small>
                            </div>
                            <span class="text-muted">$8</span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between lh-condensed">
                            <div>
                                <h6 class="my-0">Third item</h6>
                                <small class="text-muted">Brief description</small>
                            </div>
                            <span class="text-muted">$5</span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between bg-light">
                            <div class="text-success">
                                <h6 class="my-0">Promo code</h6>
                                <small>EXAMPLECODE</small>
                            </div>
                            <span class="text-success">-$5</span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between">
                            <span>Total (USD)</span>
                            <strong>$20</strong>
                        </li>
                    </ul>

                    <form class="card p-2">
                        <div class="input-group">
                            <input type="text" class="form-control" placeholder="Promo code">
                            <div class="input-group-append">
                                <button type="submit" class="btn btn-secondary">Redeem</button>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-md-8 order-md-1">
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

                        <button @click="patchTask" class="btn btn-outline-primary">Update</button>
                        <button @click="deleteTask" class="btn btn-outline-danger">Delete</button>

                    </form>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        created() {
            console.log(this.$route.params.t)
        },
        data: function () {
            return {
                task: {},
                // id: this.task.id,
                // name: this.task.name,
                // description: this.task.description,
                // status: this.task.status,
                // startDate: this.task.startDate,
                // endDate: this.task.endDate,
                // created: this.task.created,
                // updated: this.task.updated
            }
        },
        methods: {
            deleteTask: function() {
                this.axios
                    .delete('/api/task/delete/' + this.id)
                    .then(response => {
                        document.location.reload(true);
                    })
                    .catch(e => {})
            },
            patchTask: function() {
                this.axios
                    .patch('/api/task/patch', {
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

<style>

</style>