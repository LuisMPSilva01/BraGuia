import { View, Text, Image  } from 'react-native';
import { StyleSheet } from 'react-native';
import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { updateUsername } from '../actions/user';
import { updateAppInfo, setTrails } from '../actions/appData';

const Home = () => {
  const [title, setTitle] = useState("Title"); 
  const [appInfo, setAppInfo] = useState("Loading"); 
  const cookies = useSelector((state) => state.cookies.cookieVal);
  const dispatch = useDispatch();
  const initialData = useSelector((state) => state.appData.appinfo);
  
  useEffect(() => {
    if(initialData){
      setAppInfo(initialData.app_landing_page_text)
    }
    if(initialData){
      setTitle(initialData.app_desc)
    }
    getAppInfo();
    getUserFunc();
    getTrails();
  }, []);

  const getAppInfo = () => {
    fetch('https://c5a2-193-137-92-29.eu.ngrok.io/app')
      .then(response => response.json())
      .then(json => {
        const val = json[0];
        dispatch(updateAppInfo(val));
        setAppInfo(val["app_landing_page_text"]);
        setTitle(val["app_desc"]);
      })
      .catch(error => console.error(error));
  };

  const getTrails = () => {
    return fetch('https://c5a2-193-137-92-29.eu.ngrok.io/trails')
      .then(response => response.json())
      .then(json => {
        dispatch(setTrails(json));
      })
      .catch(error => {
        console.error(error);
      });
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

  return (
    <View style={styles.container}>
      <View style={styles.content}>
        <Text style={styles.header}>{title}</Text>
        <Image
          source={require('../../assets/logo_red.png')}
          style={styles.image}
        />
        <Text>{appInfo}</Text>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  content: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  header: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 60,
  },
  gridContainer: {
    width: '80%',
    justifyContent: 'center',
    alignItems: 'center',
  },
  gridRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 16,
  },
  image: {
    marginTop: 30,
    width: 300,
    height: 200,
    marginBottom: 90,
  },
});

export default Home;