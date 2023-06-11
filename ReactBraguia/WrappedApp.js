import React, { useEffect, useState } from "react";
import { ScrollView } from "react-native";
import Trails from "./src/screens/Trails";
import Home from "./src/screens/Home";
import Trail from "./src/screens/Trail";
import Login from "./src/screens/Login";
import Definitions from "./src/screens/Definitions";
import Contacts from "./src/screens/Contacts";
import Profile from "./src/screens/Profile";
import Pin from "./src/screens/Pin";
import History from "./src/screens/History";
import Socials from "./src/screens/Socials";
import Partners from "./src/screens/Partners";
import BottomBar from "./src/components/BottomBar";
import { DarkTheme, DefaultTheme, NavigationContainer } from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import { useSelector, useDispatch } from "react-redux";
import Emergency from "./src/screens/Emergency";

import theme from "./src/theme/theme";
import themeContext from "./src/theme/themeContext";

import { EventRegister } from 'react-native-event-listeners'

const Stack = createNativeStackNavigator();

const WrappedApp = () => {
  const [darkModeEnabled, setDarkMode] = useState(false);
  useEffect(() => {
    const listener = EventRegister.addEventListener('ChangeTheme', (data) => {
      setDarkMode(data)
    })
    return () =>{
      EventRegister.removeAllListeners(listener);
    }
  })

  const cookieValue = useSelector((state) => state.cookies.cookieVal);
  const loggedIn = cookieValue !== "";
  useEffect(() => {
    // Perform any necessary initialization or side effects here
  }, []);

  const scrollableScreenWrapper = (Component) => (
    <ScrollView style={{ flex: 1 }}>
      <Component />
    </ScrollView>
  );

  const scrollableScreenWrapperWithBottomBar = (Component) => (
    <>
      <ScrollView contentContainerStyle={{ flexGrow: 1 }}>
        <Component />
      </ScrollView>
      <BottomBar />
    </>
  );

  return (
    <themeContext.Provider value={darkModeEnabled == true ? theme.dark : theme.light}>
    <NavigationContainer theme={darkModeEnabled == true ? DarkTheme : DefaultTheme}>
      <Stack.Navigator
        screenOptions={{
          headerStyle: {
            backgroundColor: "#FF0008", // Set the background color of the header
          },
          headerTitleStyle: {
            color: "#FFFFFF", // Set the color of the screen titles to white
          },
        }}
      >
        {loggedIn ? (
          <>
            <Stack.Screen name="Home">
              {(props) =>
                scrollableScreenWrapperWithBottomBar(() => <Home {...props} />)
              }
            </Stack.Screen>
            <Stack.Screen name="Trails">
              {(props) =>
                scrollableScreenWrapperWithBottomBar(() => (<Trails {...props} />))}
            </Stack.Screen>
            <Stack.Screen name="Trail">
              {(props) => scrollableScreenWrapperWithBottomBar(() => <Trail {...props} />)}
            </Stack.Screen>
            <Stack.Screen name="Pin">
              {(props) => scrollableScreenWrapperWithBottomBar(() => <Pin {...props} />)}
            </Stack.Screen>
            <Stack.Screen name="Definitions">
              {(props) =>
                scrollableScreenWrapperWithBottomBar(() => (<Definitions {...props} />))}
            </Stack.Screen>
            <Stack.Screen name="Profile">
              {(props) =>
                scrollableScreenWrapperWithBottomBar(() => (<Profile {...props} />))}
            </Stack.Screen>
            <Stack.Screen name="Contacts">
              {(props) =>
                scrollableScreenWrapperWithBottomBar(() => (<Contacts {...props} />))}
            </Stack.Screen>
            <Stack.Screen name="Trails History">
              {(props) =>
                scrollableScreenWrapperWithBottomBar(() => (<History {...props} />))}
            </Stack.Screen>
            <Stack.Screen name="Emergency contacts">
              {(props) =>
                scrollableScreenWrapperWithBottomBar(() => (<Emergency {...props} />))}
            </Stack.Screen>
            <Stack.Screen name="Socials">
              {(props) =>
                scrollableScreenWrapperWithBottomBar(() => (<Socials {...props} />))}
            </Stack.Screen>
            <Stack.Screen name="Partners">
              {(props) => scrollableScreenWrapperWithBottomBar(() => (<Partners {...props} />))}
            </Stack.Screen>
          </>
        ) : (
          <Stack.Screen name="Login">
            {(props) => scrollableScreenWrapper(() => <Login {...props} />)}
          </Stack.Screen>
        )}
      </Stack.Navigator>
    </NavigationContainer>
    </themeContext.Provider>
  );
};

export default WrappedApp;