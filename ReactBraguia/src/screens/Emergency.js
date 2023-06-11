import React, { useEffect, useState, useContext } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Linking, Image } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useSelector } from 'react-redux';

import themeContext from '../theme/themeContext';

const Emergency = () => {
  const [contacts, setContacts] = useState([]);

  const theme = useContext(themeContext)

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
    <View style={[styles.container, {backgroundColor:theme.backgroundColor}]}>
      <View style={styles.content}>
        <View style={styles.contactInfo}>
          {contacts.map((item) => (
            <View key={item.contact_name} style={styles.contactDetail}>
              <View style={styles.contactDetailsContainer}>
                <Text style={[styles.contactName, {color:theme.color}]}>{item.contact_name}</Text>
                {item.contact_phone && (
                  <TouchableOpacity onPress={() => handlePhoneClick(item.contact_phone)}>
                    <View style={styles.contactRow}>
                      <Ionicons name="call" size={25} style={{color:theme.color}} />
                      <Text style={[styles.contactPhone, {color:theme.color}]}>{item.contact_phone}</Text>
                    </View>
                  </TouchableOpacity>
                )}
                {item.contact_url && (
                  <TouchableOpacity onPress={() => handleUrlClick(item.contact_url)}>
                    <View style={styles.contactRow}>
                      <Ionicons name="globe" size={25} style={{color:theme.color}} />
                      <Text style={[styles.contactUrl, {color:theme.color}]}>{item.contact_url}</Text>
                    </View>
                  </TouchableOpacity>
                )}
                {item.contact_mail && (
                  <TouchableOpacity onPress={() => handleEmailClick(item.contact_mail)}>
                    <View style={styles.contactRow}>
                      <Ionicons name="mail" size={25} style={{color:theme.color}} />
                      <Text style={[styles.contactMail, {color:theme.color}]}>{item.contact_mail}</Text>
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
  },
  contactMail: {
    fontSize: 22, // Increase the font size as desired
  },
});

export default Emergency;
