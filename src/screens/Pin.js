import React, { useState, useEffect } from 'react';
import { Text, View, StyleSheet, Image, TouchableOpacity } from 'react-native';
import { Audio } from 'expo-av';
import { Video } from 'expo-av';
import PropTypes from 'prop-types';

import noImageAvailable from '../../assets/no_image_available.jpg';

const Pin = (props) => {
  const { pin } = props.route.params;

  const image = pin.media.find((media) => media.media_type === 'I');
  const video = pin.media.find((media) => media.media_type === 'V');
  const audio = pin.media.find((media) => media.media_type === 'R');

  const pinInfo = {
    name: pin.pin_name,
    desc: pin.pin_desc,
    image: image ? image.media_file : null,
    video: video ? video.media_file : null,
    audio: audio ? audio.media_file : null
  };

  const [audioPlayback, setAudioPlayback] = useState(null);
  const [isPlaying, setIsPlaying] = useState(false);

  useEffect(() => {
    return () => {
      if (audioPlayback) {
        audioPlayback.unloadAsync();
      }
    };
  }, []);

  const startAudioPlayback = async () => {
    try {
      const newAudioPlayback = new Audio.Sound();
      await newAudioPlayback.loadAsync({ uri: pinInfo.audio });
      await newAudioPlayback.playAsync();
      setAudioPlayback(newAudioPlayback);
      setIsPlaying(true);
    } catch (error) {
      console.log('Error playing audio:', error);
    }
  };

  const stopAudioPlayback = async () => {
    try {
      if (audioPlayback) {
        await audioPlayback.stopAsync();
        await audioPlayback.unloadAsync();
        setAudioPlayback(null);
        setIsPlaying(false);
      }
    } catch (error) {
      console.log('Error stopping audio:', error);
    }
  };

  const handleAudioPlayback = () => {
    if (isPlaying) {
      stopAudioPlayback();
    } else {
      startAudioPlayback();
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.header}>{pinInfo.name}</Text>

      <View style={styles.contentContainer}>
        {pinInfo.image != null ? (
          <Image source={{ uri: pinInfo.image }} style={styles.image} />
        ) : (
          <Image source={noImageAvailable} style={styles.image} />
        )}

        {pinInfo.video != null && (
          <Video
            source={{ uri: pinInfo.video }}
            style={styles.video}
            useNativeControls
            resizeMode="contain"
          />
        )}
      </View>

      {pinInfo.audio != null && (
        <TouchableOpacity style={styles.audioButton} onPress={handleAudioPlayback}>
          <Text style={styles.audioButtonText}>{isPlaying ? 'Stop Audio' : 'Play Audio'}</Text>
        </TouchableOpacity>
      )}
      <Text>{pinInfo.desc}</Text>
    </View>
  );
};

Pin.propTypes = {
  route: PropTypes.shape({
    params: PropTypes.shape({
      pin: PropTypes.shape({
        pin_name: PropTypes.string.isRequired,
        pin_desc: PropTypes.string.isRequired,
        media: PropTypes.arrayOf(
          PropTypes.shape({
            media_type: PropTypes.string.isRequired,
            media_file: PropTypes.string.isRequired
          })
        ).isRequired
      }).isRequired
    }).isRequired
  }).isRequired
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  },
  header: {
    fontSize: 24,
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 20
  },
  contentContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 20
  },
  image: {
    width: 150,
    height: 150
  },
  video: {
    width: 150,
    height: 150
  },
  audioButton: {
    padding: 10,
    backgroundColor: '#eaeaea',
    borderRadius: 5
  },
  audioButtonText: {
    fontSize: 16
  }
});

export default Pin;
