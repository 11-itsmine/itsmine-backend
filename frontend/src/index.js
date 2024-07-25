import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

//App 의 엔트리 포인트 역할을 하는 파일로, 리액트 루트 컴포넌트를 DOM 에 마운팅하는 역할을 한다.
// 코드 하단에 보면 JSX 문법을 사용하는데 React Library 를 로딩하면 이를 해석할 수 있다.
// 루트 컴포넌트 App 을 DOM 에 마운트하기 위해서는 react-dom Library 의 render() 함수를 사용한다.

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
      <App/>
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
