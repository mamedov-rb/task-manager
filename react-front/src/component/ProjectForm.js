import React, {Component} from 'react'
import SkyLight from 'react-skylight'

class ProjectForm extends Component {

    constructor(props) {
        super(props)
        this.state = {name: '', description: '', startDate: ''}
    }

    handleSubmit = (event) => {
        event.preventDefault()
        this.props.addProject({
            name: this.state.name,
            description: this.state.description,
            startDate: this.state.startDate
        })
        this.refs.addDialog.hide()
    }

    cancelSubmit = (event) => {
        event.preventDefault()
        this.refs.addDialog.hide()
    }

    handleChange = (event) => {
        this.setState(
            {[event.target.name]: event.target.value}
        )
    }

    render() {
        return (
            <div>
                <SkyLight hideOnOverlayClicked ref="addDialog">
                    <h3>Create project</h3>
                    <form className="ui center floated segment">
                        <div className="ui input">
                            <input type="text" placeholder="Name" name="name" onChange={this.handleChange}/><br/>
                        </div>
                        <div className="ui input">
                            <input type="text" placeholder="Description" name="description" onChange={this.handleChange}/><br/>
                        </div>
                        <div className="ui input">
                            <input type="date" placeholder="Start date" name="startDate" onChange={this.handleChange}/><br/>
                        </div>
                        <button className="ui button" onClick={this.handleSubmit}>Save</button>
                        <button className="ui button" onClick={this.cancelSubmit}>Cancel</button>
                    </form>
                </SkyLight>
                <div>
                    <button className= "ui submit button left segment" onClick={() => this.refs.addDialog.show()}>
                        Add new
                    </button>
                </div>
            </div>
        )
    }
}

export default ProjectForm