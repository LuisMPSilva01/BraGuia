import React, { useEffect, useState } from 'react';
import { ScrollView, View, Text } from 'react-native';
import TrailsItem from '../components/TailsItem';

const Trails = () => {
  const [trails, setTrails] = useState([]); // Declare trails in the component's state

  const getTrails = async () => {
    try {
      const response = await fetch('https://c5a2-193-137-92-29.eu.ngrok.io/trails');
      const json = await response.json();
      const trailData = json.map((trail) => ({
        name: trail.trail_name,
        image: trail.trail_img,
        duration: trail.trail_duration,
        difficulty: trail.trail_difficulty
      }));
      setTrails(trailData); // Update the trails state with the trail names and image URLs
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    getTrails();
  }, []);

  return (
    <ScrollView>
      {trails.length > 0 ? (
        trails.map((trail, index) => (
          <TrailsItem trail={trail}/>
        ))
      ) : (
        <View style={{ padding: 20 }}>
          <Text>No trails available</Text>
        </View>
      )}
    </ScrollView>
  );
};

export default Trails;

