import React, {Component} from 'react'
import {ToastContainer, toast} from 'react-toastify'
import Faker from 'faker'
import api from '../axios-config'

class AgilePage extends Component {
    constructor(props) {
        super(props);
        this.state = {projects: []}
    }

    componentDidMount() {
        this.fetchProjects()
    }

    fetchProjects = () => {
        api.get('/project/find/all')
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
            <div className="ui cards">
                {this.state.projects.map((el) => {
                    return (
                        <a className="ui card" href="http://www.dog.com">
                            <div className="content">
                                <div className="header">{el.name}</div>
                                <div className="meta">
                                    <span className="category">Start date: {el.startDate}</span>
                                </div>
                                <div className="description">
                                    <p>{el.description}</p>
                                </div>
                            </div>
                            <div className="extra content">
                                <div className="right floated author">
                                    <img className="ui avatar image" src={Faker.image.avatar()} /> Matt
                                </div>
                            </div>
                        </a>
                    )
                })}
                <ToastContainer autoClose={3000}/>
            </div>
        )
    }
}

export default AgilePage