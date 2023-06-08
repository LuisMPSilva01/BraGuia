import React, {useEffect, useState} from 'react';
import Trails from './src/screens/Trails';
import Home from './src/screens/Home';
import Trail from './src/screens/Trail'
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { Logs } from 'expo'

Logs.enableExpoCliLogging()

const Stack = createNativeStackNavigator()
const App = () => {

  return (

    <NavigationContainer>
      <Stack.Navigator>
      <Stack.Screen name="Home"  component={Home}/>
      <Stack.Screen name="Trails"  component={Trails} />
      <Stack.Screen name="Trail"  component={Trail} />
      {/* Add more screens here */}
    </Stack.Navigator>
  </NavigationContainer>


  );
};

export default App;