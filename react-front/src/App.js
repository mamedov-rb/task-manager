import React, {Component} from 'react';
import './App.css';
import { BrowserRouter as Router, Route} from 'react-router-dom'
import {ToastContainer} from "react-toastify"
import 'react-toastify/dist/ReactToastify.css'
import Projects from './component/Projects'
import ProjectDetails from './component/ProjectDetails'
import Header from './component/Header'
import Login from './component/Login'

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {isAuthenticated: false}
    }

    componentDidMount() {
        let authenticated = Boolean(sessionStorage.getItem('isAuthenticated'));
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
                <Router>
                    <Header isAuthenticated={this.state.isAuthenticated} />
                    <div>
                        <Route exact path="/projects" component={Projects} />
                        <Route path="/project/details/:projectId" component={ProjectDetails} />
                        <Route path="/login" component={() => <Login
                            isAuthenticated={this.state.isAuthenticated}
                            setAuthenticated={this.setAuthenticated} />}
                        />
                    </div>
                    <ToastContainer autoClose={3000}/>
                </Router>
            </div>
        )
    }
}
export default App;
