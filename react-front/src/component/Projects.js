import React, {Component} from 'react'
import Project from './Project'
import api from "../axios-config";
import {toast} from "react-toastify";
import ProjectForm from './ProjectForm'

class Projects extends Component {

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

    addProject = (project) => {
        api.post('/project/save', project)
            .then(res => {
                toast.success("Project created.", {
                    position: toast.POSITION.TOP_RIGHT
                })
                this.fetchProjects()
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
                <div className="ui cards">
                    {this.state.projects.map((el) => {
                        return (
                            <Project el={el} />
                        )
                    })}
                </div>
                <ProjectForm addProject={this.addProject} fetchProjects={this.fetchProjects} />
            </div>
        )
    }

}

export default Projects