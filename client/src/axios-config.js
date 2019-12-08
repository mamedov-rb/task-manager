
import axios from 'axios'
const URL = process.env.API_BASE_URL || 'http://5.228.51.196:3000/api'
const jwt = sessionStorage.getItem('jwt')

export default axios.create({
    baseURL: URL,
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json',
        'Authorization': jwt
    }
})
