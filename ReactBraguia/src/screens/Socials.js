import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Linking } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import BottomBar from '../components/BottomBar';

const Socials = () => {
  const contactData = [
    {
      id: 1,
      icon: 'mail',
      text: 'example@mail.com',
    },
    {
      id: 2,
      icon: 'call',
      text: '(123) 456-7890',
    },
    {
      id: 3,
      icon: 'globe',
      text: 'https://www.example.com',
    },
  ];

  const handleItemClick = (item) => {
    if (item.icon === 'call') {
      Linking.openURL(`tel:${item.text}`);
    } else if (item.icon === 'globe') {
      Linking.openURL(item.text);
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.content}>
        <View style={styles.contactInfo}>
          {contactData.map((item) => (
            <TouchableOpacity
              key={item.id}
              style={styles.contactDetail}
              onPress={() => handleItemClick(item)}
            >
              <Ionicons name={item.icon} size={24} color="black" />
              <Text style={styles.contactText}>{item.text}</Text>
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
  text: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  contactInfo: {
    alignItems: 'center',
  },
  contactDetail: {
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: 10,
  },
  contactText: {
    marginLeft: 10,
    fontSize: 16,
  },
});

export default Socials;
