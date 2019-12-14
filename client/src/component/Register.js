import React, {Component} from 'react'
import api from '../axios-config'
import {toast} from "react-toastify"

class Register extends Component {

    constructor(props) {
        super(props);
        this.state = {
            firstName: '',
            lastName: '',
            phone: '',
            email: '',
            password: ''
        }
    }

    handleChange = (event) => {
        this.setState({[event.target.name]: event.target.value})
    }

    performRegister = () => {
        const user = {
            firstName: this.state.firstName,
            lastName: this.state.lastName,
            phone: this.state.phone,
            email: this.state.email,
            username: this.state.username,
            password: this.state.password
        }
        api.post('/user/save', user)
            .then(res => {
                window.location.href = "/login"
            })
            .catch(err => {
                if (err.response) {
                    toast.error(JSON.stringify(err), {
                        position: toast.POSITION.TOP_RIGHT
                    })
                } else {
                    toast.error("Something went wrong", {
                        position: toast.POSITION.TOP_RIGHT
                    })
                }
            })
    }

    render() {
        return (
            <div className="login-form ui center floated segment">
                <div>
                    <div className="field">
                        <div className="ui input">
                            <input type="text" name="firstName" placeholder="First name" onChange={this.handleChange}/>
                        </div>
                    </div>
                    <div className="field">
                        <div className="ui input">
                            <input type="text" name="lastName" placeholder="Last Name" onChange={this.handleChange}/>
                        </div>
                    </div>
                    <div className="field">
                        <div className="ui large icon input">
                            <i className="phone icon"/>
                            <input type="text" name="phone" placeholder="Phone" onChange={this.handleChange}/>
                        </div>
                    </div>
                    <div className="field">
                        <div className="ui large icon input">
                            <i className="mail icon"/>
                            <input type="email" name="email" placeholder="Email" onChange={this.handleChange}/>
                        </div>
                    </div>
                    <div className="field">
                        <div className="ui large icon input">
                            <i className="user icon"/>
                            <input type="text" name="username" placeholder="Username" onChange={this.handleChange}/>
                        </div>
                    </div>
                    <div className="field">
                        <div className="ui icon large input">
                            <i className="key icon"/>
                            <input type="password" name="password" placeholder="Password"
                                   onChange={this.handleChange}/>
                        </div>
                    </div>
                    <button className="ui left floated primary basic button" onClick={this.performRegister}>
                        Register
                    </button>
                </div>
            </div>
        )
    }

}

export default Register