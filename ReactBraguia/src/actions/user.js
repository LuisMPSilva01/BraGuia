export const UPDATE_USERNAME = 'UPDATE_USERNAME';
export const updateUsername = (username) => ({ type: UPDATE_USERNAME, username });

export const SET_COOKIES = 'SET_COOKIES';
export const setCookies = (cookies) => ({
  type: SET_COOKIES,
  cookies
});