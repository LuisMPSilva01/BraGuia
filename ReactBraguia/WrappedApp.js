import React, {useEffect, useState} from 'react';
import Trails from './src/screens/Trails';
import Home from './src/screens/Home';
import Trail from './src/screens/Trail'
import Login from './src/screens/Login';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { useSelector } from 'react-redux';

const Stack = createNativeStackNavigator();
const WrappedApp = () => {
  const loggedIn = useSelector((state) => state.cookies !== '');// Access the "loggedIn" variable from Redux state

  useEffect(() => {
    // Perform any necessary initialization or side effects here
  }, []);

  return (
    <NavigationContainer>
      <Stack.Navigator>
        {loggedIn ? ( // Check if the "loggedIn" variable exists
          <>
            <Stack.Screen name="Home" component={Home} />
            <Stack.Screen name="Trails" component={Trails} />
            <Stack.Screen name="Trail" component={Trail} />
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