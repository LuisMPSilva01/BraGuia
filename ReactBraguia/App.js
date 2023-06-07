import React, {useEffect, useState} from 'react';
import {ActivityIndicator, FlatList, Text, View} from 'react-native';
import { Logs } from 'expo'

Logs.enableExpoCliLogging()

const App = () => {
  const [isLoading, setLoading] = useState(true);
  const [data, setData] = useState([]);

  const getTrails = async () => {
    try {
      const response = await fetch('https://c5a2-193-137-92-29.eu.ngrok.io/trails');
      const json = await response.json();
      const trail = json[0];
      setData(json);
    } catch (error) {
      console.error(error);
    } finally {
      console.log(data);
      setLoading(false);
    }
  };

  useEffect(() => {
    getTrails();
  }, []);

  return (
    <View style={{flex: 1, padding: 24}}>
      {isLoading ? (
        <ActivityIndicator />
      ) : (
        <FlatList
          data={data}
        />
      )}
    </View>
  );
};

export default App;