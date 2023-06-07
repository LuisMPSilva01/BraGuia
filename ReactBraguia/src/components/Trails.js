import React, { useEffect, useState } from 'react';
import { View, Text } from 'react-native';

const Trails = () => {
  const [trails, setTrails] = useState([]); // Declare trails in the component's state

  const getTrails = async () => {
    try {
      const response = await fetch('https://c5a2-193-137-92-29.eu.ngrok.io/trails');
      const json = await response.json();
      const trailNames = json.map((trail) => trail.trail_name);
      setTrails(trailNames); // Update the trails state with the trail names
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    getTrails();
  }, []);

  return (
    <View>
      {trails.length > 0 ? (
        trails.map((trail, index) => <Text key={index}>{trail}</Text>)
      ) : (
        <Text>No trails available</Text>
      )}
    </View>
  );
};

export default Trails;
