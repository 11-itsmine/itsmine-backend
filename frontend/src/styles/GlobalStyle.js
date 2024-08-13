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
    margin: 0;
    padding: 0;
    font-family: 'Apple SD Gothic Neo', 'Apple SD 산돌고딕 Neo', BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen-Sans, Ubuntu, Cantarell, "Helvetica Neue", sans-serif;
    font-weight: 400; /* 기본 폰트 두께를 regular로 설정 */
    color: #222222; /* 기본 텍스트 색상 */
  }

  h1, h2, h3, h4, h5, h6 {
    font-family: 'Viga', sans-serif;
    font-weight: 600; /* 헤딩의 두께를 semiBold로 설정 */
    margin: 0;
  }

  button, input, textarea {
    font-family: inherit; /* 기본 폰트를 상속받도록 설정 */
    font-weight: inherit; /* 기본 두께를 상속받도록 설정 */
  }

  a {
    text-decoration: none;
    color: inherit;
    &:hover {
      text-decoration: underline; /* 링크에 호버 효과 추가 */
    }
  }
`;

export default GlobalStyle;
