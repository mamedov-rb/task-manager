import React, {Component} from 'react';
import './App.css';
import {BrowserRouter as Router, Link, Route} from 'react-router-dom'
import {ToastContainer} from "react-toastify"
import 'react-toastify/dist/ReactToastify.css'
import Projects from './component/Projects'
import ProjectDetails from './component/ProjectDetails'
import TaskDetails from './component/TaskDetails'
import Header from './component/Header'
import Login from './component/Login'
import Register from './component/Register'

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {isAuthenticated: false}
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

    render() {
        return (
            <div className="App ui center aligned container">
                <Header isAuthenticated={this.state.isAuthenticated}/>
                <Router>
                    <div className="ui top attached tabular menu">
                        <Link to="/projects" className="item">
                            Agile
                        </Link>
                        <Link to="/login" className="item">
                            {this.state.isAuthenticated ? 'Logout' : 'Login'}
                        </Link>
                        <Link to="/register" className="item">
                            Register
                        </Link>
                        <div className="right menu">
                            <div className="item">
                                <div className="ui transparent icon input">
                                    <input type="text" placeholder="Search anything..." />
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
