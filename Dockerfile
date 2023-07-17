FROM ubuntu:22.04

# System update
RUN apt update && apt-get install -y --no-install-recommends \
git \
python3-pip \
bc \
wget \
unzip


RUN DEBIAN_FRONTEND=noninteractive apt-get install -y tshark


# statistics.py
RUN pip3 install scipy
RUN pip3 install tabulate

COPY fuzzers_table /home/fuzzers
COPY openspdm /home/openspdm
COPY getVersionFuzzer /home/

RUN chmod +x home/fuzzers/*.sh
