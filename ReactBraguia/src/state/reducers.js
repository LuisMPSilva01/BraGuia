import { combineReducers } from 'redux';
import { UPDATE_USERNAME } from '../actions/user';

const user  = (user = { username: ''}, action) => {
    switch (action.type) {
        case UPDATE_USERNAME:
            return { username: action.username }
        default:
            return user;
    }
}

export default combineReducers({ user });