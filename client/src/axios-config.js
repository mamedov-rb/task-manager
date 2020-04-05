
import axios from 'axios'
const URL = process.env.API_BASE_URL || 'http://ubuntu@ec2-18-223-214-231.us-east-2.compute.amazonaws.com:4000/api'
const jwt = sessionStorage.getItem('jwt')

export default axios.create({
    baseURL: URL,
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json',
        'Authorization': jwt
    }
})
