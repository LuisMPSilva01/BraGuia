import React, { useEffect, useState } from 'react';
import { View, TextInput, Button, Text, Keyboard, TouchableWithoutFeedback, AsyncStorage } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { useSelector, useDispatch } from 'react-redux';
import { setCookies, updateUsername } from '../actions/user';

const LoginActivity = () => {
  const navigation = useNavigation();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loginFailed, setLoginFailed] = useState(false);

  const dispatch = useDispatch();

  useEffect(() => {
    const backButtonHandler = () => true;
    navigation.addListener('beforeRemove', (e) => {
      if (!backButtonHandler()) {
        e.preventDefault();
      }
    });
  }, [navigation]);


  const handleLogin = () => {
    try {
      validate();
    } catch (error) {
      setLoginFailed(true);
    }
  }
  


  const validate = () => {
    try {
      if (username.trim() === '' || password === '') {
        console.log("invalid name");
        throw new Error('Login failed');
      } else {
        console.log("valid name");
        makeLoginRequest({
          onLoginSuccess: () => {
          },
          onLoginFailure: () => {
            setLoginFailed(true);
          }
        });
      }
    } catch (error) {
      setLoginFailed(true);
    }
  };
  

  const makeLoginRequest = async (callback) => {
    const body = {
      username: username.trim(),
      email: "",
      password: password,
    };
    console.log(body);
    try {
      console.log("Started login request")
      const response = await fetch('https://c5a2-193-137-92-29.eu.ngrok.io/login', {
        credentials: 'omit',
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
      });

      if (response.ok) {
        console.log("response ok");
        const cookies = response.headers.map['set-cookie'];
        console.log(cookies)
        const csrfTokenMatch = cookies.match(/csrftoken=([^;]+)/);
        const sessionIdMatch = cookies.match(/sessionid=([^;]+)/);
        const csrfToken = csrfTokenMatch ? csrfTokenMatch[0] : null;
        const sessionId = sessionIdMatch ? sessionIdMatch[0] : null;
        console.log(csrfToken);
        console.log(sessionId);
        
        if (csrfTokenMatch.length===2) {
          // Save cookies in Redux store
          console.log("Saved Cookie")
          dispatch(setCookies(csrfToken));
          callback.onLoginSuccess();
        }
      } else {
        console.log('Login request failed:', response.status);
        callback.onLoginFailure();
      }
    } catch (error) {
      console.log('Login request failed by error:', error);
      callback.onLoginFailure();
    }
  };

  
  const dismissKeyboard = () => {
    Keyboard.dismiss();
  };

  return (
    <TouchableWithoutFeedback onPress={dismissKeyboard}>
      <View style={styles.container}>
        <TextInput
          style={styles.input}
          placeholder="Name"
          value={username}
          onChangeText={setUsername}
        />
        <TextInput
          style={styles.input}
          placeholder="Password"
          secureTextEntry
          value={password}
          onChangeText={setPassword}
        />
        <Button title="Login" onPress={handleLogin} />
        {loginFailed && <Text style={styles.loginFailedText} id="login_failed_txt">Login Failed</Text>}
      </View>
    </TouchableWithoutFeedback>
  );
};

const styles = {
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  input: {
    width: '80%',
    height: 40,
    borderWidth: 1,
    borderColor: 'gray',
    marginVertical: 10,
    paddingHorizontal: 10,
  },
  loginFailedText: {
    color: 'red',
    marginTop: 10,
  },
};

export default LoginActivity;
