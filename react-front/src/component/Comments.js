import React, {Component} from 'react'
import Faker from "faker"
import api from "../axios-config";
import {toast} from "react-toastify";

class Comments extends Component {

    constructor(props) {
        super(props);
        this.state = {comments: [], text: ''}
    }

    componentDidMount() {
        this.fetchComments()
    }

    fetchComments = () => {
        api.get('/comment/all/taskId/' + this.props.taskId)
            .then(response => {
                this.setState({comments: response.data})
            })
            .catch(err => {
                toast.error(err.message, {
                    position: toast.POSITION.TOP_RIGHT
                })
            })
    }

    addComment = () => {
        const commentRequest = {
            comment: this.state.text,
            taskId: this.props.taskId
        }
        api.post('/manager/comment/save', commentRequest)
            .then(res => {
                toast.success("Comment created.", {
                    position: toast.POSITION.TOP_RIGHT
                })
                this.fetchComments()
                this.setState({text: ''})
            })
            .catch(err => {
                toast.error(err.message, {
                    position: toast.POSITION.TOP_RIGHT
                })
            })
    }

    handleChange = (event) => {
        this.setState(
            {[event.target.name]: event.target.value}
        )
    }

    render() {
        return (
            <div className="ui minimal comments segment left aligned">
                <h3 className="ui dividing header">Comments</h3>
                {this.state.comments.map((el) => {
                    return (
                        <div className="comment">
                            <a className="avatar">
                                <img src={Faker.image.avatar()}/>
                            </a>
                            <div className="content">
                                {/*<a className="author">{el.author}</a>*/}
                                <div className="metadata">
                                    <span className="date">{el.created}</span>
                                </div>
                                <div className="text">
                                    {el.text}
                                </div>
                            </div>
                        </div>
                    )
                })}
                <form className="ui reply form">
                    <div className="field">
                        <textarea name="text" onChange={this.handleChange}/>
                    </div>
                    <div className="ui blue labeled submit icon button" onClick={this.addComment}>
                        <i className="icon edit"/> Add Reply
                    </div>
                </form>
            </div>
        )
    }

}

export default Comments