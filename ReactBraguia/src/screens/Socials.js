import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Linking } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import BottomBar from '../components/BottomBar';
import { useSelector } from 'react-redux';

const Socials = () => {
  const [socials, setSocials] = useState([]);

  const initialData = useSelector((state) => state.appData.appinfo);
  useEffect(() => {
    if (initialData) setSocials(initialData.socials);
  }, []);

  return (
    <View style={styles.container}>
      <View style={styles.content}>
        <View style={styles.socialInfo}>
          {socials.map((item) => (
            <TouchableOpacity
              key={item.social_name}
              style={styles.socialDetail}
            >
              <Ionicons name="logo-facebook" size={50} color="blue" />
              <View style={styles.socialDetailsContainer}>
                <Text style={styles.socialName}>{item.social_name}</Text>
                {item.social_url && (
                  <TouchableOpacity onPress={() => Linking.openURL(item.social_url)}>
                    <Text style={styles.socialUrl} selectable={false}>{item.social_url}</Text>
                  </TouchableOpacity>
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
    textDecorationLine: 'underline',
  },
});

export default Socials;
//TODO: CORRIGIR!