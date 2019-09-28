import React, {Component} from 'react'
import api from '../axios-config'
import {toast} from "react-toastify"
import UsersTable from './UsersTable'
import Task from './Task'

const PLANNED = 'PLANNED'
const IN_PROGRESS = 'IN_PROGRESS'
const PAUSED = 'PAUSED'
const DONE = 'DONE'

class ProjectDetails extends Component {


    constructor(props) {
        super(props);
        this.state = {projectDetails: {}, tasksSize: '', planned: [], inProgress: [], paused: [], done: []}
    }

    componentDidMount() {
        this.fetchProjectDetails()
        this.fetchTasks()
    }

    fetchTasks = () => {
        api.get('/task/all/projectId/' + this.props.match.params.projectId)
            .then(response => {
                this.sortTasks(response.data)
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
            <div className="ui container segment">
                <div className="ui grid">
                    <div className="four wide column">
                        <div className="ui top label">
                            <h3 style={{marginTop: 4, paddingBottom: 5}}>Details</h3>
                            <div className="ui segment container">
                                {this.state.projectDetails.name}<br/>
                                {this.state.projectDetails.description}<br/>
                                {this.state.projectDetails.created}<br/>
                                {this.state.projectDetails.updated}<br/>
                                {this.state.projectDetails.startDate}<br/>
                                {this.state.projectDetails.endDate}<br/>
                                tasks: {this.state.tasksSize}
                            </div>
                            <div className="ui segment container">
                                <UsersTable/>
                            </div>
                        </div>
                    </div>
                    <div className="twelve wide column">
                        <div className="ui grid">
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
                    </div>
                </div>
            </div>
        )
    }
}

export default ProjectDetails