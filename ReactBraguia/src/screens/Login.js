import React, { createContext,useEffect, useState , useContext } from 'react';
import { View, TextInput, Button, Text, Keyboard, TouchableWithoutFeedback } from 'react-native';
import { useNavigation } from '@react-navigation/native';

const LoginActivity = () => {
  const navigation = useNavigation();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loginFailed, setLoginFailed] = useState(false);

  const AppContext = createContext();
  const appContext = useContext(AppContext);

  useEffect(() => {
    const backButtonHandler = () => true;
    navigation.addListener('beforeRemove', (e) => {
      if (!backButtonHandler()) {
        e.preventDefault();
      }
    });
  }, []);

  const handleLogin = () => {
    try {
      validate(username.trim(), password);
    } catch (error) {
      setLoginFailed(true);
    }
  };


  const validate = (userName, userPassword) => {
    try {
      if (userName === '' || userPassword === '') {
        throw new Error('Login failed');
      } else {
        makeLoginRequest(username, password, appContext, {
          onLoginSuccess: () => {
            setLoginFailed(false);
            changeToMainActivity();
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
  

  const makeLoginRequest = async (username, password, context, callback) => {
    const body = {
      username: username,
      email: '',
      password: password
    };
  
    try {
      const response = await fetch('https://c5a2-193-137-92-29.eu.ngrok.io/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
      });
  
      if (response.ok) {
        const cookies = response.headers.map['set-cookie'];
        if (cookies && cookies.length > 0) {
          const cookieString = cookies.map(cookie => cookie.split(';')[0]).join(';');
          const sharedPreferences = context.getSharedPreferences('BraguiaPreferences', Context.MODE_PRIVATE);
          sharedPreferences.edit().putString('cookies', cookieString).apply();
          updateUserAPI(cookieString, callback, context);
        }
      } else {
        console.error('Login request failed:', response.status);
        callback.onLoginFailure();
      }
    } catch (error) {
      console.error('Login request failed:', error);
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
