# Use of existing TLS fuzzer for SPDM development

Git repo for development of a SPDM fuzzer using a TLS pre-existing one. 

## About

This repo has two parts: the pre testing for already existing fuzzers for TLS, with the aim of finding the highest perfomance one.
The second part is the fuzzer development itself.

### Fuzzers Table

For the first part, there is a Dockerfile made for building an image that compares all the fuzzers automatically. You build it, run and them you receive the table with all the fuzzers.
The script has a tshark running to record all the packets sent and received in the 4433 port locally. That's why the docker is not 100% automatic, you can access it by following the steps in the Running part.

#### Requisites


```
Docker
(you can use Windows WSL and Linux)
```

#### Installing

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

#### Running

All the files you gonna need are in the home folder inside your container.
Just run the test.sh script.
```
>chmod +x test.sh
./test.sh
```

### SPDM Fuzzer

Soon.


### Specs used for testing

Windows 10 22H2
```
                   -`                    duvanel@duvanel
                  .o+`                   --------------
                 `ooo/                   OS: Arch Linux on Windows 10 x86_64
                `+oooo:                  Kernel: 5.15.90.1-microsoft-standard-WSL2
               `+oooooo:                 Uptime: 48 mins
               -+oooooo+:                Packages: 273 (pacman)
             `/:-:++oooo+:               Shell: zsh 5.9
            `/++++/+++++++:              Terminal: Windows Terminal
           `/++++++++++++++:             CPU: AMD Ryzen 7 5800H with Radeon Graphics (16) @ 3.193GHz
          `/+++ooooooooooooo/`           GPU: 734e:00:00.0 Microsoft Corporation Basic Render Driver
         ./ooosssso++osssssso+`          Memory: 1919MiB / 7618MiB
        .oossssso-````/ossssss+`
       -osssssso.      :ssssssso.
      :osssssss/        osssso+++.
     /ossssssss/        +ssssooo/-
   `/ossssso+/:-        -:/+osssso+-
  `+sso+:-`                 `.-/+oso:
 `++:.                           `-/+/
 .`                                 `/
```
```
LENOVO IDEAPAD GAMING 3
AMD RYZEN 7 5800H
16 GB DDR4 2866 MhZ
NVIDIA GEFORCE RTX 3060
```
The docker uses Ubuntu 22:04.


## Authors

* **Thiago Duvanel Ferreira** - [Linkedin](https://www.linkedin.com/in/thiago-duvanel-ferreira-142028244/) - [GitHub](https://github.com/th-duvanel)
* **Filipe Tressmann Velozo**
* **Eduardo Figueredo Pacheco**



