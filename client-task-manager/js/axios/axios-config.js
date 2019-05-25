import axios from 'axios'
const API_URL = process.env.API_URL || 'http://localhost:7575'

export default axios.create({
    baseURL: API_URL,
    timeout: 1500,
    headers: {
        'Content-Type': 'application/json'
    }
})