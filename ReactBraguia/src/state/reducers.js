import { combineReducers } from 'redux';
import { UPDATE_USERNAME , SET_COOKIES , RESET_STATE} from '../actions/user';

const user  = (user = { username: ''}, action) => {
    switch (action.type) {
        case UPDATE_USERNAME:
            return { username: action.username }
        case RESET_STATE:
            return { username: '' }; // Reset the username to its initial value
        default:
            return user;
    }
}

const cookies  = (cookies = { cookieVal: ''}, action) => {
    switch (action.type) {
        case SET_COOKIES:
            return { cookieVal: action.cookieVal }
        case RESET_STATE:
            return { cookieVal: '' }; // Reset the cookieVal to its initial value
        default:
            return cookies;
    }
}

export default combineReducers({ user , cookies});