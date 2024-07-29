import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance'; // 경로 수정
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import {createTheme, ThemeProvider} from '@mui/material/styles';

const defaultTheme = createTheme();

function Copyright(props) {
  return (
      <Typography variant="body2" color="text.secondary"
                  align="center" {...props}>
        {'Copyright © '}
        <Link color="inherit" href="https://mui.com/">
          Yours?
        </Link>{' '}
        {new Date().getFullYear()}
        {'.'}
      </Typography>
  );
}

export default function SignIn({onLogin}) {
  const [loginRequest, setLoginRequest] = useState({
    username: '',
    password: ''
  });

  const [errorMessage, setErrorMessage] = useState('');

  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const response = await axiosInstance.post('/users/login', loginRequest);

      // 응답 바디에서 토큰 추출
      const token = response.data.data;
      // const token = response.headers.getAuthorization(); // 응답에서 토큰 추출 // 응답에서 토큰 추출 (예: { "status": 200, "message": "로그인 성공", "data": "토큰" })
      console.log('Login successful!', token);
      console.log(response);

      // 토큰을 localStorage에 저장
      localStorage.setItem('Authorization', token);
      // 부모 컴포넌트에 로그인 상태 변경 알리기
      onLogin();

      // 페이지를 리다이렉트하거나 상태를 업데이트할 수 있습니다.
      navigate('/board');
    } catch (error) {
      // 로그인 실패 시 처리 로직
      console.error('Login failed:', error);
      if (error.response && error.response.data) {
        setErrorMessage(error.response.data.message);
      } else {
        setErrorMessage('로그인에 실패했습니다. 다시 시도해주세요.');
      }
    }
  };

  const handleChange = (e) => {
    const {name, value} = e.target;
    setLoginRequest({...loginRequest, [name]: value});
  };

  const goToSignup = () => {
    navigate('/signup');
  };

  return (
      <ThemeProvider theme={defaultTheme}>
        <Container component="main" maxWidth="xs">
          <CssBaseline/>
          <Box
              sx={{
                marginTop: 8,
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
              }}
          >
            <Avatar sx={{m: 1, bgcolor: 'secondary.main'}}>
              <LockOutlinedIcon/>
            </Avatar>
            <Typography component="h1" variant="h5">
              Sign in
            </Typography>
            <Box component="form" onSubmit={handleSubmit} noValidate
                 sx={{mt: 1}}>
              <TextField
                  margin="normal"
                  required
                  fullWidth
                  id="username"
                  label="Username"
                  name="username"
                  autoComplete="username"
                  autoFocus
                  value={loginRequest.username}
                  onChange={handleChange}
              />
              <TextField
                  margin="normal"
                  required
                  fullWidth
                  name="password"
                  label="Password"
                  type="password"
                  id="password"
                  autoComplete="current-password"
                  value={loginRequest.password}
                  onChange={handleChange}
              />
              <FormControlLabel
                  control={<Checkbox value="remember" color="primary"/>}
                  label="Remember me"
              />
              {errorMessage && <Typography
                  color="error">{errorMessage}</Typography>}
              <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  sx={{mt: 3, mb: 2}}
              >
                Sign In
              </Button>
              <Grid container>
                <Grid item xs>
                  <Link href="#" variant="body2">
                    Forgot password?
                  </Link>
                </Grid>
                <Grid item>
                  <Link href="#" variant="body2" onClick={goToSignup}>
                    {"Don't have an account? Sign Up"}
                  </Link>
                </Grid>
              </Grid>
            </Box>
          </Box>
          <Copyright sx={{mt: 8, mb: 4}}/>
        </Container>
      </ThemeProvider>
  );
}
