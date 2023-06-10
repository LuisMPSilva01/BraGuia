export const UPDATE_USERNAME = 'UPDATE_USERNAME';
export const updateUsername = (username) => ({
  type: UPDATE_USERNAME,
  username 
});

export const SET_COOKIES = 'SET_COOKIES';
export const setCookies = (cookieVal) => ({
  type: SET_COOKIES,
  cookieVal
});

export const ADD_TRIP = 'ADD_TRIP';
export const addTrip = (trip) => ({
  type: ADD_TRIP,
  trip
});


export const RESET_STATE = 'RESET_STATE';
export const resetState = () => ({
  type: RESET_STATE,
});

