FROM        adoptopenjdk/openjdk15:alpine-jre
LABEL       app=url-service
RUN         addgroup --system app && adduser --system --no-create-home --ingroup app app
USER        app
COPY        package/dependency     /opt/app
COPY        package/app            /opt/app
CMD         ["/opt/app/bin/url-service"]
