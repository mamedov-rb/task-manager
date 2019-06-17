<template>
    <tr>
        <th scope="row"> {{user.id}} </th>
        <td> {{user.fullName}} </td>
        <td>
            <span v-for="role in user.roles">
                <span> {{role.name}} </span><br>
            </span>
        </td>
        <td> {{user.username}} </td>
        <td> {{user.email}} </td>
        <td> {{user.phone}} </td>
        <td> {{user.age}} </td>
        <td> {{user.registered}} </td>
        <td> {{user.updated}} </td>
    </tr>
</template>

<script>
    export default {
        props: {
            user: {
                type: Object
            }
        }
    }
</script>

<style>
</style>