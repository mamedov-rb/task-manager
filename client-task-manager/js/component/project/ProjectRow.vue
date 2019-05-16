<template>
    <tr>
        <th scope="row"> {{project.id}} </th>
        <td> {{project.Name}} </td>
        <td> {{project.description}} </td>
        <td> {{project.status}} </td>
        <td> {{project.startDate}} </td>
        <td> {{project.endDate}} </td>
        <td class="text-secondary">
            <span v-for="task in project.tasks">
                <span> {{task.name}} </span><br>
            </span>
        </td>
        <td> {{project.created}} </td>
        <td> {{project.updated}} </td>
    </tr>
</template>

<script>
    export default {
        props: {
            project: {
                type: Object
            }
        }
    }
</script>

<style scoped>

</style>