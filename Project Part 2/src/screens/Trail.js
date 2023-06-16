import React, { useState, useEffect, useContext } from 'react';
import { Text, View, StyleSheet, TouchableOpacity, Image, Dimensions } from 'react-native';
import ToggleButton from '../components/ToggleButton';
import { useNavigation } from '@react-navigation/native';
import PinsSlide from '../components/PinsSlide';
import MapView, { Marker, Polyline } from 'react-native-maps';
import { useSelector, useDispatch } from 'react-redux';
import { addTrip } from '../actions/user';
import LocationTrack from '../backgroundServices/LocationTracker';
import { useNotification } from '../components/NotificationManager';
import * as Notifications from 'expo-notifications';
import * as Linking from 'expo-linking';
import PropTypes from 'prop-types';

import themeContext from '../theme/themeContext';

const Trail = (props) => {
  const navigation = useNavigation();
  const dispatch = useDispatch();
  const GeoDistance = useSelector((state) => state.distance.distanceVal);
  const { trail } = props.route.params;
  const pins = trail.edges.map((edge) => edge.edge_start).concat(trail.edges[trail.edges.length - 1].edge_end);

  const theme = useContext(themeContext);

  const initialRegion = {
    latitude: pins[0].pin_lat,
    longitude: pins[0].pin_lng,
    latitudeDelta: 0.0922,
    longitudeDelta: 0.0421
  };
  const coordinates = pins.map((pin) => ({
    latitude: pin.pin_lat,
    longitude: pin.pin_lng
  }));

  const { expoPushToken, sendPushNotification } = useNotification();
  const [startTime, setStartTime] = useState(null);
  const [isToggled, setToggle] = useState(false);
  const [visitedTrips, setVisitedTrips] = useState([]);
  const [notifications, setNotifications] = useState([]);



  const handleButtonToggle = () => {
    setToggle(!isToggled);
  };

  const findPinById = (id) => {
    for (let i = 0; i < pins.length; i++) {
      const pin = pins[i];
      if (pin.id === id) {
        return pin;
      }
    }

    return null;
  };

  useEffect(() => {
    const subscription = Notifications.addNotificationResponseReceivedListener((response) => {
      const pin = findPinById(response.notification.request.content.data.id);
      console.log(pin);
      if (pin) {
        navigation.navigate('Pin', { pin });
      } else {
        alert('Old notification, forget to clear?');
      }
    });
    return () => subscription.remove();
  }, []);

  useEffect(() => {
    const processNotificationQueue = async () => {
      const newNotifications = visitedTrips.filter((visitedTrip) => {
        // Check if visited trip ID is not present in notifications
        return !notifications.some((notification) => notification === visitedTrip);
      });
      if(newNotifications.length>0){
        // Iterate over new notifications and send push notifications
        for (const visitedTrip of newNotifications) {
          const item = findPinById(visitedTrip); // Replace with your logic to find the pin details
          await sendPushNotification(expoPushToken, item.pin_name, item.pin_desc, item.id);
        }
        setNotifications((prevNotifications) => [...prevNotifications, ...newNotifications]);
      }
    };
  
    processNotificationQueue();
  }, [visitedTrips, notifications]);
  


  useEffect(() => {
    const getLocation = () => {
      LocationTrack()
        .then((location) => {
          const { latitude, longitude } = location.coords;

          const closePins = pins.filter((pin) => {
            const { pin_lat, pin_lng } = pin;
            const latDiff = Math.abs(pin_lat - latitude);
            const lngDiff = Math.abs(pin_lng - longitude);
            return latDiff <= GeoDistance && lngDiff <= GeoDistance;
          });
          console.log(visitedTrips);
          if (closePins.length > 0) {
            setVisitedTrips((prevVisitedTrips) => {
              const updatedVisitedTrips = [
                ...prevVisitedTrips,
                ...closePins.reduce((acc, pin) => {
                  const isVisited = prevVisitedTrips.some((visitedId) => visitedId === pin.id);
                  if (!isVisited) {
                    acc.push(pin.id);
                  }
                  return acc;
                }, [])
              ];
              return updatedVisitedTrips;
            }); 
          }
        })
        .catch((error) => {
          console.error('Error getting location:', error);
        });
    };

    const interval = setInterval(() => {
      if (isToggled) {
        getLocation();
      }
    }, 2000);

    return () => {
      clearInterval(interval);
    };
  }, [isToggled, visitedTrips]);

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
      if (startTime != null) {
        const endTime = new Date().getTime();
        const elapsedTime = endTime - startTime;
        const completePercentage = (visitedTrips.length / pins.length) * 100;
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
  }, [isToggled, navigation, visitedTrips]);

  const startNavigation = () => {
    const route = pins.map((pin) => pin.pin_lat + ',' + pin.pin_lng);
    const lastIndex = route.length - 1;
    const destination = route[lastIndex];
    const waypoints = route.slice(0, lastIndex).join('|');
    Linking.openURL(`https://www.google.com/maps/dir/?api=1&destination=${destination}&waypoints=${waypoints}`);
  };

  return (
    <View style={[styles.container, { backgroundColor: theme.backgroundColor }]}>
      <Text style={[styles.title, { color: theme.color }]}>{trail.trail_name}</Text>
      <Image source={{ uri: trail.trail_img }} style={{ width: 150, height: 150 }} />
      <ToggleButton onToggle={handleButtonToggle} />
      {isToggled ? (
        <TouchableOpacity style={styles.buttonStyle} onPress={startNavigation}>
          <Text style={styles.text}> Navigate </Text>
        </TouchableOpacity>
      ) : null}

      <MapView style={styles.map} initialRegion={initialRegion}>
        <Polyline coordinates={coordinates} strokeWidth={2} strokeColor="red" />
        {pins.map((pin) => (
          <Marker
            key={pin.id}
            coordinate={{
              latitude: pin.pin_lat,
              longitude: pin.pin_lng
            }}
            title={pin.title}
          />
        ))}
      </MapView>
      <PinsSlide pins={pins} />
    </View>
  );
};

Trail.propTypes = {
  route: PropTypes.shape({
    params: PropTypes.shape({
      trail: PropTypes.object.isRequired
    }).isRequired
  }).isRequired
};

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    backgroundColor: '#F5F5F5',
    height: Dimensions.get('window').height,
    marginBottom: 20
  },
  buttonStyle: {
    backgroundColor: 'red',
    padding: 10,
    borderRadius: 5,
    alignSelf: 'center',
    marginTop: 20
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    paddingTop: 50,
    paddingBottom: 50
  },
  map: {
    width: '70%',
    height: '30%',
    marginTop: 20
  }
});

export default Trail;
