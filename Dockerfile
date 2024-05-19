#자바 21 베이스 이미지
FROM eclipse-temurin:21

#인자를 설정한다. argument.
ARG JAR_FILE=build/libs/*.jar

#이미지 생성할 때 파일을 복사한다. 위의 jar_file을 app.jar로 복사해서 이미지로 가져온다.
COPY ${JAR_FILE} app.jar

#컨테이너를 시작할 때 실행할 명령어.
ENTRYPOINT ["java", "-jar", "app.jar"]
