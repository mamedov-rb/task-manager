import React, {Component} from 'react'

class Project extends Component {

    showTasks = () => {

    }

    render() {
        return (
            <a className="ui card" onClick={this.showTasks}>
                <div className="content">
                    <div className="header">{this.props.el.name}</div>
                </div>
                <div className="extra content">
                    <span className="category">{this.props.el.description}</span>
                </div>
            </a>
        )
    }

}

export default Project