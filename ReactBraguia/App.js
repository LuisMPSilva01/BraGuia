import React from 'react';
import WrappedApp from './WrappedApp';
import { Logs } from 'expo'
import { Provider } from 'react-redux';
import { PersistGate } from 'redux-persist/integration/react';
import { store, persistor } from './src/state/store';

Logs.enableExpoCliLogging();
const App = () => {
  return (
    <Provider store={store}>
      <PersistGate persistor={persistor} loading={null}>
          <WrappedApp/>
      </PersistGate>
    </Provider>
  );
};

export default App;