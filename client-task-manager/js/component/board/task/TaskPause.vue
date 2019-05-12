<template>
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
</template>

<script>
export default {
    props:['task']
}
</script>

<style>

</style>
