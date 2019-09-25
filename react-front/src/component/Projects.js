import React, {Component} from 'react'
import Project from './Project'

class Projects extends Component {

    render() {
        return (
            <div className="ui segment cards">
                {this.props.projects.map((el) => {
                    return (
                        <Project el={el} />
                    )
                })}
            </div>
        )
    }

}

export default Projects