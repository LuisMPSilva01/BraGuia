import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Linking, Image } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useSelector } from 'react-redux';

const Emergency = () => {
  const [contacts, setContacts] = useState([]);

  const initialData = useSelector((state) => state.appData.appinfo);
  useEffect(() => {
    if (initialData) setContacts(initialData.contacts);
  }, []);

  const handlePhoneClick = (phone) => {
    if (phone) {
      Linking.openURL(`tel:${phone}`);
    }
  };

  const handleUrlClick = (url) => {
    if (url) {
      Linking.openURL(url);
    }
  };

  const handleEmailClick = (email) => {
    if (email) {
      Linking.openURL(`mailto:${email}`);
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.content}>
        <View style={styles.contactInfo}>
          {contacts.map((item) => (
            <View key={item.contact_name} style={styles.contactDetail}>
              <View style={styles.contactDetailsContainer}>
                <Text style={styles.contactName}>{item.contact_name}</Text>
                {item.contact_phone && (
                  <TouchableOpacity onPress={() => handlePhoneClick(item.contact_phone)}>
                    <View style={styles.contactRow}>
                      <Ionicons name="call" size={25} color="black" />
                      <Text style={styles.contactPhone}>{item.contact_phone}</Text>
                    </View>
                  </TouchableOpacity>
                )}
                {item.contact_url && (
                  <TouchableOpacity onPress={() => handleUrlClick(item.contact_url)}>
                    <View style={styles.contactRow}>
                      <Ionicons name="globe" size={25} color="black" />
                      <Text style={styles.contactUrl}>{item.contact_url}</Text>
                    </View>
                  </TouchableOpacity>
                )}
                {item.contact_mail && (
                  <TouchableOpacity onPress={() => handleEmailClick(item.contact_mail)}>
                    <View style={styles.contactRow}>
                      <Ionicons name="mail" size={25} color="black" />
                      <Text style={styles.contactMail}>{item.contact_mail}</Text>
                    </View>
                  </TouchableOpacity>
                )}
              </View>
            </View>
          ))}
        </View>
      </View>
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
    alignItems: 'flex-start',
  },
  contactDetail: {
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: 10,
  },
  contactImage: {
    width: 120, // Increase the width as desired
    height: 60, // Increase the height as desired
    marginRight: 10,
  },
  contactDetailsContainer: {
    marginLeft: 10,
  },
  contactRow: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  contactName: {
    fontSize: 25, // Increase the font size as desired
    fontWeight: 'bold',
    marginBottom: 5,
  },
  contactPhone: {
    fontSize: 22, // Increase the font size as desired
    marginBottom: 3,
  },
  contactUrl: {
    fontSize: 22, // Increase the font size as desired
    marginBottom: 3,
    color: 'blue',
  },
  contactMail: {
    fontSize: 22, // Increase the font size as desired
    color: 'blue',
  },
});

export default Emergency;
