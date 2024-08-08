import { createGlobalStyle } from 'styled-components';
import reset from 'styled-reset';

const GlobalStyle = createGlobalStyle`
 ${reset}
 
  *, *::before, *::after {
    box-sizing: border-box;
    font-family: 'Apple SD Gothic Neo', 'Apple SD 산돌고딕 Neo', BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen-Sans, Ubuntu, Cantarell, "Helvetica Neue", sans-serif;
  }
  
  body {
    line-height: 1.5;
    font-family: 'Apple SD Gothic Neo', 'Apple SD 산돌고딕 Neo', BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen-Sans, Ubuntu, Cantarell, "Helvetica Neue", sans-serif;
  }

  h1 {
    font-family: 'Viga', sans-serif;
  }

`;

export default GlobalStyle;
