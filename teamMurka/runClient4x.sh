#!/bin/bash

cd build/classes/main
xterm -e java com/tmquoridor/Client/GameClient localhost:6000 localhost:6001 localhost:6002 localhost:6003 &