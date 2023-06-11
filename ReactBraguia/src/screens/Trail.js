import React, { useState, useEffect } from 'react';
import { Text, View, StyleSheet, TouchableOpacity,Image } from 'react-native';
import ToggleButton from '../components/ToggleButton';
import { useNavigation, useFocusEffect } from '@react-navigation/native';
import PinsSlide from '../components/PinsSlide';
import MapView, { Marker, Polyline } from 'react-native-maps';
import { useSelector, useDispatch } from 'react-redux';
import { addTrip } from '../actions/user';
import LocationTrack from '../backgroundServices/LocationTracker';
import { useNotification } from '../components/NotificationManager';
import * as Notifications from 'expo-notifications';
import * as Linking from 'expo-linking';

const Trail = ({ route }) => {
  const navigation = useNavigation();
  const dispatch = useDispatch();
  const GeoDistance = useSelector((state) => state.distance.distanceVal);
  console.log(GeoDistance);
  const { trail } = route.params;

  const pins = trail.edges.map(edge => edge.edge_start).concat(trail.edges[trail.edges.length - 1].edge_end);

  const initialRegion = { 
    latitude: pins[0].pin_lat,
    longitude: pins[0].pin_lng,
    latitudeDelta: 0.0922,
    longitudeDelta: 0.0421,
  };
  const coordinates = pins.map((pin) => ({
    latitude: pin.pin_lat,
    longitude: pin.pin_lng,
  }));

  const { expoPushToken, sendPushNotification } = useNotification();
  const [startTime, setStartTime] = useState(null);
  const [isToggled, setToggle] = useState(false);
  const [visitedTrips, setVisitedTrips] = useState([]);
  const [notificationQueue, setNotificationQueue] = useState([]);
  const handleButtonToggle = () => {
    setToggle(!isToggled);
  };

  const findPinById = (id) => {
    // console.log("ID:", id);
    // console.log("Trail:", trail);
    for (let i = 0; i < pins.length; i++) {
      const pin = pins[i];
      // console.log("Edge:", edge);
      if (pin.id === id) {
        return pin;
      }
    }
  
    return null;
  };
  

  useEffect(() => {
    const subscription = Notifications.addNotificationResponseReceivedListener(response => {
      const pin = findPinById(response.notification.request.content.data.id);
      console.log(pin);
      if(pin){
        navigation.navigate('Pin', { pin: pin});
      }
      else{
        alert('Old notification, forget to clear?');
      }
    });
    return () => subscription.remove();
  }, []);
  
  useEffect(() => {
    const processNotificationQueue = async () => {
      if (notificationQueue.length > 0) {
        // Perform an action on each item in the previous queue
        for (const item of notificationQueue) {
          await sendPushNotification(expoPushToken, item.pin_name, item.pin_desc, item.id);
        }
  
        // Clear the notification queue
        setNotificationQueue([]);
      }
    };
  
    processNotificationQueue();
  }, [setNotificationQueue, notificationQueue]);
  
  

  useEffect(() => {
    const getLocation = () => {
      LocationTrack()
        .then((location) => {
          const { latitude, longitude } = location.coords;
  
          // Check if the current location is close to any of the trail pins
  
          const closePins = pins.filter((pin) => {
            const { pin_lat, pin_lng } = pin;
            const latDiff = Math.abs(pin_lat - latitude);
            const lngDiff = Math.abs(pin_lng - longitude);
  
            // Check if the pins is not already present in the visitedTrips array
            const isVisited = visitedTrips.some((visitedId) => visitedId == pin.id);
            return latDiff <= 0.9 && lngDiff <= 0.9 && !isVisited; // Adjust the tolerance values as needed
          });
  
          // If close pins are found, add them to the visitedTrips state
          if (closePins.length > 0) {
            setVisitedTrips((prevVisitedTrips) => {
              const updatedVisitedTrips = [...prevVisitedTrips, ...closePins.map((pin) => pin.id)];
              return updatedVisitedTrips;
            });
            setNotificationQueue((prevQueue) => {
              const updatedQueue = [...prevQueue, ...closePins];
              return updatedQueue;
            });
          }
        })
        .catch((error) => {
          console.error("Error getting location:", error);
        });
    };
  
    const interval = setInterval(() => {
      if (isToggled) {
        getLocation();
      }
    }, 2000); // Repeat every 2 seconds
  
    return () => {
      clearInterval(interval); // Cleanup the interval when the component unmounts
    };
  }, [isToggled, setVisitedTrips, visitedTrips,notificationQueue]);

  // Listen for changes in the isToggled state
  useEffect(() => {
    if (isToggled) {
      setStartTime(new Date().getTime());
  
      const unsubscribe = navigation.addListener('beforeRemove', (e) => {
        e.preventDefault();
      });
  
      return () => {
        unsubscribe();
      };
    } else {
      if(startTime!=null){
        const endTime = new Date().getTime();
        const elapsedTime = endTime - startTime;
        const completePercentage = (visitedTrips.length / pins.length)*100;
        const trip = {
          trailId: trail.id,
          visitedPlaces: visitedTrips,
          completePercentage: completePercentage,
          timeTaken: elapsedTime
        };
        setStartTime(null);
        setVisitedTrips([]);
        dispatch(addTrip(trip));
      }
      return () => {};
    }
  }, [isToggled, navigation, visitedTrips,notificationQueue]);
  
  const startNavigation = () => {
    const route = pins.map((pin) => pin.pin_lat+","+pin.pin_lng);
    const lastIndex = route.length - 1;
    const destination = route[lastIndex];
    const waypoints = route.slice(0, lastIndex).join('|');
    Linking.openURL(`https://www.google.com/maps/dir/?api=1&destination=${destination}&waypoints=${waypoints}`);
  };
  
  return (
    <View style={styles.container}>
      <Text style={styles.title}>{trail.trail_name}</Text>
      <Image source={{ uri: trail.trail_img }} style={{ width: 150, height: 150 }} />
      <ToggleButton onToggle={handleButtonToggle} />
      {isToggled ? (
        <>
          <TouchableOpacity style={styles.buttonStyle} onPress={startNavigation}>
            <Text style={styles.text}> Navigate </Text>
          </TouchableOpacity>
        </>
      ) : (
        <></>
      )}

      <MapView style={styles.map} initialRegion={initialRegion}>
        <Polyline coordinates={coordinates} strokeWidth={2} strokeColor="red" />
        { pins.map((pin) => (
          <Marker key={pin.id} 
                  coordinate={{
                    latitude: pin.pin_lat,
                    longitude: pin.pin_lng,
                    }}
                  title={pin.title} />
        ))}
      </MapView>
      <PinsSlide pins={pins}/>
      
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    backgroundColor: '#F5F5F5',
    height: 60,
  },
  buttonStyle: {
    backgroundColor: 'red',
    padding: 10,
    borderRadius: 5,
    alignSelf: 'center',
    marginTop: 20,
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    paddingTop: 50,
    paddingBottom: 50,
  },
  map: {
    width: '70%',
    height: '30%',
    marginTop: 20,
  },
});

export default Trail;

