import React, {useEffect, useState} from 'react';
import axiosInstance from "../../api/axiosInstance";

const Profile = () => {
  // 사용자 프로필 상태 관리
  const [profile, setProfile] = useState(null);
  const [profileError, setProfileError] = useState(null);
  const [file, setFile] = useState(null);
  const [uploadError, setUploadError] = useState(null);
  const [uploadSuccess, setUploadSuccess] = useState(false);

  // 제품 목록 상태 관리
  const [products, setProducts] = useState([]);
  const [productError, setProductError] = useState(null);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10); // 한 페이지에 표시할 제품 수
  const [category, setCategory] = useState(null);
  const [price, setPrice] = useState(null);
  const [search, setSearch] = useState('');
  const [sort, setSort] = useState('createdAt');

  // 좋아요한 제품 목록 상태 관리
  const [likedProducts, setLikedProducts] = useState([]);
  const [likedError, setLikedError] = useState(null);

  // 사용자 프로필 데이터를 가져오는 함수
  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        const response = await axiosInstance.get('/users/profile');
        setProfile(response.data.data);
      } catch (err) {
        setProfileError(JSON.stringify(
            err.response ? err.response.data : "프로필 정보를 가져오는 중 오류가 발생했습니다."));
      }
    };

    fetchUserProfile();
  }, []);

  // 제품 목록을 가져오는 함수
  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await axiosInstance.get('/products', {
          params: {
            page,
            size,
            category,
            price,
            search,
            sort,
          }
        });
        setProducts(response.data.data.content);
      } catch (err) {
        setProductError(JSON.stringify(
            err.response ? err.response.data : "제품 목록을 가져오는 중 오류가 발생했습니다."));
      }
    };

    fetchProducts();
  }, [page, size, category, price, search, sort]);

  // 좋아요한 제품 목록을 가져오는 함수
  useEffect(() => {
    const fetchLikedProducts = async () => {
      try {
        const response = await axiosInstance.get('/products/likes', {
          params: {
            page,
            size,
          }
        });
        setLikedProducts(response.data.data.content);
      } catch (err) {
        setLikedError(JSON.stringify(err.response ? err.response.data
            : "좋아하는 제품 목록을 가져오는 중 오류가 발생했습니다."));
      }
    };

    fetchLikedProducts();
  }, [page, size]);

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
      const response = await axiosInstance.post('/s3/upload/profile', formData,
          {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          });

      setUploadSuccess(true);
      setProfile((prevProfile) => ({
        ...prevProfile,
        imageUrl: response.data.imageUrl,
      }));
      setUploadError(null);
    } catch (err) {
      setUploadSuccess(false);
      setUploadError(JSON.stringify(
          err.response ? err.response.data : "프로필 업로드 중 오류가 발생했습니다."));
    }
  };

  return (
      <div style={containerStyle}>
        {/* 사용자 프로필 영역 */}
        <div style={contentStyle}>
          <h1>사용자 프로필</h1>
          {profileError && <p style={{color: 'red'}}>오류: {profileError}</p>}
          {profile ? (
              <>
                {profile.imageUrl && <img src={profile.imageUrl} alt="프로필 사진"
                                          style={imageStyle}/>}
                <p><strong>사용자 이름:</strong> {profile.username}</p>
                <p><strong>이름:</strong> {profile.name}</p>
                <p><strong>닉네임:</strong> {profile.nickname}</p>
                <p><strong>이메일:</strong> {profile.email}</p>
                <p><strong>주소:</strong> {profile.address}</p>

                <form onSubmit={handleProfileUpload} style={formStyle}>
                  <input type="file" accept="image/*"
                         onChange={handleFileChange}/>
                  <button type="submit">프로필 사진 업로드</button>
                </form>
                {uploadSuccess && <p style={{color: 'green'}}>프로필 사진이 성공적으로
                  업로드되었습니다.</p>}
                {uploadError && <p style={{color: 'red'}}>오류: {uploadError}</p>}
              </>
          ) : (
              <p>로딩 중...</p>
          )}
        </div>

        {/* 제품 목록 영역 */}
        <div style={contentStyle}>
          <h2>제품 목록</h2>
          {productError && <p style={{color: 'red'}}>오류: {productError}</p>}
          <div style={productListStyle}>
            {products.map((product) => (
                <div key={product.id} style={productItemStyle}>
                  {product.imagesUrl.length > 0 && (
                      <img src={product.imagesUrl[0]} alt={product.productName}
                           style={productImageStyle}/>
                  )}
                  <h3>{product.productName}</h3>
                  <p>{product.description}</p>
                  <p><strong>시작 가격:</strong> {product.startPrice}원</p>
                  <p><strong>현재 가격:</strong> {product.currentPrice}원</p>
                  <p><strong>마감일:</strong> {new Date(
                      product.dueDate).toLocaleString()}</p>
                </div>
            ))}
          </div>
        </div>

        {/* 좋아요한 제품 목록 영역 */}
        <div style={contentStyle}>
          <h2>좋아하는 제품 목록</h2>
          {likedError && <p style={{color: 'red'}}>오류: {likedError}</p>}
          <div style={productListStyle}>
            {likedProducts.map((product) => (
                <div key={product.id} style={productItemStyle}>
                  {product.imagesUrl.length > 0 && (
                      <img src={product.imagesUrl[0]} alt={product.productName}
                           style={productImageStyle}/>
                  )}
                  <h3>{product.productName}</h3>
                  <p>{product.description}</p>
                  <p><strong>시작 가격:</strong> {product.startPrice}원</p>
                  <p><strong>현재 가격:</strong> {product.currentPrice}원</p>
                  <p><strong>마감일:</strong> {new Date(
                      product.dueDate).toLocaleString()}</p>
                </div>
            ))}
          </div>
        </div>
      </div>
  );
};

// 인라인 스타일 정의
const containerStyle = {
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'flex-start',
  alignItems: 'center',
  minHeight: '100vh',
  padding: '20px',
  boxSizing: 'border-box',
};

const contentStyle = {
  textAlign: 'left',
  maxWidth: '600px',
  width: '100%',
  backgroundColor: '#f9f9f9',
  padding: '20px',
  borderRadius: '8px',
  boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
  marginBottom: '20px',
};

const imageStyle = {
  maxWidth: '100%',
  height: 'auto',
  borderRadius: '50%',
  marginBottom: '20px',
};

const formStyle = {
  display: 'flex',
  flexDirection: 'column',
  gap: '10px',
  marginTop: '20px',
};

const productListStyle = {
  display: 'flex',
  flexDirection: 'column',
  gap: '10px',
};

const productItemStyle = {
  border: '1px solid #ddd',
  borderRadius: '8px',
  padding: '10px',
  textAlign: 'left',
  backgroundColor: '#fff',
  boxShadow: '0 1px 2px rgba(0, 0, 0, 0.1)',
};

const productImageStyle = {
  maxWidth: '100%',
  height: 'auto',
  borderRadius: '8px',
  marginBottom: '10px',
};

export default Profile;
