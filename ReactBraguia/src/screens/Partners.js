import React, { useEffect, useState, useContext } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Linking, Image } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useSelector } from 'react-redux';

import themeContext from '../theme/themeContext';

const Partners = () => {
  const [partners, setPartners] = useState([]);

  const theme = useContext(themeContext)

  const initialData = useSelector((state) => state.appData.appinfo);
  useEffect(() => {
    if (initialData) setPartners(initialData.partners);
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
        <View style={styles.partnerInfo}>
          {partners.map((item) => (
            <View key={item.partner_name} style={styles.partnerDetail}>
              <Image source={require('../../assets/uminho_logo.png')} style={styles.partnerImage} />
              <View style={styles.partnerDetailsContainer}>
                <Text style={[styles.partnerName, {color:theme.color}]}>{item.partner_name}</Text>
                {item.partner_phone && (
                  <TouchableOpacity onPress={() => handlePhoneClick(item.partner_phone)}>
                    <Text style={[styles.partnerPhone, {color:theme.color}]}>
                      <Ionicons name="call" size={25} style={{color:theme.color}} /> {item.partner_phone}
                    </Text>
                  </TouchableOpacity>
                )}
                {item.partner_url && (
                  <TouchableOpacity onPress={() => handleUrlClick(item.partner_url)}>
                    <Text style={[styles.partnerUrl, {color:theme.color}]}>
                      <Ionicons name="globe" size={25} style={{color:theme.color}} /> {item.partner_url}
                    </Text>
                  </TouchableOpacity>
                )}
                {item.partner_mail && (
                  <TouchableOpacity onPress={() => handleEmailClick(item.partner_mail)}>
                    <Text style={[styles.partnerMail, {color:theme.color}]}>
                      <Ionicons name="mail" size={25} style={{color:theme.color}} /> {item.partner_mail}
                    </Text>
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
  partnerInfo: {
    alignItems: 'center',
  },
  partnerDetail: {
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: 10,
  },
  partnerImage: {
    width: 120, // Increase the width as desired
    height: 60, // Increase the height as desired
    marginRight: 10,
  },
  partnerDetailsContainer: {
    marginLeft: 10,
  },
  partnerName: {
    fontSize: 25, // Increase the font size as desired
    fontWeight: 'bold',
    marginBottom: 5,
  },
  partnerPhone: {
    fontSize: 25, // Increase the font size as desired
    marginBottom: 3,
  },
  partnerUrl: {
    fontSize: 25, // Increase the font size as desired
    marginBottom: 3,
  },
  partnerMail: {
    fontSize: 25, // Increase the font size as desired
  },
});

export default Partners;
