import React, { useEffect, useState } from 'react';
import { View, TextInput, Button, Text, Keyboard, TouchableWithoutFeedback, AsyncStorage } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { useSelector, useDispatch } from 'react-redux';
import { resetState, setCookies, updateUsername } from '../actions/user';

const LogoutButton = () => {
  const dispatch = useDispatch();
  const csrfToken = useSelector((state) => state.cookies);
  const handleLogout = async () => {
    try {
      const response = await fetch('https://c5a2-193-137-92-29.eu.ngrok.io/logout', {
        credentials: 'omit',
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          csrfToken,
        },
      });

      if (response.ok) {
        // Clear cookies from Redux store
        dispatch(resetState());
        // Perform any additional logout actions
      } else {
        console.log('Logout request failed:', response.status);
        // Handle logout failure
      }
    } catch (error) {
      console.log('Logout request failed by error:', error);
      // Handle logout failure
    }
  };

  return (
    <Button title="Logout" onPress={handleLogout} />
  );
};
// Existing styles and export statement

export default LogoutButton;
