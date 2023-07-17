# Use of existing TLS fuzzer for SPDM development

Git repo for development of a SPDM fuzzer using a TLS pre-existing one. 

## About

This repo has two parts: the pre testing for already existing fuzzers for TLS, with the aim of finding the highest perfomance one.
The second part is the fuzzer development itself.

For the first part, there is a Dockerfile made for building an image that compares all the fuzzers automatically. You build it, run and them you receive the table with all the fuzzers.
The script has a tshark running to record all the packets sent and received in the 4433 port locally. That's why the docker is not 100% automatic, you can access it by following the steps in the Running part.

### Requisites


```
Docker
(you can use Windows WSL and Linux)
```

### Installing

First, you need to clone the repo:
```
>git clone https://github.com/th-duvanel/fuzzer-tests.git
```
Then, just:
```
>cd fuzzer-tests
>docker build -t fuzzer-tests .
```
After building your image, you can run it if it's not open by using:
```
>docker run -ti fuzzer-tests
```
If it's already running, you can open another terminal by using:
```
>docker ps
CONTAINER ID        IMAGE        NAMES               ........         .......

<container-id>      ......       <name>              ........         .......

>docker exec -ti <container-id> bash
```

### Running

All the files you gonna need are in the home folder.
Just run the test.sh script.
```
>chmod +x test.sh
./test.sh
```

## Authors

* **Thiago Duvanel Ferreira** - [Linkedin](https://www.linkedin.com/in/thiago-duvanel-ferreira-142028244/) - [GitHub](https://github.com/th-duvanel)
* **Filipe Tressmann Velozo**
* **Eduardo Figueredo Pacheco**



