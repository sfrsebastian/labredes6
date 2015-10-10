#!/bin/sh
source ~/.bash_profile
shopt -s expand_aliases
vlc -vvv $1 --sout '#transcode{vcodec=MPEG-4,acodec=mpga,vb=800,ab=128,deinterlace}:rtp{proto=udp,mux=ts,dst=239.255.0.1,port=9001}' --loop --ttl 3