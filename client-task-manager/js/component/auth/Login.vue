<template>
    <div class="container">
        <div class="text-center shadow-lg p-3 mb-4 bg-white rounded logo">
            <h5>
                Task manager /
                <span class="text-secondary">Login page</span>
            </h5>
        </div>
        <form class="form-signin" method="POST" action="http://localhost:7575/login">
            <div>
            <span v-if='isError()'>
                <p class="alert alert-danger">Invalid username or password.</p>
            </span>
                <span v-if='isLogout()'>
          <p class="alert alert-info">You have been logged out.</p>
            </span>
            </div>
            <input
                    name="username"
                    type="text"
                    class="form-control"
                    placeholder="Username"
                    required
                    autofocus
            >
            <input
                    name="password"
                    type="password"
                    class="form-control"
                    placeholder="Password"
                    required
            >
            <div class="checkbox">
                <label>
                    <input type="checkbox" name="remember-me"> Remember me
                </label>
            </div>
            <button class="btn btn-lg btn-success btn-block w-50" type="submit">Sign in</button>
        </form>
    </div>
</template>
<script>

    export default {
        data: function () {
            return {
                username: '',
                password: '',
            }
        },
        methods: {
            // login: function () {
            //     this.axios
            //         .post('/login', {
            //             username: this.username,
            //             password: this.password
            //         })
            //         .then(response => {})
            //         .catch(e => {})
            // },
            isLogout: function () {
                return getParam() === 'logout';
            },
            isError: function () {
                return getParam() === 'error';
            }
        }
    };

    function getParam() {
        const url = new URL(window.location.href).toString();
        return url.substring(url.lastIndexOf('?') + 1);
    }
</script>

<style>
    .form-signin {
        max-width: 430px;
        padding: 15px;
        margin: 0 auto;
        margin-top: 10%;
    }

    .form-signin .form-signin-heading,
    .form-signin .checkbox {
        margin-bottom: 10px;
    }

    .form-signin .checkbox {
        font-weight: 400;
    }

    .form-signin .form-control {
        position: relative;
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
        height: auto;
        padding: 10px;
        font-size: 16px;
    }

    .form-signin .form-control:focus {
        z-index: 2;
    }

    .form-signin input[type="email"] {
        margin-bottom: -1px;
        border-bottom-right-radius: 0;
        border-bottom-left-radius: 0;
    }

    .form-signin input[type="password"] {
        margin-bottom: 10px;
        border-top-left-radius: 0;
        border-top-right-radius: 0;
    }
</style>
