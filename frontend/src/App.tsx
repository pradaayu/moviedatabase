import React, { useState, useEffect } from 'react';

const App = () => {
  const [message, setMessage] = useState('Loading...');

  useEffect(() => {
    // Fetch message from backend
    fetch('/api/hello')
      .then(response => response.json())
      .then(data => setMessage(data.message))
      .catch(error => setMessage('Error connecting to backend'));
  }, []);

  return (
    <div style={{ textAlign: 'center', marginTop: '50px' }}>
      <h1>Hello World</h1>
      <p>Backend says: {message}</p>
	  <div>
	  	<h2>Now we can start building our IMDB clone</h2>
	</div>
    </div>
  );
};

export default App;