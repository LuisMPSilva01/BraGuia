import React from 'react';
import { ScrollView, View, Text, Image, StyleSheet, TouchableOpacity } from 'react-native';
import { useNavigation } from '@react-navigation/native';

const PinsSlide = ({ pins }) => {
  const pinInfo = pins.map((edge) => {
    const startPin = edge.edge_start;
    const image = startPin.media.find((media) => media.media_type === 'I');
    return {
      name: startPin.pin_name,
      image: image ? image.media_file : null,
      info: startPin
    };
  });

  const noImageAvailable = require('../../assets/no_image_available.jpg');

  const navigation = useNavigation();

  const handlePinPress = (pin) => {
    // Navigate to the Pin page
    navigation.navigate('Pin', { pin: pin.info});
  };

  return (
    <ScrollView style={styles.scrollContainer} horizontal>
      {pinInfo.map((pin) => (
        <TouchableOpacity
          key={pin.name}
          style={styles.itemContainer}
          onPress={() => handlePinPress(pin)}
        >
          <Text style={styles.pinName}>{pin.name}</Text>
          {pin.image != null ? (
            <Image source={{ uri: pin.image }} style={styles.image} />
          ) : (
            <Image source={noImageAvailable} style={styles.image} />
          )}
        </TouchableOpacity>
      ))}
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  scrollContainer: {
    marginBottom: 20,
  },
  itemContainer: {
    padding: 10,
    backgroundColor: '#f1f1f1',
    borderRadius: 10,
    marginRight: 20,
    marginTop: 20,
  },
  image: {
    width: 50,
    height: 50,
    marginBottom: 5,
    marginLeft: 20,
    marginBottom: 20,
  },
  pinName: {
    fontSize: 16,
    fontWeight: 'bold',
    marginTop: 20,
    marginBottom: 20,
  },
});

export default PinsSlide;
