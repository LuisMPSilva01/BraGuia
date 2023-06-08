import { TouchableOpacity, View, Text, Image } from 'react-native';
import { useNavigation } from '@react-navigation/native';


const TrailsItem = ({ trail,key }) => {

    const navigation = useNavigation();

    const handleTrailPress = (trail) => {
    // Navigate to the details screen with the selected trail
    navigation.navigate('Trail', trail);
    };
    return(
    <TouchableOpacity
      style={{ padding: 20, flexDirection: 'row', alignItems: 'center' }}
      onPress={() => handleTrailPress({trail})}
    >
      <Image source={{ uri: trail.trail_img }} style={{ width: 50, height: 50, marginRight: 10 }} />
      <View>
        <Text>{trail.trail_name}</Text>
        <Text>Duration: {trail.trail_duration}</Text>
        <Text>Difficulty: {trail.trail_difficulty}</Text>
      </View>
    </TouchableOpacity>
    )
};


export default TrailsItem;