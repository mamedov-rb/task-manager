import React, {Component} from 'react';
import './App.css';
import { BrowserRouter as Router, Route} from 'react-router-dom'
import AgilePage from './component/AgilePage'
// import UserList from './component/UserList'
// import CoursePage from './component/CoursePage'
// import HomePage from './component/HomePage'
// import LoginPage from './component/LoginPage';
// import BlogPage from './component/BlogPage'

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
                    {/*<AppHeader isAuthenticated={this.state.isAuthenticated} />*/}
                    <div>
                        <Route exact path="/" component={AgilePage} />
                        {/*<Route path="/users" component={UserList} />*/}
                        {/*<Route path="/course" component={CoursePage} />*/}
                        {/*<Route path="/blog" component={BlogPage} />*/}
                        {/*<Route path="/login" component={() => <LoginPage*/}
                        {/*    isAuthenticated={this.state.isAuthenticated}*/}
                        {/*    setAuthenticated={this.setAuthenticated} />}*/}
                        {/*/>*/}
                    </div>
                </Router>
            </div>
        )
    }
}
export default App;
