import React, { useState, useEffect } from 'react';
import { Text, View, StyleSheet, Image } from 'react-native';
import ToggleButton from '../components/ToggleButton';
import { useNavigation, useFocusEffect } from '@react-navigation/native';
import PinsSlide from '../components/PinsSlide';
import MapView, { Marker, Polyline } from 'react-native-maps';
import { useSelector, useDispatch } from 'react-redux';
import { addTrip } from '../actions/user';
import LocationTrack from '../backgroundServices/LocationTracker';

const Trail = ({ route }) => {
  const navigation = useNavigation();
  const dispatch = useDispatch();

  const { trail } = route.params;
  const initialRegion = { 
    latitude: trail.edges[0].edge_start.pin_lat,
    longitude: trail.edges[0].edge_start.pin_lng,
    latitudeDelta: 0.0922,
    longitudeDelta: 0.0421,
  };
  const coordinates = trail.edges.map((edge) => ({//TODO update edges
    latitude: edge.edge_start.pin_lat,
    longitude: edge.edge_start.pin_lng,
  }));

  const [startTime, setStartTime] = useState(null);
  const [isToggled, setToggle] = useState(false);
  const [visitedTrips, setVisitedTrips] = useState([]);
  const handleButtonToggle = () => {
    setToggle(!isToggled);
  };


  useEffect(() => {
    const getLocation = () => {
      LocationTrack()
        .then((location) => {
          console.log(location);
  
          const { latitude, longitude } = location.coords;
  
          // Check if the current location is close to any of the trail edges
  
          const closeEdges = trail.edges.filter((edge) => {
            const { pin_lat, pin_lng } = edge.edge_start;
            const latDiff = Math.abs(pin_lat - latitude);
            const lngDiff = Math.abs(pin_lng - longitude);
  
            // Check if the edge is not already present in the visitedTrips array
            const isVisited = visitedTrips.some((visitedId) => visitedId == edge.edge_start.id);
            console.log("LatDiff:", latDiff);
            console.log("LngDiff:", lngDiff);
            console.log(isVisited);
            return latDiff <= 0.9 && lngDiff <= 0.9 && !isVisited; // Adjust the tolerance values as needed
          });
  
          // If close edges are found, add them to the visitedTrips state
          console.log("Close edges:", closeEdges.length);
          if (closeEdges.length > 0) {
            setVisitedTrips((prevVisitedTrips) => {
              const updatedVisitedTrips = [...prevVisitedTrips, ...closeEdges.map((edge) => edge.edge_start.id)];
              return updatedVisitedTrips;
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
  }, [isToggled, setVisitedTrips, trail.edges, visitedTrips]);
  
  
  



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
        const completePercentage = (visitedTrips.length / trail.edges.length)*100; // TODO: update edges
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
  }, [isToggled, navigation, visitedTrips, trail.edges]);
  

  return (
    <View style={styles.container}>
      <Text style={styles.title}>{trail.trail_name}</Text>
      <Image source={{ uri: trail.trail_img }} style={{ width: 150, height: 150 }} />
      <ToggleButton onToggle={handleButtonToggle} />
      <MapView style={styles.map} initialRegion={initialRegion}>
        <Polyline coordinates={coordinates} strokeWidth={2} strokeColor="red" />
        { trail.edges.map((edge) => (
          <Marker key={edge.edge_start.id} 
                  coordinate={{
                    latitude: edge.edge_start.pin_lat,
                    longitude: edge.edge_start.pin_lng,
                    }}
                  title={edge.edge_start.title} />
        ))}
      </MapView>
      <PinsSlide pins={trail.edges}/>
      
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

