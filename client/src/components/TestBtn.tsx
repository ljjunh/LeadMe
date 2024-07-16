import React from 'react';
import axios from 'axios';

interface ButtonProps {
  label: string;
  url: string;
}

const Button: React.FC<ButtonProps> = ({ label, url }) => {
  const handleClick = async () => {
    try {
      const response = await axios.post(url, { data: 'sample url' });
      console.log('Response:', response.data);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    <button onClick={handleClick}>
      {label}
    </button>
  );
};

export default Button;
