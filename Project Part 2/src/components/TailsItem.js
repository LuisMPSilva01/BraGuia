import React, { useContext } from 'react';
import { TouchableOpacity, View, Text, Image, StyleSheet } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useNavigation } from '@react-navigation/native';
import { useSelector } from 'react-redux';
import PropTypes from 'prop-types';
import themeContext from '../theme/themeContext';

const TrailsItem = ({ trail }) => {
  const navigation = useNavigation();

  const theme = useContext(themeContext);
  const userType = useSelector((state) => state.user.username.user_type);
  const handleTrailPress = (trail) => {
    if (userType === 'Premium') {
      // Navigate to the details screen with the selected trail
      navigation.navigate('Trail', { trail });
    } else {
      alert('User is not premium');
    }
  };

  return (
    <TouchableOpacity
      style={styles.container}
      onPress={() => handleTrailPress(trail)}
    >
      <View style={styles.coolSquare}>
        <View style={styles.imageContainer}>
          <Image source={{ uri: trail.trail_img }} style={styles.image} />
        </View>
        <View style={[styles.itemContent, { backgroundColor: theme.backgroundColor }]}>
          <View style={[styles.trailInfo, { color: theme.color }]}>
            <Ionicons
              name="trail-sign-outline"
              size={20}
              color="black"
              style={[styles.icon, { color: theme.color }]}
            />
            <Text style={[styles.label, { color: theme.color }]}>{trail.trail_name}</Text>
          </View>
          <View style={[styles.trailInfo, { color: theme.color }]}>
            <Ionicons
              name="time-outline"
              size={20}
              color="black"
              style={[styles.icon, { color: theme.color }]}
            />
            <Text style={[styles.label, { color: theme.color }]}>{trail.trail_duration} minutes</Text>
          </View>
          <View style={[styles.trailInfo, { color: theme.color }]}>
            <Ionicons
              name="alert-circle-outline"
              size={20}
              color="black"
              style={[styles.icon, { color: theme.color }]}
            />
            <Text style={[styles.label, { color: theme.color }]}>{trail.trail_difficulty}</Text>
          </View>
        </View>
      </View>
    </TouchableOpacity>
  );
};

TrailsItem.propTypes = {
  trail: PropTypes.shape({
    trail_img: PropTypes.string.isRequired,
    trail_name: PropTypes.string.isRequired,
    trail_duration: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
    trail_difficulty: PropTypes.string.isRequired,
  }).isRequired,
};

const styles = StyleSheet.create({
  container: {
    padding: 20,
    flexDirection: 'row',
    alignItems: 'center',
  },
  coolSquare: {
    borderWidth: 2,
    width: 350,
    borderColor: 'gray',
    padding: 10,
    borderRadius: 10,
    flexDirection: 'row',
    alignItems: 'center',
  },
  imageContainer: {
    marginRight: 10,
  },
  image: {
    width: 50,
    height: 75,
  },
  itemContent: {
    flex: 1,
  },
  trailInfo: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 5,
  },
  icon: {
    marginRight: 5,
  },
  label: {
    fontSize: 14,
    maxWidth: 200,
  },
});

export default TrailsItem;
