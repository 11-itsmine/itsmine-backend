# Node.js를 기반으로 하는 리액트 앱 이미지
FROM node:20.16.0 as build

# 작업 디렉토리 설정
WORKDIR /app

# 의존성 설치 및 빌드(CI)
COPY package.json .
RUN npm install
COPY . .
RUN npm run build

# Nginx를 기반으로 하는 최종 이미지
FROM nginx:alpine

# 시간대 및 로케일 설정
RUN apk add --no-cache tzdata musl-locales musl-locales-lang && \
    ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone && \
    echo "export LANG=ko_KR.UTF-8" >> /etc/profile.d/locale.sh && \
    echo "export LANGUAGE=ko_KR.UTF-8" >> /etc/profile.d/locale.sh && \
    echo "export LC_ALL=ko_KR.UTF-8" >> /etc/profile.d/locale.sh && \
    apk del tzdata

# Nginx 설정 파일 복사
COPY nginx/nginx.conf /etc/nginx/nginx.conf

# 빌드된 리액트 앱을 Nginx의 HTML 디렉토리로 복사
COPY --from=build /app/build /usr/share/nginx/html

# 포트 설정
EXPOSE 80
EXPOSE 443

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]