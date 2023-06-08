import { View, Text } from 'react-native';
import BottomBar from '../components/BottomBar';
import { StyleSheet } from 'react-native';
import React, { useEffect, useState } from 'react';
const Home = () => {
  const [appInfo, setAppInfo] = useState(''); 
  const getAppInfo = async () => {
    try {
      const response = await fetch('https://c5a2-193-137-92-29.eu.ngrok.io/app');
      const json = await response.json();
      setAppInfo(json[0]["app_landing_page_text"])
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    getAppInfo();
  }, []);

  return (
    <View style={styles.container}>
    <View style={styles.content}>
      <Text style={styles.header}>Braguia - Your virtual guide in Braga</Text>
      <Text>{appInfo}</Text>
    </View>
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