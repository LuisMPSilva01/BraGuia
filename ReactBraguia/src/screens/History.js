import React from 'react';
import { View, Text, StyleSheet, Image } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import BottomBar from '../components/BottomBar';
import { useSelector } from 'react-redux';


function formatTime(timeInSeconds) {
  const hours = Math.floor(timeInSeconds / 3600);
  const minutes = Math.floor((timeInSeconds % 3600) / 60);
  const seconds = timeInSeconds % 60;

  let formattedTime = "";

  if (hours > 0) {
    formattedTime += `${hours} ${hours === 1 ? 'Hour' : 'Hours'}`;
  }

  if (minutes > 0) {
    formattedTime += `${hours > 0 ? ', ' : ''}${minutes} ${minutes === 1 ? 'Minute' : 'Minutes'}`;
  }

  if (seconds > 0) {
    formattedTime += `${hours > 0 || minutes > 0 ? ', and ' : ''}${seconds} ${seconds === 1 ? 'Second' : 'Seconds'}`;
  }

  return formattedTime;
}

const History = () => {
  const tripsMetaData = useSelector((state) => state.trips.tripsVal);
  const trailsMetaData = useSelector((state) => state.appData.trails);

  const getTrailById = (trailId) => {
    return trailsMetaData.find((trail) => trail.id === trailId);
  };

  const getPin_name_byId = (pinId, pins) => {
    const pin = pins.find((pin) => pin.id === pinId);
    return pin ? pin.pin_name : null;
  };
  

  

  return (
    <View style={styles.container}>
      <View style={styles.content}>
        {tripsMetaData.map((trip, index) => {
          const trail = getTrailById(trip.trailId);
          const pins = trail.edges.map(edge => edge.edge_start).concat(trail.edges[trail.edges.length - 1].edge_end);
          
          console.log(pins);
          return (
            <View style={styles.coolSquare}>
            <View key={index} style={styles.item}>
              {trail && (
                <Image source={{ uri: trail.trail_img }} style={styles.image} />
              )}
              <View style={styles.itemContent}>
                {trail && (
                  <View style={styles.trailInfo}>
                    <Ionicons name="trail-sign-outline" size={20} color="black" style={styles.icon} />
                    <Text style={styles.label}>{trail.trail_name}</Text>
                  </View>
                )}
                {trail && (
                  <View style={styles.trailInfo}>
                    <Ionicons name="alert-circle-outline" size={20} color="black" style={styles.icon} />
                    <Text style={styles.label}>{trail.trail_difficulty}</Text>
                  </View>
                )}
                {trail && (
                  <View style={styles.trailInfo}>
                    <Ionicons name="time-outline" size={20} color="black" style={styles.icon} />
                    <Text style={styles.label}>{trail.trail_duration} minutes</Text>
                  </View>
                )}
                {/* Add more trail data as needed */}
                <View style={styles.trailInfo}>
                  <Ionicons name="checkmark-done-outline" size={20} color="black" style={styles.icon} />
                  <Text style={styles.label}>{trip.completePercentage} %</Text>
                </View>
                
                <View style={styles.trailInfo}>
                  <Ionicons name="time-outline" size={20} color="black" style={styles.icon} />
                  <Text style={styles.label}>Time spent: {formatTime(trip.timeTaken)}</Text>
                </View>

                <View style={styles.trailInfo}>
                  <Ionicons name="map-outline" size={20} color="black" style={styles.icon} />
                  <Text style={styles.label}>
                    Visited Places: {trip.visitedPlaces.length > 0 ? trip.visitedPlaces.map((pinId) => getPin_name_byId(pinId, pins)).join(', ') : 'None'}
                  </Text>
                </View>
              </View>
            </View>
            </View>
          );
        })}
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
  item: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 10,
  },
  image: {
    width: 100,
    height: 150,
    marginRight: 10,
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
  text: {
    fontSize: 20,
    fontWeight: 'bold',
  },
  coolSquare: {
    borderWidth: 2,
    width:350,
    borderColor: 'gray',
    padding: 10,
    borderRadius: 10,
    marginBottom: 10,
  },
  
});

export default History;
