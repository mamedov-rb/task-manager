import React, {Component} from 'react'
import api from '../axios-config'
import {toast} from "react-toastify"

class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {username: '', password: ''}
    }

    handleChange = (event) => {
        this.setState({[event.target.name]: event.target.value})
    }

    performLogin = () => {
        const user = {username: this.state.username, password: this.state.password}
        api.post('/login', user)
            .then(res => {
                const jwt = res.headers.authorization
                if (jwt !== null) {
                    sessionStorage.setItem('jwt', jwt)
                    sessionStorage.setItem('isAuthenticated', 'true')
                    this.props.setAuthenticated(true)
                    window.location.href = "/projects"
                }
            })
            .catch(err => {
                if (err.response) {
                    toast.error(err.response.data, {
                        position: toast.POSITION.TOP_RIGHT
                    })
                } else {
                    toast.error("Something went wrong", {
                        position: toast.POSITION.TOP_RIGHT
                    })
                }
            })
    }

    performLogout = () => {
        sessionStorage.removeItem('jwt')
        sessionStorage.removeItem('isAuthenticated')
        this.props.setAuthenticated(false)
    }

    render() {
        let authenticated = this.props.isAuthenticated;
        return (
            <div className="login-form ui center floated segment">
                {authenticated ?
                    <div className="ui left floated warning basic button" onClick={this.performLogout}>Logout</div> :
                    <div>
                        <div className="field">
                            <div className="ui large icon input">
                                <i className="user icon"/>
                                <input type="text" name="username" placeholder="Username" onChange={this.handleChange}/>
                            </div>
                        </div>
                        <div className="field">
                            <div className="ui large input">
                                <input type="password" name="password" placeholder="Password"
                                       onChange={this.handleChange}/>
                            </div>
                        </div>
                        <button className="ui left floated primary basic button" onClick={this.performLogin}>
                            Login
                        </button>
                    </div>}
            </div>
        )
    }

}

export default Login