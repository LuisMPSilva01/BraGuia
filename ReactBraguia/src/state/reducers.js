import { combineReducers } from 'redux';
import { UPDATE_USERNAME , SET_COOKIES , ADD_TRIP, SET_DISTANCE,RESET_STATE, SET_DARK_MODE} from '../actions/user';
import {SET_APP_INFO, SET_APP_TRAILS, RESET_APP_DATA} from '../actions/appData';

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

const trips = (trips = { tripsVal: [] }, action) => {
  switch (action.type) {
    case ADD_TRIP:
      return {
        tripsVal: [...trips.tripsVal, action.trip]
      };
    case RESET_STATE:
      return {
        tripsVal: []
      };
    default:
      return trips;
  }
};

const distance = (distance = { distanceVal: 1 }, action) => {
  switch (action.type) {
    case SET_DISTANCE:
      return {
        distanceVal: action.dist
      };
    case RESET_STATE:
      return {
        distanceVal: 1
      };
    default:
      return distance;
  }
};

const darkMode_reducer = (darkMode_reducer = { darkMode: false }, action) => {
  switch (action.type) {
    case SET_DARK_MODE:
      return {
        darkMode: action.dm
      };
    case RESET_STATE:
      return {
        darkMode: false
      };
    default:
      return darkMode_reducer;
  }
};
  
  
const appData = (state = { appinfo: null, trails: [] }, action) => {
  switch (action.type) {
    case SET_APP_INFO:
      return {
        ...state,
        appinfo: action.appinfo
      };
    case SET_APP_TRAILS:
      return {
        ...state,
        trails: action.trails
      };
    case RESET_APP_DATA:
      return {
        appinfo: null,
        trails: []
      };
    default:
      return state;
  }
};

export default combineReducers({ user, cookies, trips, appData,distance, darkMode_reducer });