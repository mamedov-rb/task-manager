import React, {Component} from 'react'
import SkyLight from 'react-skylight'

class TaskForm extends Component {

    constructor(props) {
        super(props)
        this.state = {name: '', description: '', startDate: ''}
    }

    handleSubmit = (event) => {
        event.preventDefault()
        this.props.addTask({
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
                    <h3>Create task</h3>
                    <form style={{marginTop: 50}}>
                        <div className="ui input">
                            <input type="text" placeholder="Name" name="name" required="true" onChange={this.handleChange}/><br/>
                        </div>
                        <div className="ui input">
                            <input type="text" placeholder="Description" name="description" required="true" onChange={this.handleChange}/><br/>
                        </div>
                        <div className="ui input">
                            <input type="date" name="startDate" required="true" onChange={this.handleChange}/><br/>
                        </div>
                        <button className="ui positive basic button" onClick={this.handleSubmit}>Submit</button>
                        <button className="ui negative basic button" onClick={this.cancelSubmit}>Cancel</button>
                    </form>
                </SkyLight>
                <div className="ui left aligned segment">
                    <button className= "ui positive basic button" onClick={() => this.refs.addDialog.show()}>
                        Add new
                    </button>
                </div>
            </div>
        )
    }
}

export default TaskForm