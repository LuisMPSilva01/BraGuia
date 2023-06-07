import React, {useEffect, useState} from 'react';
import {ActivityIndicator, FlatList, Text, View} from 'react-native';
import Trails from './src/components/Trails';
import { Logs } from 'expo'

Logs.enableExpoCliLogging()

const App = () => {

  return (
    <View style={{flex: 1, padding: 24}}>

        <Trails/>

    </View>

  );
};

export default App;