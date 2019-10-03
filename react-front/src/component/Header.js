import React, {Component} from 'react'

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
            </div>
        )
    }
}

export default Header