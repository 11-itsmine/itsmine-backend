import React, {useState} from 'react';

const MessageInput = ({onSend}) => {
  const [message, setMessage] = useState('');

  const handleSend = () => {
    if (message.trim()) {
      onSend(message);
      setMessage('');
    }
  };

  return (
      <div className="message-input">
        <input
            type="text"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            placeholder="Enter your message..."
        />
        <button onClick={handleSend}>Send</button>
      </div>
  );
};

export default MessageInput;
