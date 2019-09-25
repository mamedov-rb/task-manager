import React, {Component} from 'react'
import api from '../axios-config'
import Projects from './Projects'
import {toast} from "react-toastify"

class AgilePage extends Component {
    constructor(props) {
        super(props);
        this.state = {projects: []}
    }

    componentDidMount() {
        this.fetchProjects()
    }

    fetchProjects = () => {
        api.get('/project/all')
            .then(response => {
                this.setState({projects: response.data})
            })
            .catch(err => {
                toast.error(err.message, {
                    position: toast.POSITION.TOP_RIGHT
                })
            })
    }

    fetchTasks = () => {
        api.get('/task/all')
            .then(response => {
                this.setState({projects: response.data})
            })
            .catch(err => {
                toast.error(err.message, {
                    position: toast.POSITION.TOP_RIGHT
                })
            })
    }

    render() {
        return (
            <div className="ui container">
                <div className="ui grid">
                    <div className="two wide column">
                        <Projects projects={this.state.projects}/>
                    </div>
                    <div className="fourteen wide column">
                        <div className="ui grid">
                            <div className="four wide column">
                                <div className="ui grey label">
                                    <h3>PLANNED</h3>

                                </div>
                            </div>
                            <div className="four wide column">
                                <div className="ui blue label">
                                    <h3>IN PROGRESS</h3>

                                </div>
                            </div>
                            <div className="four wide column">
                                <div className="ui yellow label">
                                    <h3>REVIEW</h3>

                                </div>
                            </div>
                            <div className="four wide column">
                                <div className="ui green label">
                                    <h3>DONE</h3>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default AgilePage