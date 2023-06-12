import { Platform } from 'react-native';
import * as Device from 'expo-device';
import * as Location from 'expo-location';

export default async function LocationTrack() {
  if (Platform.OS === 'android' && !Device.isDevice) {
    console.log('Oops, this will not work on Snack in an Android Emulator. Try it on your device!');
    return null;
  }
  try {
    console.log("Location updated")
    let { status } = await Location.requestForegroundPermissionsAsync();
    if (status !== 'granted') {
      console.log('Permission to access location was denied');
      return null;
    }
    
    let location = await Location.getCurrentPositionAsync({});
    console.log(location);
    return location;
  } catch (error) {
    console.log('Error fetching location');
    return null;
  }
}