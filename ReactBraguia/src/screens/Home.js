import React from 'react';
import { View, Text } from 'react-native';
import BottomBar from '../components/BottomBar';
import { StyleSheet } from 'react-native';

const Home = () => {
  return (
    <View style={styles.container}>
    <View style={styles.content}>
      <Text>Hello, World!</Text>
    </View>
    <BottomBar />
  </View>
  );
};

const styles = StyleSheet.create({
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