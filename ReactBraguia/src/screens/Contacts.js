import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import BottomBar from '../components/BottomBar';

const Contacts = ({ navigation }) => {
  const handleButtonPress = (screenName) => {
    navigation.navigate(screenName);
  };

  return (
    <View style={styles.container}>
      <View style={styles.content}>
        <TouchableOpacity
          style={styles.buttonContainer}
          onPress={() => handleButtonPress('Emergency contacts')}
        >
          <Icon name="exclamation-circle" size={20} color="white" />
          <Text style={styles.buttonText}>Emergency</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.buttonContainer}
          onPress={() => handleButtonPress('Socials')}
        >
          <Icon name="users" size={20} color="white" />
          <Text style={styles.buttonText}>Socials</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.buttonContainer}
          onPress={() => handleButtonPress('Partners')}
        >
          <Icon name="handshake-o" size={20} color="white" />
          <Text style={styles.buttonText}>Partners</Text>
        </TouchableOpacity>
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
  buttonContainer: {
    justifyContent: 'center',
    alignItems: 'center',
    width: 200,
    height: 50,
    marginVertical: 10,
    backgroundColor: 'red',
    borderRadius: 10,
    flexDirection: 'row', // Added to align icon and text horizontally
  },
  buttonText: {
    fontSize: 20,
    fontWeight: 'bold',
    color: 'white',
    marginLeft: 10, // Added to add spacing between icon and text
  },
});

export default Contacts;
