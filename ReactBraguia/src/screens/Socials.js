import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Linking } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import BottomBar from '../components/BottomBar';

const Socials = () => {
  const [socials, setSocials] = useState([]);

  useEffect(() => {
    fetch('https://c5a2-193-137-92-29.eu.ngrok.io/app')
      .then((response) => response.json())
      .then((data) => setSocials(data[0].socials))
      .catch((error) => console.log(error));
  }, []);

  const handleItemClick = (item) => {
    if (item.social_url) {
      Linking.openURL(item.social_url);
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.content}>
        <View style={styles.socialInfo}>
          {socials.map((item) => (
            <TouchableOpacity
              key={item.social_name}
              style={styles.socialDetail}
              onPress={() => handleItemClick(item)}
            >
              <Ionicons name="logo-facebook" size={50} color="blue" />
              <View style={styles.socialDetailsContainer}>
                <Text style={styles.socialName}>{item.social_name}</Text>
                <Text style={styles.socialUrl}>{item.social_url}</Text>
              </View>
            </TouchableOpacity>
          ))}
        </View>
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
  socialInfo: {
    alignItems: 'center',
  },
  socialDetail: {
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: 10,
  },
  socialDetailsContainer: {
    marginLeft: 10,
  },
  socialName: {
    fontSize: 25,
    fontWeight: 'bold',
    marginBottom: 5,
  },
  socialUrl: {
    fontSize: 25,
    color: 'blue',
  },
});

export default Socials;
