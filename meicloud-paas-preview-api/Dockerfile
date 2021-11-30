FROM centos

RUN yum update -y \
    && yum install telnet -y \
    && yum remove epel-release -y \
    && yum update -y \
    && yum install epel-release -y \
    && yum groupinstall "Fonts" -y \
    && yum groupinstall "Input Methods" -y \
    && rm -rf /etc/localtime \
    && ln -s /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && yum -y install kde-l10n-Chinese \
    && yum -y reinstall glibc-common \
    && localedef -c -f UTF-8 -i zh_CN zh_CN.utf8 \
    && yum clean all
# 设置中文支持环境
ENV LC_ALL zh_CN.utf8
RUN yum install -y libreoffice

RUN yum install -y java-1.8.0-openjdk.x86_64
ENV JAVA_HOME /usr/lib/jvm/jre-1.8.0-openjdk-1.8.0.222.b10-0.el7_6.x86_64
ENV TZ 'Asia/Shanghai'
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8


COPY app.jar /app.jar

CMD /usr/bin/libreoffice --headless --accept="socket,host=127.0.0.1,port=8100;urp;" --nofirststartwizard
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]