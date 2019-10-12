import React, {Component} from 'react'
import api from '../axios-config'
import {toast} from "react-toastify"
import UsersTable from './UsersTable'
import Task from './Task'
import TaskForm from "./TaskForm";

const PLANNED = 'PLANNED'
const IN_PROGRESS = 'IN_PROGRESS'
const PAUSED = 'PAUSED'
const DONE = 'DONE'

class ProjectDetails extends Component {

    constructor(props) {
        super(props);
        this.state = {
            isMemberOf: false,
            projectDetails: {},
            tasksSize: '',
            users: [{firstName: '', roles: [{id: '', name: ''}]}],
            planned: [], inProgress: [], paused: [], done: []
        }
    }

    componentDidMount() {
        this.fetchProjectDetails()
        this.fetchTasks()
        this.fetchUsers()
        this.isMemberOf()
    }

    fetchTasks = () => {
        api.get('/task/all/by/assignedTo/projectId/' + this.props.match.params.projectId)
            .then(response => {
                this.sortTasks(response.data)
            })
            .catch(err => {
                toast.error(err.message, {
                    position: toast.POSITION.TOP_RIGHT
                })
            })
    }

    addTask = (task) => {
        api.post('/manager/assign/task/to/user', task)
            .then(res => {
                toast.success("Task created.", {
                    position: toast.POSITION.TOP_RIGHT
                })
                this.fetchTasks()
            })
            .catch(err => {
                toast.error(err.message, {
                    position: toast.POSITION.TOP_RIGHT
                })
            })
    }

    fetchProjectDetails = () => {
        api.get('/project/find/' + this.props.match.params.projectId)
            .then(response => {
                this.setState({projectDetails: response.data})
            })
            .catch(err => {
                toast.error(err.message, {
                    position: toast.POSITION.TOP_RIGHT
                })
            })
    }

    fetchUsers = () => {
        api.get('/manager/users/' + this.props.match.params.projectId)
            .then(response => {
                this.setState({users: response.data})
            })
            .catch(err => {
                toast.error(err.message, {
                    position: toast.POSITION.TOP_RIGHT
                })
            })
    }

    isMemberOf = () => {
        api.get('/project/is-member')
            .then(response => {
                this.setState({isMemberOf: Boolean(response.data)})
            })
            .catch(err => {
                toast.error(err.message, {
                    position: toast.POSITION.TOP_RIGHT
                })
            })
    }

    joinToProject = () => {
        api.patch('/manager/assign/yourself/projectId/' + this.props.match.params.projectId)
            .then(response => {
                toast.success("Joined to project.")
                this.fetchUsers()
                this.setState({isMemberOf: true})
            })
            .catch(err => {
                toast.error(err.message, {
                    position: toast.POSITION.TOP_RIGHT
                })
            })
    }

    leaveProject = () => {
        api.patch('/manager/leave/projectId/' + this.props.match.params.projectId)
            .then(response => {
                toast.success("Project was leave.")
                this.fetchUsers()
                this.setState({isMemberOf: false})
            })
            .catch(err => {
                toast.error(err.message, {
                    position: toast.POSITION.TOP_RIGHT
                })
            })
    }

    sortTasks = (tasks) => {
        const planned = []
        const inProgress = []
        const paused = []
        const done = []
        tasks.map((el) => {
            switch (el.status) {
                case PLANNED:
                    planned.push(el)
                    break
                case IN_PROGRESS:
                    inProgress.push(el)
                    break
                case PAUSED:
                    paused.push(el)
                    break
                case DONE:
                    done.push(el)
                    break
            }
        })
        this.setState({planned: planned})
        this.setState({inProgress: inProgress})
        this.setState({paused: paused})
        this.setState({done: done})
        this.setState({tasksSize: tasks.length})
    }

    render() {
        return (
            <div>
                <div className="ui grid">
                    <div className="three wide column">
                        <div className="ui label">
                            <h3>Details</h3>
                        </div>
                        <div className="ui card">
                            <div className="content">
                                <h3>{this.state.projectDetails.name}</h3>
                            </div>
                            <div className="extra content left aligned">
                                Description: <span>{this.state.projectDetails.description}</span><br/>
                                Created: <span>{this.state.projectDetails.created}</span><br/>
                                Updated: <span>{this.state.projectDetails.updated}</span><br/>
                                Start date: <span>{this.state.projectDetails.startDate}</span><br/>
                                Tasks: <span>{this.state.tasksSize}</span>
                            </div>
                        </div>
                        <div className="ui segment">
                            <UsersTable users={this.state.users}/>
                        </div>
                        <div className="ui segment">
                            {!this.state.isMemberOf ?
                                <button className="ui blue basic button center floated" onClick={this.joinToProject}>
                                    Join to project
                                </button> :
                                <button className="ui red basic button center floate" onClick={this.leaveProject}>
                                    Leave project
                                </button>
                            }
                        </div>
                    </div>
                    <div className="thirteen wide column">
                        <div className="ui grid segment">
                            <div className="four wide column">
                                <div className="ui grey label">
                                    <h3>{PLANNED}</h3>
                                </div>
                                <div className="task ui cards">
                                    {this.state.planned.map((el) => {
                                        return (
                                            <Task el={el}/>
                                        )
                                    })}
                                </div>
                            </div>
                            <div className="four wide column">
                                <div className="ui blue label">
                                    <h3>{IN_PROGRESS}</h3>
                                </div>
                                <div className="task ui cards">
                                    {this.state.inProgress.map((el) => {
                                        return (
                                            <Task el={el}/>
                                        )
                                    })}
                                </div>
                            </div>
                            <div className="four wide column">
                                <div className="ui yellow label">
                                    <h3>{PAUSED}</h3>
                                </div>
                                <div className="task ui cards">
                                    {this.state.paused.map((el) => {
                                        return (
                                            <Task el={el}/>
                                        )
                                    })}
                                </div>
                            </div>
                            <div className="four wide column">
                                <div className="ui green label">
                                    <h3>{DONE}</h3>
                                </div>
                                <div className="task ui cards">
                                    {this.state.done.map((el) => {
                                        return (
                                            <Task el={el}/>
                                        )
                                    })}
                                </div>
                            </div>
                        </div>
                        <TaskForm projectId={this.props.match.params.projectId} addTask={this.addTask}
                                  fetchTasks={this.fetchTasks}/>
                    </div>
                </div>
            </div>
        )
    }
}

export default ProjectDetails