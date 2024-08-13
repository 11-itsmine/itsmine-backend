export const REDIRECT_URI = 'https://itsyours.store/oauth/callback/kakao';

export const REACT_APP_REST_API_KEY = '08a46b304d568ef454241721bcb6b981';

export const KAKAO_AUTH_URL = `https://kauth.kakao.com/oauth/authorize?client_id=${REACT_APP_REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code&prompt=select_account`;