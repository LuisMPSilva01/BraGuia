import React from 'react';
import { View, TextInput, StyleSheet } from 'react-native';
import PropTypes from 'prop-types';

const SearchBar = ({ onSearch }) => {
  const handleChangeText = (text) => {
    onSearch(text);
  };

  return (
    <View style={styles.container}>
      <TextInput
        style={styles.input}
        placeholder="Search"
        onChangeText={handleChangeText}
      />
    </View>
  );
};

SearchBar.propTypes = {
  onSearch: PropTypes.func.isRequired,
};

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#f0f0f0',
    borderRadius: 8,
    padding: 8,
    margin: 16,
  },
  input: {
    flex: 1,
    marginRight: 8,
  },
});

export default SearchBar;
