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
                    <div className="right floated author">
                        <img className="ui avatar image" src={Faker.image.avatar()} />
                    </div>
                </div>
            </Link>
        )
    }

}

export default Task