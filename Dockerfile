FROM ibm-semeru-runtimes:open-17-jre-focal

WORKDIR /usr/app
COPY build/install/valorantrankbot ./

ENTRYPOINT ["bin/valorantrankbot"]