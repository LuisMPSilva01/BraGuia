import React, {useEffect, useState} from 'react';
import Trails from './src/screens/Trails';
import Home from './src/screens/Home';
import Trail from './src/screens/Trail'
import Login from './src/screens/Login';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { Logs } from 'expo'
import { Provider } from 'react-redux';
import { PersistGate } from 'redux-persist/integration/react';
import { store, persistor } from './src/state/store';

Logs.enableExpoCliLogging()

const Stack = createNativeStackNavigator()
const App = () => {

  return (
    <Provider store={store}>
      <PersistGate persistor={persistor} loading={null}>
        <NavigationContainer>
          <Stack.Navigator>
          {/*<Stack.Screen name="Login"  component={Login}/> */}
          <Stack.Screen name="Home"  component={Home}/>
          <Stack.Screen name="Trails"  component={Trails} />
          <Stack.Screen name="Trail"  component={Trail} />
          {/* Add more screens here */}
          </Stack.Navigator>
        </NavigationContainer>
      </PersistGate>
    </Provider>
  );
};

export default App;