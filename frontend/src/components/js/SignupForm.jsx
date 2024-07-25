import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance';

function SignupForm() {
    const [signupRequest, setSignupRequest] = useState({
        username: '',
        password: '',
        email: '',
        nickname: '',
        introduction: '',
        rolePassword: ''
    });

    const [errorMessage, setErrorMessage] = useState('');

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            await axiosInstance.post('/api/auth/signup', signupRequest);

            console.log('Signup successful!');
            navigate('/login');
        } catch (error) {
            console.error('Signup failed:', error);
            if (error.response && error.response.data) {
                setErrorMessage(error.response.data.message);
            } else {
                setErrorMessage('회원가입에 실패했습니다. 다시 시도해주세요.');
            }
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setSignupRequest({ ...signupRequest, [name]: value });
    };

    return (
        <form onSubmit={handleSubmit}>
            <h1>회원가입 페이지</h1>
            <label>
                Username:
                <input type="text" name="username" value={signupRequest.username} onChange={handleChange} required />
            </label>
            <br/>
            <label>
                Password:
                <input type="password" name="password" value={signupRequest.password} onChange={handleChange} required />
            </label>
            <br/>
            <label>
                Email:
                <input type="email" name="email" value={signupRequest.email} onChange={handleChange} required />
            </label>
            <br/>
            <label>
                Nickname:
                <input type="text" name="nickname" value={signupRequest.nickname} onChange={handleChange} required />
            </label>
            <br/>
            <label>
                Introduction:
                <input type="text" name="introduction" value={signupRequest.introduction} onChange={handleChange} />
            </label>
            <br/>
            <label>
                Role Password:
                <input type="password" name="rolePassword" value={signupRequest.rolePassword} onChange={handleChange} />
            </label>
            <br/>
            {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
            <button type="submit">Signup</button>
        </form>
    );
}

export default SignupForm;