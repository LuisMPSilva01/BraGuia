import React, { useEffect, useState } from 'react';

import Trails from './src/screens/Trails';
import Home from './src/screens/Home';
import Trail from './src/screens/Trail'
import Login from './src/screens/Login';
import Definitions from './src/screens/Definitions'
import Contacts from './src/screens/Contacts'
import Profile from './src/screens/Profile'
import Pin from './src/screens/Pin'
import History from './src/screens/History';
import Socials from './src/screens/Socials';
import Partners from './src/screens/Partners';

import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { useSelector, useDispatch } from 'react-redux';
import Emergency from './src/screens/Emergency';

const Stack = createNativeStackNavigator();

const WrappedApp = () => {
  const cookieValue = useSelector((state) => state.cookies.cookieVal);
  const loggedIn = cookieValue !== "";
  console.log("State:", useSelector((state) => state)); // Add this line
  console.log("Trips:", useSelector((state) => JSON.stringify(state.trips.tripsVal)));
  useEffect(() => {
    // Perform any necessary initialization or side effects here
  }, []);
  return (
    <NavigationContainer>
      <Stack.Navigator
        screenOptions={{
          headerStyle: {
            backgroundColor: '#FF0008', // Set the background color of the header
          },
          headerTitleStyle: {
            color: '#FFFFFF', // Set the color of the screen titles to white
          },
        }}
      >
        {loggedIn ? (
          <>
            <Stack.Screen name="Home" component={Home} />
            <Stack.Screen name="Trails" component={Trails} />
            <Stack.Screen name="Trail" component={Trail} />
            <Stack.Screen name="Pin" component={Pin} />
            <Stack.Screen name="Definitions" component={Definitions} />
            <Stack.Screen name="Profile" component={Profile} />
            <Stack.Screen name="Contacts" component={Contacts} />
            <Stack.Screen name="Trails History" component={History} />
            <Stack.Screen name="Emergency contacts" component={Emergency} />
            <Stack.Screen name="Socials" component={Socials} />
            <Stack.Screen name="Partners" component={Partners} />
            {/* Add more screens here */}
          </>
        ) : (
          <Stack.Screen name="Login" component={Login} />
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
};

export default WrappedApp;
