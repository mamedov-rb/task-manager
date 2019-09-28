import React, {Component} from 'react'
import Faker from "faker";
import {Link} from "react-router-dom";

class Task extends Component {

    render() {
        return (
            <Link className="ui card olive basic" to={'/task/details/' + this.props.el.id}>
                <div className="content">
                    <div className="header">{this.props.el.name}</div>
                    <div className="description">
                        <span className="category">{this.props.el.description}</span>
                    </div>
                </div>
                <div className="extra content">
                    <div className="left floated left aligned">
                        started:<br/>
                        {this.props.el.startDate}
                    </div>
                    <div className="right floated author">
                        <img className="ui avatar image" src={Faker.image.avatar()} />
                    </div>
                </div>
            </Link>
        )
    }

}

// description: "Some description of task 5"
// endDate: "05.11.2016 11:44"
// id: "taskId_05"
// name: "task_05"
// startDate: "05.11.2016 11:44"
// status: "PAUSED"

export default Task