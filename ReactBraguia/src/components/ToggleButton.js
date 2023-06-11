import React, { useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';

const ToggleButton = ({ onToggle }) => {
  const [isRunning, setIsRunning] = useState(false);

  const handlePress = () => {
    const newValue = !isRunning;
    setIsRunning(newValue);
    if (onToggle) {
      onToggle(newValue);
    }
  };

  return (
    <TouchableOpacity style={styles.container} onPress={handlePress}>
      <Text style={styles.text}>{isRunning ? 'Stop' : 'Start'}</Text>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  container: {
    backgroundColor: 'red',
    padding: 10,
    borderRadius: 5,
    alignSelf: 'center',
    marginTop: 20,
  },
  text: {
    color: 'white',
    fontSize: 18,
  },
});

export default ToggleButton;
