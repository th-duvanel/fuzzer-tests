ar -M <<EOM
CREATE $1
ADDLIB $2
ADDMOD ${@:2}
ADDMOD module.o
SAVE
END
EOM
