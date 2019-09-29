import React, {Component} from 'react'
import Faker from "faker"

class UsersTable extends Component {

    render() {
        return (
            <div>
                <table className="ui very basic collapsing celled table">
                    <thead>
                    <tr>
                        <th>Participants</th>
                        <th>Tasks</th>
                    </tr>
                    </thead>
                    <tbody>
                    {this.props.users.map((el) => {
                        return (
                            <tr>
                                <td>
                                    <h4 className="ui image header">
                                        <img src={Faker.image.avatar()} className="ui mini rounded image"/>
                                        <div className="extra content">
                                            {el.fullName}
                                            <div className="sub header">
                                                {el.roles.map((i) => {return (<span>{i.name.toLowerCase()} </span>)})}
                                            </div>
                                        </div>
                                    </h4>
                                </td>
                                <td>
                                    11
                                </td>
                            </tr>
                        )
                    })}
                    </tbody>
                </table>

            </div>
        )
    }
}

export default UsersTable