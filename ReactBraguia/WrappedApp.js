import React, {useEffect, useState} from 'react';
import Trails from './src/screens/Trails';
import Home from './src/screens/Home';
import Trail from './src/screens/Trail'
import Login from './src/screens/Login';
import Pin from './src/screens/Pin'
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { useSelector ,useDispatch} from 'react-redux';
import PrintLocationOnScreen from './src/screens/PrintLocationOnScreen';
const Stack = createNativeStackNavigator();
const WrappedApp = () => {
  const cookieValue = useSelector((state) => state.cookies.cookieVal);
  const loggedIn = cookieValue !== "";
  console.log("State:", useSelector((state) => state)); // Add this line
  console.log("Trips:",useSelector((state) => JSON.stringify(state.trips.tripsVal))); 
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
            <Stack.Screen name="Pin"  component={Pin} />
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