import React, { useEffect, useState } from 'react';
import axiosInstance from "../../api/axiosInstance"; // axios 설정 파일을 불러옵니다.

const Profile = () => {
  const [profile, setProfile] = useState(null);
  const [error, setError] = useState(null);
  const [file, setFile] = useState(null);
  const [uploadError, setUploadError] = useState(null);
  const [uploadSuccess, setUploadSuccess] = useState(false);

  // 사용자 프로필 데이터를 가져오는 함수
  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        const response = await axiosInstance.get('/users/profile');
        setProfile(response.data.data);
      } catch (err) {
        setError(err.response ? err.response.data : "프로필 정보를 가져오는 중 오류가 발생했습니다.");
      }
    };

    fetchUserProfile();
  }, []);

  // 파일 입력 변경 처리 함수
  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  // 프로필 사진 업로드 함수
  const handleProfileUpload = async (e) => {
    e.preventDefault();

    if (!file) {
      setUploadError("업로드할 파일을 선택해주세요.");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await axiosInstance.post('/s3/upload/profile', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      setUploadSuccess(true);
      setProfile((prevProfile) => ({
        ...prevProfile,
        imageUrl: response.data.imageUrl, // 새로운 프로필 URL로 업데이트
      }));
      setUploadError(null);
    } catch (err) {
      setUploadSuccess(false);
      setUploadError(err.response ? err.response.data : "프로필 업로드 중 오류가 발생했습니다.");
    }
  };

  if (!profile && !error) {
    return <div style={containerStyle}>로딩 중...</div>;
  }

  if (error) {
    return <div style={containerStyle}>오류: {error}</div>;
  }

  return (
      <div style={containerStyle}>
        <div style={contentStyle}>
          <h1>사용자 프로필</h1>
          {profile.imageUrl && <img src={profile.imageUrl} alt="프로필 사진" style={imageStyle} />}
          <p><strong>사용자 이름:</strong> {profile.username}</p>
          <p><strong>이름:</strong> {profile.name}</p>
          <p><strong>닉네임:</strong> {profile.nickname}</p>
          <p><strong>이메일:</strong> {profile.email}</p>
          <p><strong>주소:</strong> {profile.address}</p>

          <form onSubmit={handleProfileUpload} style={formStyle}>
            <input type="file" accept="image/*" onChange={handleFileChange} />
            <button type="submit">프로필 사진 업로드</button>
          </form>
          {uploadSuccess && <p style={{ color: 'green' }}>프로필 사진이 성공적으로 업로드되었습니다.</p>}
          {uploadError && <p style={{ color: 'red' }}>오류: {uploadError}</p>}
        </div>
      </div>
  );
};

// 인라인 스타일 정의
const containerStyle = {
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'flex-end', // 하단에 배치
  alignItems: 'center', // 수평 중앙 정렬
  minHeight: '100vh', // 화면 전체 높이 사용
  padding: '20px',
  boxSizing: 'border-box',
};

const contentStyle = {
  textAlign: 'left', // 텍스트 왼쪽 정렬
  maxWidth: '600px', // 최대 너비 설정
  width: '100%',
  backgroundColor: '#f9f9f9', // 배경색
  padding: '20px',
  borderRadius: '8px',
  boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
};

const imageStyle = {
  maxWidth: '100%', // 이미지 너비 제한
  height: 'auto',
  borderRadius: '50%', // 원형으로 만들기
  marginBottom: '20px',
};

const formStyle = {
  display: 'flex',
  flexDirection: 'column',
  gap: '10px',
  marginTop: '20px',
};

export default Profile;
