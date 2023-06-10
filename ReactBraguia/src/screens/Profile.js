import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import BottomBar from '../components/BottomBar';

const Profile = () => {
  return (
    <View style={styles.container}>
      <View style={styles.content}>
        <Text style={styles.text}>Profile page</Text>
      </View>
      <BottomBar />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  content: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  text: {
    fontSize: 20,
    fontWeight: 'bold',
  },
});

export default Profile;
