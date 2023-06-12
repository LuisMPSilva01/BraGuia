import React, { useEffect, useState, useContext } from 'react';
import { View, Text, StyleSheet, Switch, TextInput } from 'react-native';
import { useSelector, useDispatch } from 'react-redux';
import { setDistanceRedux, setDarkMode } from '../actions/user'
import { EventRegister } from 'react-native-event-listeners'
import themeContext from '../theme/themeContext';

const Definitions = () => {
  const theme = useContext(themeContext)

  const [darkModeEnabled, setDarkModeEnabled] = useState(false);
  const [distance, setDistance] = useState('0');
  const initialDistance = useSelector((state) => state.distance.distanceVal);
  const dispatch = useDispatch();

  const initialDm = useSelector((state) => state.darkMode_reducer.darkMode);

  useEffect(() => {
    setDistance(initialDistance.toString())
    setDarkModeEnabled(initialDm)
  }, []);

  const handleDistanceChange = (value) => {
    const parsedValue = parseFloat(value.replace(',', '.'));
    if (!isNaN(parsedValue)) {
      setDistance(value);
      dispatch(setDistanceRedux(parsedValue));
    }
  };

  const handleDarkModeToggle = (value) => {
    setDarkModeEnabled(value);
    dispatch(setDarkMode(value));
    EventRegister.emit('ChangeTheme', value);
  };

  return (
    <View style={[styles.container, {backgroundColor:theme.backgroundColor}]}>
      <View style={styles.content}>
        <View style={styles.row}>
          <Text style={[styles.label, {color:theme.color}]}>Enable Dark Mode:</Text>
          <Switch 
            value={darkModeEnabled} 
            onValueChange={handleDarkModeToggle}
          />
        </View>
        <View style={styles.row}>
          <Text style={[styles.label, {color:theme.color}]}>GeoDistance from pins to your location:</Text>
          <TextInput
            value={distance}
            onChangeText={handleDistanceChange}
            keyboardType="numeric"
            style={[styles.numberInput, {color:theme.color}]}
          />
        </View>
      </View>
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
