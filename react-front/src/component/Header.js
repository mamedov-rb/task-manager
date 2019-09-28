import React, {Component} from 'react'
import {Link} from 'react-router-dom'

class Header extends Component {

    render() {
        return (
            <div>
                <h2 className="ui header">
                    <i className="clone outline icon"/>
                    <div className="content">
                        Task manager
                    </div>
                </h2>
                <div className="ui pointing menu">
                    <Link to="/projects" className="item">
                        Agile
                    </Link>
                    <Link to="/login" className="item">
                        Login
                    </Link>
                    <div className="right menu">
                        <div className="item">
                            <div className="ui transparent icon input">
                                <input type="text" placeholder="Search..."/>
                                    <i className="search link icon"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }

}

export default Header