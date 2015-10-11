import numpy as np
import sys
import cv2
import socket
import time
import pickle
import struct

BUFFER = 7680
MCAST_GRP = '239.255.0.1'
MCAST_PORT = 9001

cap = cv2.VideoCapture(sys.argv[1])
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, 2)

try:
	while(cap.isOpened()):
		print 'sending frame'
		ret, frame = cap.read()
		frame = cv2.resize(frame, (320, 240))
		cv2.imshow('Server',frame)
		if cv2.waitKey(1) & 0xFF == ord('q'):
			break
		frame = frame.flatten()
		data = frame.tostring()
		for i in xrange(30):
			sock.sendto (data[i*BUFFER:(i+1)*BUFFER],(MCAST_GRP, MCAST_PORT))
finally:
	print 'Closing socket'
	sock.close()
	cap.release()
	cv2.destroyAllWindows()


