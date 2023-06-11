import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Linking, Image } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import BottomBar from '../components/BottomBar';

const Partners = () => {
  const [partners, setPartners] = useState([]);

  useEffect(() => {
    fetch('https://c5a2-193-137-92-29.eu.ngrok.io/app')
      .then((response) => response.json())
      .then((data) => setPartners(data[0].partners))
      .catch((error) => console.log(error));
  }, []);

  const handleItemClick = (item) => {
    if (item.partner_phone) {
      Linking.openURL(`tel:${item.partner_phone}`);
    } else if (item.partner_url) {
      Linking.openURL(item.partner_url);
    } else if (item.partner_mail) {
      Linking.openURL(`mailto:${item.partner_mail}`);
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.content}>
        <View style={styles.partnerInfo}>
          {partners.map((item) => (
            <TouchableOpacity
              key={item.partner_name}
              style={styles.partnerDetail}
              onPress={() => handleItemClick(item)}
            >
              <Image source={require('../../assets/uminho_logo.png')} style={styles.partnerImage} />
              <View style={styles.partnerDetailsContainer}>
                <Text style={styles.partnerName}>{item.partner_name}</Text>
                {item.partner_phone && (
                  <Text style={styles.partnerPhone}>
                    <Ionicons name="call" size={25} color="black" /> {item.partner_phone}
                  </Text>
                )}
                {item.partner_url && (
                  <Text style={styles.partnerUrl}>
                    <Ionicons name="globe" size={25} color="black" /> {item.partner_url}
                  </Text>
                )}
                {item.partner_mail && (
                  <Text style={styles.partnerMail}>
                    <Ionicons name="mail" size={25} color="black" /> {item.partner_mail}
                  </Text>
                )}
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
    color: 'blue',
  },
  partnerMail: {
    fontSize: 25, // Increase the font size as desired
    color: 'blue',
  },
});


export default Partners;
