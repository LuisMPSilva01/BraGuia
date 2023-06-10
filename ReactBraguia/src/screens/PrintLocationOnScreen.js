import React, { useEffect, useState } from 'react';
import { View, StyleSheet, Button } from 'react-native';
import LocationTrack from '../backgroundServices/LocationTracker';

export default function PrintLocationOnScreen() {
  const [shouldTrack, setShouldTrack] = useState(true); // Initial value is true

  useEffect(() => {
    const getLocation = async () => {
      const location = await LocationTrack();
      console.log(location);
    };

    const interval = setInterval(() => {
      if (shouldTrack) {
        getLocation();
      }
    }, 2000); // Repeat every 2 seconds

    return () => {
      clearInterval(interval); // Cleanup the interval when the component unmounts
    };
  }, [shouldTrack]); // Add shouldTrack to the dependency array

  const toggleTracking = () => {
    setShouldTrack(!shouldTrack);
  };

  return (
    <View style={styles.container}>
      <Button title={shouldTrack ? 'Stop Tracking' : 'Start Tracking'} onPress={toggleTracking} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
  },
  paragraph: {
    fontSize: 18,
    textAlign: 'center',
  },
});
