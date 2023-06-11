import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, Switch, TextInput } from 'react-native';
import BottomBar from '../components/BottomBar';
import { debounce } from 'lodash';
import { useSelector, useDispatch } from 'react-redux';
import { setDistanceRedux } from '../actions/user'

const Definitions = () => {
  const [locationEnabled, setLocationEnabled] = useState(false);
  const [darkModeEnabled, setDarkModeEnabled] = useState(false);
  const [distance, setDistance] = useState('0'); // Default value of 10 km
  const initialDistance = useSelector((state) => state.distance.distanceVal);
  const dispatch = useDispatch();

  useEffect(() => {
    setDistance(initialDistance.toString())
  }, []);

  
  const handleLocationToggle = () => {
    setLocationEnabled(!locationEnabled);
    console.log(
      'TODO: FAZER ALGUMA COISA UTIL COM ISTO!!!!!!!!!! -> Location toggle:',
      !locationEnabled ? 'enabled' : 'disabled'
    );
  };

  const handleDarkModeToggle = () => {
    setDarkModeEnabled(!darkModeEnabled);
    console.log(
      'TODO: FAZER ALGUMA COISA UTIL COM ISTO!!!!!!!!!! -> Dark mode toggle:',
      !darkModeEnabled ? 'enabled' : 'disabled'
    );
  };

  const handleDistanceChange = (value) => {
    const parsedValue = Number(value);
    if (!isNaN(parsedValue)) {
      setDistance(parsedValue.toString());
      dispatch(setDistanceRedux(parsedValue));
    }
  };
  
  

  return (
    <View style={styles.container}>
      <View style={styles.content}>
        <View style={styles.row}>
          <Text style={styles.label}>Enable Location Service:</Text>
          <Switch value={locationEnabled} onValueChange={handleLocationToggle} />
        </View>
        <View style={styles.row}>
          <Text style={styles.label}>Enable Dark Mode:</Text>
          <Switch value={darkModeEnabled} onValueChange={handleDarkModeToggle} />
        </View>
        <View style={styles.row}>
          <Text style={styles.label}>GeoDistance from pins to your location:</Text>
          <TextInput
            value={distance}
            onChangeText={handleDistanceChange}
            keyboardType="numeric"
            style={styles.numberInput}
          />
        </View>
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
  row: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 10,
    marginLeft:10,
    marginRight:10,
  },
  label: {
    flex: 1,
    marginRight: 10,
  },
  numberInput: {
    marginLeft: 10,
    borderWidth: 1,
    borderColor: 'gray',
    padding: 5,
    width: 70,
  },
});

export default Definitions;
