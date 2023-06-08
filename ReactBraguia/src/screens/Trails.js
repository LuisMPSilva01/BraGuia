import React, { useEffect, useState } from 'react';
import { ScrollView, View, Text } from 'react-native';
import TrailsItem from '../components/TailsItem';
import SearchBar from '../components/SearchBar';
import BottomBar from '../components/BottomBar';
import { StyleSheet } from 'react-native';


const Trails = () => {
  const [trails, setTrails] = useState([]); 
  const [searchResults, setSearchResults] = useState([]);

  const getTrails = async () => {
    try {
      const response = await fetch('https://c5a2-193-137-92-29.eu.ngrok.io/trails');
      const json = await response.json();
      setTrails(json); // Update the trails state with the trail names and image URLs
      setSearchResults(json);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    getTrails();
  }, []);

  const handleSearch = (query) => {;

    if(query==""){
      setSearchResults(trails)
    }else{
      const filteredData = trails.filter((trail) => {
        return trail.name.includes(query)
      });
      setSearchResults(filteredData);
    }
    
 
  };
  return (
    <View style={styles.container}>
      <SearchBar onSearch={handleSearch}/>
      <ScrollView>
        {searchResults.length > 0 ? (
          searchResults.map((trail,index) => (
            <TrailsItem trail={trail} key={index}/>
          ))
        ) : (
        <View style={{ padding: 20 }}>
          <Text>No trails available</Text>
        </View>
        )}
      </ScrollView>
      <BottomBar />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  content: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});

export default Trails;

