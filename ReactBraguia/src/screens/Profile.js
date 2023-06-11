import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useSelector } from 'react-redux';
import { useNavigation} from '@react-navigation/native';

const Profile = () => {
  const navigation = useNavigation();
  const handleTrailsHistory = () => {
    navigation.navigate('Trails History');
  };

  const userMetaData = useSelector((state) => state.user.username);
  const {
    username,
    email,
    date_joined,
    user_type,
  } = userMetaData;

  const formattedDateJoined = new Date(date_joined).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });

  return (
    <View style={styles.container}>
      <View style={styles.content}>
        <View style={styles.profileContainer}>
          <View style={styles.backgroundContainer}>
            <Ionicons name="md-person-circle" size={150} color="#FFFFFF" />
            <Text style={styles.username}>{username}</Text>
            <Text style={styles.infoTitle}>User type</Text>
            <Text style={styles.userType}>{user_type}</Text>
          </View>
        </View>

        <View style={styles.detailContainer}>
          <View style={styles.detailRow}>
            <Ionicons name="md-person-outline" size={36} color="#000000" />
            <Text style={styles.detailText}>{username}</Text>
          </View>
          <View style={styles.detailRow}>
            <Ionicons name="md-mail" size={36} color="#000000" />
            {email ? (
              <Text style={styles.detailText}>{email}</Text>
            ) : (
              <Text style={styles.detailText}>No email available</Text>
            )}
          </View>
          <View style={styles.detailRow}>
            <Ionicons name="md-calendar" size={36} color="#000000" />
            <Text style={styles.detailText}>
              Member since {formattedDateJoined}
            </Text>
          </View>
        </View>

        <TouchableOpacity style={styles.button} onPress={handleTrailsHistory}>
          <Ionicons name="md-list" size={24} color="#FFFFFF" />
          <Text style={styles.buttonText}>Trails history</Text>
        </TouchableOpacity>
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
  profileContainer: {
    height: 350,
    width: 350,
    marginBottom: 20,
  },
  backgroundContainer: {
    flex: 1,
    backgroundColor: '#FF0008',
    justifyContent: 'center',
    alignItems: 'center',
  },
  username: {
    marginTop: 10,
    color: '#ffffff',
    fontSize: 21,
    fontWeight: 'bold',
  },
  infoTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#000000',
    marginTop: 10,
  },
  userType: {
    color: '#FFFFFF',
    fontSize: 20,
    fontWeight: 'bold',
  },
  detailContainer: {
    marginBottom: 20,
  },
  detailRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 15,
  },
  detailText: {
    paddingLeft: 20,
    fontWeight: 'bold',
    fontSize: 16,
    color: '#000000',
  },
  button: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#FF0008',
    paddingHorizontal: 10,
    paddingVertical: 10,
    borderRadius: 10,
    width: '50%', // Adjust the width as per your requirements
  },
  buttonText: {
    color: '#FFFFFF',
    fontWeight: 'bold',
    fontSize: 18,
    marginLeft: 5,
    marginRight: 10,
  },
});

export default Profile;
