FROM libreoffice:v1
ENV LC_ALL zh_CN.utf8

ENV JAVA_HOME /usr/lib/jvm/jre-1.8.0-openjdk-1.8.0.222.b10-0.el7_6.x86_64
ENV TZ 'Asia/Shanghai'
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

COPY app.jar /usr/src/app.jar
WORKDIR /usr/src

CMD /usr/bin/libreoffice --headless --accept="socket,host=127.0.0.1,port=8100;urp;" --nofirststartwizard
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]