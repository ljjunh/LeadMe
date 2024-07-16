import React from 'react';
import Button from './components/TestBtn';

const App: React.FC = () => {
  return (
    <div>
      <Button label="Send Request" url="YOUR_BACKEND_URL_HERE" />
    </div>
  );
};

export default App;
