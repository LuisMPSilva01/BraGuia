import React, { useEffect, useState, useContext} from 'react';
import { ScrollView, View, Text } from 'react-native';
import TrailsItem from '../components/TailsItem';
import SearchBar from '../components/SearchBar';
import { StyleSheet } from 'react-native';
import { useSelector } from 'react-redux';

import themeContext from '../theme/themeContext';

const Trails = () => {
  const [trails, setTrails] = useState([]); 
  const [searchResults, setSearchResults] = useState([]);
  const trailsMetaData = useSelector((state) => state.appData.trails);

  const theme = useContext(themeContext)

  useEffect(() => {
    if(trailsMetaData){
      setTrails(trailsMetaData);
      setSearchResults(trailsMetaData);
    }
  }, []);

  const handleSearch = (query) => {;

    if(query==""){
      setSearchResults(trails)
    }else{
      const filteredData = trails.filter((trail) => {
        return trail.trail_name.includes(query)
      });
      setSearchResults(filteredData);
    }
    
 
  };
  return (
    <View style={[styles.container,  {backgroundColor:theme.backgroundColor}]}>
      <SearchBar onSearch={handleSearch}/>
      <ScrollView>
        {searchResults.length > 0 ? (
          searchResults.map((trail,index) => (
            <TrailsItem trail={trail} key={index}/>
          ))
        ) : (
        <View style={{ padding: 20 }}>
          <Text style =  {{color:theme.color}}>No trails available</Text>
        </View>
        )}
      </ScrollView>
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

