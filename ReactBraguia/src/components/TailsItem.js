import React from 'react';
import { TouchableOpacity, View, Text, Image, StyleSheet } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useNavigation } from '@react-navigation/native';

const TrailsItem = ({ trail }) => {
  const navigation = useNavigation();

  const handleTrailPress = (trail) => {
    // Navigate to the details screen with the selected trail
    navigation.navigate('Trail', { trail });
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
        <View style={styles.itemContent}>
          <View style={styles.trailInfo}>
            <Ionicons name="trail-sign-outline" size={20} color="black" style={styles.icon} />
            <Text style={styles.label}>{trail.trail_name}</Text>
          </View>
          <View style={styles.trailInfo}>
            <Ionicons name="time-outline" size={20} color="black" style={styles.icon} />
            <Text style={styles.label}>{trail.trail_duration} minutes</Text>
          </View>
          <View style={styles.trailInfo}>
            <Ionicons name="alert-circle-outline" size={20} color="black" style={styles.icon} />
            <Text style={styles.label}>{trail.trail_difficulty}</Text>
          </View>
        </View>
      </View>
    </TouchableOpacity>
  );
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
