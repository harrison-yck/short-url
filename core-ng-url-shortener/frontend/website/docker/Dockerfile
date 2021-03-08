FROM        adoptopenjdk/openjdk15:alpine-jre
LABEL       app=website
RUN         addgroup --system app && adduser --system --no-create-home --ingroup app app
USER        app
COPY        package/dependency     /opt/app
COPY        package/app            /opt/app
CMD         ["ls -al"]
CMD         ["/opt/app/bin/website"]
