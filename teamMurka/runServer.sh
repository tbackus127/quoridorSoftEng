#!/bin/bash

cd build/classes/main
xterm -e java com/tmquoridor/Server/ManualInputServer --port $0 &