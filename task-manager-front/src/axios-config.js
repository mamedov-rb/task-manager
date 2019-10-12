import axios from 'axios'
const URL = process.env.API_URL || 'http://localhost:9090/api'
const jwt = sessionStorage.getItem('jwt')

export default axios.create({
    baseURL: URL,
    timeout: 1500,
    headers: {
        'Content-Type': 'application/json',
        'Authorization': jwt
    }
})
