import React, {Component} from 'react'
import api from '../axios-config'
import {toast} from "react-toastify"
import Comments from "./Comments"
import {Dropdown} from 'semantic-ui-react'
import Faker from "faker";

class TaskDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            task: {name: '', description: '', status: ''},
            status: '',
            assignTo: ''
        }
    }

    componentDidMount() {
        this.fetchTaskDetails()
    }

    fetchTaskDetails = () => {
        api.get('/task/details/' + this.props.match.params.taskId)
            .then(response => {
                this.setState({task: response.data})
            })
            .catch(err => {
                toast.error(err.message, {
                    position: toast.POSITION.TOP_RIGHT
                })
            })
    }

    changeStatus = (status) => {
        api.put('/task/' + this.props.match.params.taskId + '/status/' + status)
            .then(response => {
                toast.success("Status changed.", {
                    position: toast.POSITION.TOP_RIGHT
                })
            })
            .catch(err => {
                toast.error(err.message, {
                    position: toast.POSITION.TOP_RIGHT
                })
            })
    }

    render() {
        const handleOnChangeStatus = (e, data) => {
            this.changeStatus(data.value)
        }
        const handleOnChangeAssignTo = (e, data) => {
            this.setState({assignTo: data.value})
        }
        const statuses = [
            {key: 'pl', value: 'PLANNED', text: 'PLANNED'},
            {key: 'in', value: 'IN_PROGRESS', text: 'IN_PROGRESS'},
            {key: 'ps', value: 'PAUSED', text: 'PAUSED'},
            {key: 'dn', value: 'DONE', text: 'DONE'},
        ]
        // const usersOptions = this.state.users.map((el) => (
        //     {
        //         key: el.fullName,
        //         text: el.fullName,
        //         value: el.username,
        //         image: {avatar: true, src: Faker.image.avatar()}
        //     }
        // ))
        return (
            <div>
                <div className="container segment">
                    <div className="ui icon message">
                        <i className="sticky note outline icon"/>
                        <div className="content">
                            <div className="header">
                                {this.state.task.name}
                            </div>
                            <p>{this.state.task.description}</p>
                            <p>status: {this.state.task.status}</p>
                            <p>created: {this.state.task.created}</p>
                            <p>changed: {this.state.task.updated}</p>
                        </div>
                    </div>
                    <div className="ui grid">
                        <div className="six wide column">
                            <Comments taskId={this.props.match.params.taskId}/>
                        </div>
                        <div className="ten wide column">
                            <div className="ui segment">
                                <div className="ui form">
                                    <div className="two fields">
                                        <button className="ui negative basic button">
                                            Drop
                                        </button>
                                        <div className="field">
                                            <Dropdown placeholder='Change status'
                                                      value={this.state.status}
                                                      name="status"
                                                      onChange={handleOnChangeStatus}
                                                      selection
                                                      options={statuses}/>
                                        </div>
                                        <div className="field">
                                            {/*<Dropdown placeholder='Assign to'*/}
                                            {/*          value={this.state.assignTo}*/}
                                            {/*          name="assignTo"*/}
                                            {/*          onChange={handleOnChangeAssignTo}*/}
                                            {/*          fluid*/}
                                            {/*          selection*/}
                                            {/*          options={usersOptions}/>*/}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default TaskDetails