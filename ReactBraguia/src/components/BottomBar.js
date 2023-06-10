import React from 'react';
import { View, TouchableOpacity, StyleSheet, Dimensions } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import Icon from 'react-native-vector-icons/FontAwesome';
import { useSelector, useDispatch } from 'react-redux';
import { resetState, setCookies, updateUsername } from '../actions/user';



const BottomBar = () => {
  const navigation = useNavigation();

  const dispatch = useDispatch();
  const csrfToken = useSelector((state) => state.cookies);
  const handleLogout = async () => {
    try {
      const response = await fetch('https://c5a2-193-137-92-29.eu.ngrok.io/logout', {
        credentials: 'omit',
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          csrfToken,
        },
      });

      if (response.ok) {
        // Clear cookies from Redux store
        dispatch(resetState());
        // Perform any additional logout actions
      } else {
        console.log('Logout request failed:', response.status);
        // Handle logout failure
      }
    } catch (error) {
      console.log('Logout request failed by error:', error);
      // Handle logout failure
    }
  };

  return (
    <View style={styles.container}>
      <TouchableOpacity
        style={[styles.option, { backgroundColor: '#FF0008' }]}
        onPress={() => navigation.navigate('Home')}
      >
        <Icon name="home" size={20} color="#ffffff" />
      </TouchableOpacity>
  
      <TouchableOpacity
        style={[styles.option, { backgroundColor: '#FF0008' }]}
        onPress={() => navigation.navigate('Trails')}
      >
        <Icon name="map" size={20} color="#ffffff" />
      </TouchableOpacity>
  
      <TouchableOpacity
        style={[styles.option, { backgroundColor: '#FF0008' }]}
        onPress={() => navigation.navigate('Definitions')}
      >
        <Icon name="wrench" size={20} color="#ffffff" />
      </TouchableOpacity>
  
      <TouchableOpacity
        style={[styles.option, { backgroundColor: '#FF0008' }]}
        onPress={() => navigation.navigate('Profile')}
      >
        <Icon name="user" size={20} color="#ffffff" />
      </TouchableOpacity>
  
      <TouchableOpacity
        style={[styles.option, { backgroundColor: '#FF0008' }]}
        onPress={() => navigation.navigate('Contacts')}
      >
        <Icon name="address-book" size={20} color="#ffffff" />
      </TouchableOpacity>
  
      <TouchableOpacity
        style={[styles.option, { backgroundColor: '#FF0008' }]}
        onPress={() => handleLogout()}
      >
        <Icon name="sign-out" size={20} color="#ffffff" />
      </TouchableOpacity>
    </View>
  );
  
};

const windowHeight = Dimensions.get('window').height;

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    alignItems: 'stretch',
    backgroundColor: '#f2f2f2',
    height: windowHeight * 0.05,
  },
  option: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 8,
  },
});

export default BottomBar;
