import socket
import numpy as np
import time
import cv2
import struct

UDP_IP = "127.0.0.1"
UDP_PORT = 5005
BUFFER = 7680

MCAST_GRP = '239.255.0.1'
MCAST_PORT = 9001

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
sock.bind((MCAST_GRP, MCAST_PORT))
mreq = struct.pack("4sl", socket.inet_aton(MCAST_GRP), socket.INADDR_ANY)
sock.setsockopt(socket.IPPROTO_IP, socket.IP_ADD_MEMBERSHIP, mreq)

try:
	s=""
	while True:
		data, addr = sock.recvfrom(BUFFER)
		s+=data
		if(len(s)==(30*BUFFER)):
			frame = np.fromstring (s,dtype=np.uint8)
			frame = frame.reshape (240,320,3)
			cv2.imshow('Client',frame)
			if cv2.waitKey(1) & 0xFF == ord ('q'):
				break
			s=""

		
finally:
	print 'Closing socket'
	sock.close()
	cap.release()
	cv2.destroyAllWindows()
	