import { View, Text } from 'react-native';
import BottomBar from '../components/BottomBar';
import { StyleSheet } from 'react-native';
import React, { useEffect, useState } from 'react';
import LogoutButton from '../components/LogOutButton';
import { useSelector, useDispatch } from 'react-redux';
import { resetState, setCookies, updateUsername } from '../actions/user';
import { PermissionsAndroid } from 'react-native';
import ReactNativeForegroundService from '@supersami/rn-foreground-service';

const Home = () => {
  const [appInfo, setAppInfo] = useState(''); 
  const cookies = useSelector((state) => state.cookies.cookieVal);
  const dispatch = useDispatch();
  
  const getAppInfo = async () => {
    try {
      const response = await fetch('https://c5a2-193-137-92-29.eu.ngrok.io/app');
      const json = await response.json();
      setAppInfo(json[0]["app_landing_page_text"])
    } catch (error) {
      console.error(error);
    }
  };


  //request the permission before starting the service.
  const getUserFunc = () => {
    fetch('https://c5a2-193-137-92-29.eu.ngrok.io/user', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Cookie': cookies
      },
    })
    .then(response => {
      if (response.ok) {
        // Perform any additional logout actions
        return response.json();
      } else {
        console.log('GetUser request failed:', response.status);
        throw new Error('GetUser request failed');
      }
    })
    .then(user => {
      dispatch(updateUsername(user));
    })
    .catch(error => {
      console.log('GetUser request failed by error:', error);
      throw new Error('GetUser request failed');
    });
  };

  const requestLocationPermission = () => {
    /*
    ReactNativeForegroundService.add_task(() => update(), {
      delay: 1000,
      onLoop: true,
      taskId: "taskid",
      onError: (e) => console.log(`Error logging:`, e),
    });
*/
    
    return ReactNativeForegroundService.start({
      id: 1244,
      title: "Foreground Service",
      message: "We are live World",
      icon: "ic_launcher",
      button: true,
      button2: true,
      buttonText: "Button",
      button2Text: "Anther Button",
      buttonOnPress: "cray",
      setOnlyAlertOnce: true,
      color: "#000000",
      progress: {
        max: 100,
        curr: 50,
      },
    });
    
    /*
    const backgroundgranted = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.ACCESS_BACKGROUND_LOCATION,
      {
        title: 'Background Location Permission',
        message:
          'We need access to your location ' +
          'so you can get live quality updates.',
        buttonNeutral: 'Ask Me Later',
        buttonNegative: 'Cancel',
        buttonPositive: 'OK',
      },
    );
    console.log("yo");
    if (backgroundgranted === PermissionsAndroid.RESULTS.GRANTED) {
      console.log("granted");
    }*/
  };

  useEffect(() => {
    getAppInfo();
    getUserFunc();
    requestLocationPermission();
  }, []);

  return (
    <View style={styles.container}>
    <View style={styles.content}>
      <Text style={styles.header}>Braguia - Your virtual guide in Braga</Text>
      <Text>{appInfo}</Text>
    </View>
    <LogoutButton/>
    <BottomBar />
  </View>
  );
};

const styles = StyleSheet.create({
  header:{
    fontSize: 24,
    fontWeight: 'bold',
    color: 'black',
  },
  container: {
    flex: 1,
  },
  content: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});

export default Home;