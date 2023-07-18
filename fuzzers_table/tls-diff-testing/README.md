# TLS-Diff Fuzzer: README

This repository contains the TLS-Diff fuzzer, which is primarily focused on testing servers using the openssl library. While the fuzzer is designed to test multiple server implementations like mbedtls, wolfssl, matrixssl, and boringssl, for this project, we have standardized our testing exclusively on openssl servers. This allows us to compare the performance and effectiveness of the fuzzer across different server implementations consistently.

To use the TLS-Diff fuzzer with openssl, you will find two essential files in this repository: `generate_multi.sh` and `stimulator.cpp`. These files are intended to replace their counterparts in the original repository.

Here's a brief explanation of their significance and what was modified:

1. **`stimulator.cpp`:** This file is responsible for establishing connections with the target servers. In our implementation, we have made modifications to the `port` variable at line 217, fixing it to the standard TLS port, which is 4433. By doing so, we ensure that the fuzzer tests are conducted consistently on the desired port across different server implementations.

2. **`generate_multi.sh`:** This script generates the messages used for testing. To streamline the testing process, we have made adjustments to generate a reduced number of messages compared to the original fuzzer. Instead of generating 10 x 100,000 messages, our implementation generates 10 x 10 messages. This modification simplifies the testing process while maintaining the ability to effectively evaluate the performance of the TLS-Diff fuzzer.

By utilizing the standardized openssl configuration and the provided files in this repository, you can seamlessly conduct comparative testing using the TLS-Diff fuzzer.
