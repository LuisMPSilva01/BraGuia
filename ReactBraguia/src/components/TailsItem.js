import { TouchableOpacity, View, Text, Image } from 'react-native';
import { useNavigation } from '@react-navigation/native';


const TrailsItem = ({ trail, onPress }) => {

    const navigation = useNavigation();

    const handleTrailPress = (trail) => {
    console.log('Trail pressed:', trail.name);
    // Navigate to the details screen with the selected trail
    //navigation.navigate('Details', { trail });
    };
    return(
    <TouchableOpacity
      style={{ padding: 20, flexDirection: 'row', alignItems: 'center' }}
      onPress={() => handleTrailPress(trail)}
    >
      <Image source={{ uri: trail.image }} style={{ width: 50, height: 50, marginRight: 10 }} />
      <View>
        <Text>{trail.name}</Text>
        <Text>Duration: {trail.duration}</Text>
        <Text>Difficulty: {trail.difficulty}</Text>
      </View>
    </TouchableOpacity>
    )
};


export default TrailsItem;