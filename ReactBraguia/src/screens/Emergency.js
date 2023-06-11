import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Linking } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import BottomBar from '../components/BottomBar';

const Emergency = () => {
  const [contacts, setContacts] = useState([]);

  useEffect(() => {
    fetch('https://c5a2-193-137-92-29.eu.ngrok.io/app')
      .then((response) => response.json())
      .then((data) => setContacts(data[0].contacts))
      .catch((error) => console.log(error));
  }, []);

  const handleItemClick = (item, action) => {
    switch (action) {
      case 'phone':
        if (item.contact_phone) {
          Linking.openURL(`tel:${item.contact_phone}`);
        }
        break;
      case 'url':
        if (item.contact_url) {
          Linking.openURL(item.contact_url);
        }
        break;
      case 'email':
        if (item.contact_mail) {
          Linking.openURL(`mailto:${item.contact_mail}`);
        }
        break;
      default:
        break;
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.content}>
        <View style={styles.contactInfo}>
          {contacts.map((item) => (
            <TouchableOpacity
              key={item.contact_name}
              style={styles.contactDetail}
            >
              <View style={styles.contactDetailsContainer}>
                <TouchableOpacity
                  onPress={() => handleItemClick(item, 'phone')}
                >
                  <View style={styles.contactRow}>
                    <Ionicons name="person" size={25} color="black" />
                    <Text style={styles.contactName}>{item.contact_name}</Text>
                  </View>
                </TouchableOpacity>
                <TouchableOpacity
                  onPress={() => handleItemClick(item, 'phone')}
                >
                  <View style={styles.contactRow}>
                    <Ionicons name="call" size={25} color="black" />
                    <Text style={styles.contactPhone}>{item.contact_phone}</Text>
                  </View>
                </TouchableOpacity>
                <TouchableOpacity
                  onPress={() => handleItemClick(item, 'url')}
                >
                  <View style={styles.contactRow}>
                    <Ionicons name="globe" size={25} color="black" />
                    <Text style={styles.contactUrl}>{item.contact_url}</Text>
                  </View>
                </TouchableOpacity>
                <TouchableOpacity
                  onPress={() => handleItemClick(item, 'email')}
                >
                  <View style={styles.contactRow}>
                    <Ionicons name="mail" size={25} color="black" />
                    <Text style={styles.contactMail}>{item.contact_mail}</Text>
                  </View>
                </TouchableOpacity>
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
  contactInfo: {
    alignItems: 'flex-start', // Align items to the left
    marginLeft: 10, // Add left margin to create some spacing
  },
  contactDetail: {
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: 10,
  },
  contactDetailsContainer: {
    marginLeft: 10,
  },
  contactRow: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  contactName: {
    fontSize: 16,
    fontWeight: 'bold',
    marginLeft: 5,
  },
  contactPhone: {
    fontSize: 20,
    marginLeft: 5,
  },
  contactUrl: {
    fontSize: 20,
    marginLeft: 5,
    color: 'blue',
  },
  contactMail: {
    fontSize: 20,
    marginLeft: 5,
    color: 'blue',
  },
});

export default Emergency;
