import React, {Component} from 'react'
import Project from './Project'
import api from "../axios-config";
import {toast} from "react-toastify";

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

    render() {
        return (
            <div className="ui cards segment container">
                {this.state.projects.map((el) => {
                    return (
                        <Project el={el} />
                    )
                })}
            </div>
        )
    }

}

export default Projects