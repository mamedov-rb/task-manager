import React, {Component} from 'react';
import './App.css';
import {BrowserRouter as Router, Link, Route} from 'react-router-dom'
import {toast, ToastContainer} from "react-toastify"
import 'react-toastify/dist/ReactToastify.css'
import Projects from './component/Projects'
import ProjectDetails from './component/ProjectDetails'
import TaskDetails from './component/TaskDetails'
import Header from './component/Header'
import Login from './component/Login'
import Register from './component/Register'
import api from "./axios-config";

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isAuthenticated: false,
            param: '',
            searchResult: {
                users: [],
                projects: [],
                tasks: []
            }
        }
    }

    componentDidMount() {
        let authenticated = Boolean(sessionStorage.getItem('isAuthenticated'))
        this.setState({isAuthenticated: authenticated})
    }

    setAuthenticated = (value) => {
        this.setState({isAuthenticated: value})
        // this.forceUpdate()
        window.location.reload()
    }

    performSearch = () => {
        api.get('/manager/search', {params: {param: this.state.param}})
            .then(res => {
                this.setState({searchResult: res.data})
            })
            .catch(err => {
                toast.error(err.response.data, {
                    position: toast.POSITION.TOP_RIGHT
                })
            })
    }

    handleChange = (event) => {
        this.setState({[event.target.name]: event.target.value})
        this.performSearch()
    }

    render() {
        return (
            <div className="App ui center aligned container">
                <Header isAuthenticated={this.state.isAuthenticated}/>
                <Router>
                    <div className="ui top attached tabular menu">
                        {!this.state.isAuthenticated ? '' : <Link to="/projects" className="item">Projects</Link>}
                        <Link to="/login" className="item">
                            {this.state.isAuthenticated ? 'Logout' : 'Login'}
                        </Link>
                        {this.state.isAuthenticated ? '' : <Link to="/register" className="item">Register</Link>}
                        <div className="right menu">
                            <div className="item">
                                <div className="ui transparent icon input">
                                    <input type="text" name="param" placeholder="Search something..." onChange={this.handleChange} />
                                        <i className="search link icon" />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="ui bottom container">
                        <Route exact path="/projects" component={Projects}/>
                        <Route path="/project/details/:projectId" render={(props) =>
                            <ProjectDetails globalStore={this.state.isAuthenticated} {...props} />}
                        />
                        <Route path="/task/details/:taskId" component={TaskDetails}/>
                        <Route path="/register" component={Register}/>
                        <Route path="/login" component={() =>
                            <Login isAuthenticated={this.state.isAuthenticated} setAuthenticated={this.setAuthenticated} />}
                        />
                    </div>
                    <ToastContainer autoClose={3000}/>
                </Router>
            </div>
        )
    }
}

export default App;
