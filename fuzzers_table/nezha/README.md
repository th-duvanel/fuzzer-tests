# Explanation about the wget and not using git clone
In nezha, which is a abandoned fuzzer in git, for some reason unknown, if you clone directly on the repository, much unnecessary files appear, and we don't want them. So, we use wget to get the .zip from the repository.
Then, for the error, it uses clang-3.8 version, that is a 2016 version. Is very old, and pratically impossible to search.