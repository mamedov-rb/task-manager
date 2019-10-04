import React, {Component} from 'react'
import api from '../axios-config'
import {toast} from "react-toastify"
import TaskForm from "./TaskForm";

class TaskDetails extends Component {

    constructor(props) {
        super(props);
            this.state = {}
    }

    fetchProjectDetails = () => {
        api.get('/project/find/' + this.props.match.params.taskId)
            .then(response => {

            })
            .catch(err => {
                toast.error(err.message, {
                    position: toast.POSITION.TOP_RIGHT
                })
            })
    }

    render() {
        return (
            <div>
                <div className="container segment">
                        {/*<TaskForm taskId={this.props.match.params.taskId} addTask={this.addTask} fetchTasks={this.fetchTasks} />*/}
                </div>
            </div>
        )
    }
}

export default TaskDetails