import axios from 'axios'
const API_URL = process.env.API_URL || 'http://localhost:6060/api'

export default axios.create({
    baseURL: API_URL,
    timeout: 1500,
    headers: {
        'Content-Type': 'application/json'
    }
})