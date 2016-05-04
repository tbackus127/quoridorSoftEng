#!/bin/sh

if [[ $# == 2 || $# == 4 ]]; then
    java -cp build/libs/Quoridor-teamMurka-1.0.jar com.tmquoridor Client.GameClient $@
else
    echo "Usage:"
    echo "  ./client <host1>:<port1> <host2>:<port2> \\"
    echo "           [<host3:<port3> <host4>:<port4>]"
    echo "Host names are optional (default: localhost)"
fi