<template>
    <tr>
        <th scope="row"> {{task.id}} </th>
        <td> {{task.Name}} </td>
        <td> {{task.description}} </td>
        <td> {{task.status}} </td>
        <td> {{task.startDate}} </td>
        <td> {{task.endDate}} </td>
        <td> {{task.created}} </td>
        <td> {{task.updated}} </td>
    </tr>
</template>

<script>
    export default {
        props: {
            task: {
                type: Object
            }
        }
    }
</script>

<style>
</style>