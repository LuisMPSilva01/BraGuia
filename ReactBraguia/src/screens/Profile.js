import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import BottomBar from '../components/BottomBar';

const Profile = ({ navigation }) => {
  const handleTrailsHistory = () => {
    navigation.navigate('Trails History');
  };

  return (
    <View style={styles.container}>
      <View style={styles.content}>
        <View style={styles.profileContainer}>
          <View style={styles.backgroundContainer}>
            <Ionicons name="md-person-circle" size={150} color="#FFFFFF" />
            <Text style={styles.username}>Cesário Verde</Text>
            <Text style={styles.infoTitle}>User type</Text>
            <Text style={styles.userType}>Premium</Text>
          </View>
        </View>

        <View style={styles.detailContainer}>
          <View style={styles.detailRow}>
            <Ionicons name="md-person-outline" size={36} color="#000000" />
            <Text style={styles.detailText}>José Joaquim Cesário Verde</Text>
          </View>
          <View style={styles.detailRow}>
            <Ionicons name="md-mail" size={36} color="#000000" />
            <Text style={styles.detailText}>cesarioVerde@gmail.com</Text>
          </View>
        </View>

        <TouchableOpacity style={styles.button} onPress={handleTrailsHistory}>
          <Ionicons name="md-list" size={24} color="#FFFFFF" />
          <Text style={styles.buttonText}>Trails history</Text>
        </TouchableOpacity>
      </View>
      <BottomBar />
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
  },
  button: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#FF0008',
    marginTop: 15,
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
