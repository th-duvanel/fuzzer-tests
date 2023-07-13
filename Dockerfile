FROM ubuntu:22.04

# System update
RUN apt update && apt-get install y --no-install-recommends \
git \
tshark \
python3-pip \
bc \
python-dev

RUN pip3 install scipy
RUN pip3 install tabulate

COPY 1_fuzzers_table /home/1_fuzzers_table

RUN cd home/1_fuzzers_table
RUN chmod +x *.sh

RUN ./test.sh