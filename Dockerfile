FROM ubuntu:22.04

# System update
RUN apt update && apt-get install y --no-install-recommends \
git \
tshark \
python3-pip \
bc \
openjdk-11-jdk \
maven

RUN pip3 install scipy
RUN pip3 install tabulate



COPY 1_fuzzers_table /home/fuzzers