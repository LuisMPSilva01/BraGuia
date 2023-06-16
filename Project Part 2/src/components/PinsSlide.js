import React from 'react';
import { ScrollView, Text, Image, StyleSheet, TouchableOpacity, View } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import PropTypes from 'prop-types';
import noImageAvailable from '../../assets/no_image_available.jpg';

const PinsSlide = ({ pins }) => {
  const pinInfo = pins.map((pin) => {
    const image = pin.media.find((media) => media.media_type === 'I');
    return {
      name: pin.pin_name,
      image: image ? image.media_file : null,
      info: pin
    };
  });

  const navigation = useNavigation();

  const handlePinPress = (pin) => {
    // Navigate to the Pin page
    navigation.navigate('Pin', { pin: pin.info });
  };

  return (
    <ScrollView horizontal>
      {pinInfo.map((pin) => (
        <TouchableOpacity
          key={pin.name}
          style={styles.itemContainer}
          onPress={() => handlePinPress(pin)}
        >
          <Text style={styles.pinName}>{pin.name}</Text>
          <View style={styles.imageContainer}>
            {pin.image != null ? (
              <Image source={{ uri: pin.image }} style={styles.image} />
            ) : (
              <Image source={noImageAvailable} style={styles.image} />
            )}
          </View>
        </TouchableOpacity>
      ))}
    </ScrollView>
  );
};

PinsSlide.propTypes = {
  pins: PropTypes.arrayOf(
    PropTypes.shape({
      pin_name: PropTypes.string.isRequired,
      media: PropTypes.arrayOf(
        PropTypes.shape({
          media_type: PropTypes.string.isRequired,
          media_file: PropTypes.string.isRequired
        })
      ).isRequired
    })
  ).isRequired
};

const styles = StyleSheet.create({
  itemContainer: {
    padding: 10,
    backgroundColor: '#f1f1f1',
    borderRadius: 10,
    marginRight: 5,
    marginTop: 5,
  },
  imageContainer: {
    alignItems: 'center',
  },
  image: {
    width: 75,
    height: 100,
    marginBottom: 20,
    marginTop: 10,
  },
  pinName: {
    fontSize: 16,
    fontWeight: 'bold',
    marginTop: 20,
    textAlign: 'center',
  },
  
});

export default PinsSlide;
