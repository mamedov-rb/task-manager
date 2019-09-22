import React, {Component} from 'react'
import Faker from 'faker'
import api from '../axios-config'

class AgilePage extends Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        this.fetchFavors()
    }

    fetchProjects = () => {
        api.get('/favor/all')
            .then(response => {
                this.setState({favors: response.data})
            })
            .catch(err => {
                toast.error(err.message, {
                    position: toast.POSITION.TOP_RIGHT
                })
            })
    }
}