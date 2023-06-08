import { combineReducers } from 'redux';
import { UPDATE_USERNAME , SET_COOKIES } from '../actions/user';

const user  = (user = { username: ''}, action) => {
    switch (action.type) {
        case UPDATE_USERNAME:
            return { username: action.username }
        default:
            return user;
    }
}

const cookies  = (cookies = { cookieVal: ''}, action) => {
    switch (action.type) {
        case SET_COOKIES:
            return { cookieVal: action.cookieVal }
        default:
            return cookies;
    }
}

export default combineReducers({ user , cookies});