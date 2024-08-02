# OpenJDK 17을 사용하는 경량 Alpine 리눅스 이미지를 베이스 이미지로 설정합니다.
FROM openjdk:17-jdk-alpine

# 이미지 메타데이터에 유지보수 정보를 추가합니다.
LABEL maintainer="yugi828@naver.com"

# /tmp 디렉토리를 호스트와 공유하는 볼륨으로 설정합니다.
VOLUME /tmp

# 컨테이너 외부에서 접근할 수 있도록 8080 포트를 노출합니다.
EXPOSE 8080

# 빌드 과정에서 사용할 JAR 파일 경로를 정의합니다. Gradle 빌드 디렉토리에서 생성된 JAR 파일을 사용합니다.
ARG JAR_FILE=build/libs/itsmine-0.0.1-SNAPSHOT.jar

# JAR 파일을 컨테이너 이미지의 루트 디렉토리에 app.jar 이름으로 추가합니다.
ADD ${JAR_FILE} app.jar

# 컨테이너가 시작될 때 실행할 명령을 정의합니다. 여기서는 JAR 파일을 실행합니다.
ENTRYPOINT ["java","-jar","/app.jar"]