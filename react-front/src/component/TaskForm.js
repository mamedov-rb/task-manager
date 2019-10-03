import React, {Component} from 'react'
import SkyLight from 'react-skylight'
import api from "../axios-config";
import {toast} from "react-toastify";
import Faker from "faker";
import {Dropdown} from "semantic-ui-react";

class TaskForm extends Component {
    constructor(props) {
        super(props)
        this.state = {users: [], name: '', description: '', status: '', assignTo: ''}
    }

    componentDidMount() {
        this.fetchUsers()
    }

    fetchUsers = () => {
        api.get('/manager/users/' + this.props.projectId)
            .then(response => {
                this.setState({users: response.data})
            })
            .catch(err => {
                toast.error(err.message, {
                    position: toast.POSITION.TOP_RIGHT
                })
            })
    }

    handleSubmit = (event) => {
        event.preventDefault()
        this.props.addTask({
            projectId: this.props.projectId,
            assignTo: this.state.assignTo,
            task: {
                name: this.state.name,
                description: this.state.description,
                status: this.state.status
            }
        })
        this.refs.addDialog.hide()
    }

    cancelSubmit = (event) => {
        event.preventDefault()
        this.refs.addDialog.hide()
    }

    handleChange = (event) => {
        this.setState(
            {[event.target.name]: event.target.value}
        )
    }

    render() {
        const handleOnChangeAssignTo = (e, data) => {
            this.setState({assignTo: data.value})
        }
        const handleOnChangeStatus = (e, data) => {
            this.setState({status: data.value})
        }
        const usersOptions = this.state.users.map((el) => (
            {
                key: el.fullName,
                text: el.fullName,
                value: el.username,
                image: {avatar: true, src: Faker.image.avatar()}
            }
        ))
        const statuses = [
            {key: 'pl', value: 'PLANNED', text: 'PLANNED'},
            {key: 'in', value: 'IN_PROGRESS', text: 'IN_PROGRESS'},
            {key: 'ps', value: 'PAUSED', text: 'PAUSED'},
            {key: 'dn', value: 'DONE', text: 'DONE'},
        ]
        return (
            <div>
                <SkyLight hideOnOverlayClicked ref="addDialog">
                    <h3>Create task</h3>
                    <form style={{marginTop: 50}}>
                        <div className="ui input">
                            <input type="text" placeholder="Name" name="name" required="true"
                                   onChange={this.handleChange}/><br/>
                        </div>
                        <div className="ui input">
                            <input type="text" placeholder="Description" name="description" required="true"
                                   onChange={this.handleChange}/><br/>
                        </div>
                        <div className="ui input">
                        <Dropdown placeholder='Assign to'
                                  value={this.state.assignTo}
                                  name="assignTo"
                                  onChange={handleOnChangeAssignTo}
                                  fluid
                                  selection
                                  options={usersOptions}/>
                        </div>
                        <div className="ui input">
                            <Dropdown placeholder='STATUS'
                                      value={this.state.status}
                                      name="status"
                                      onChange={handleOnChangeStatus}
                                      fluid
                                      selection
                                      options={statuses}/>
                        </div>
                        <button className="ui positive basic button" onClick={this.handleSubmit}>Submit</button>
                        <button className="ui negative basic button" onClick={this.cancelSubmit}>Cancel</button>
                    </form>
                </SkyLight>
                <div className="ui left aligned segment">
                    <button className="ui positive basic button" onClick={() => this.refs.addDialog.show()}>
                        New task
                    </button>
                </div>
            </div>
        )
    }
}

export default TaskForm