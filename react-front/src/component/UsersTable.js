import React, {Component} from 'react'
import Faker from "faker";

class UsersTable extends Component {

    render() {
        return (
            <div>
                <table className="ui very basic collapsing celled table">
                    <thead>
                    <tr>
                        <th>Participants</th>
                        <th>Opened tasks</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>
                            <h4 className="ui image header">
                                <img src={Faker.image.avatar()} className="ui mini rounded image"/>
                                    <div className="content">
                                        Lena
                                        <div className="sub header">Human Resources
                                        </div>
                                    </div>
                            </h4>
                        </td>
                        <td>
                            22
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <h4 className="ui image header">
                                <img src={Faker.image.avatar()} className="ui mini rounded image"/>
                                    <div className="content">
                                        Matthew
                                        <div className="sub header">Fabric Design
                                        </div>
                                    </div>
                            </h4>
                        </td>
                        <td>
                            15
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <h4 className="ui image header">
                                <img src={Faker.image.avatar()} className="ui mini rounded image"/>
                                    <div className="content">
                                        Lindsay
                                        <div className="sub header">Entertainment
                                        </div>
                                    </div>
                            </h4>
                        </td>
                        <td>
                            12
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <h4 className="ui image header">
                                <img src={Faker.image.avatar()} className="ui mini rounded image"/>
                                    <div className="content">
                                        Mark
                                        <div className="sub header">Executive
                                        </div>
                                    </div>
                            </h4>
                        </td>
                        <td>
                            11
                        </td>
                    </tr>
                    </tbody>
                </table>

            </div>
        )
    }
}

export default UsersTable