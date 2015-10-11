import socket
import numpy as np
import cv2
import struct


class ClientUDP():
	BUFFER = 7680
	def __init__(self, address, port):
		self.MCAST_GRP = address
		self.MCAST_PORT = int(port)
		self.sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
		self.sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
		self.sock.bind((self.MCAST_GRP, self.MCAST_PORT))
		self.mreq = struct.pack("4sl", socket.inet_aton(self.MCAST_GRP), socket.INADDR_ANY)
		self.sock.setsockopt(socket.IPPROTO_IP, socket.IP_ADD_MEMBERSHIP, self.mreq)

	def listen(self, video):
		try:
			s=""
			while True:
				data, addr = self.sock.recvfrom(self.BUFFER)
				s+=data
				if(len(s)==(30*self.BUFFER)):
					frame = np.fromstring (s,dtype=np.uint8)
					frame = frame.reshape (240,320,3)
					cv2.imshow(video,frame)
					if cv2.waitKey(1) & 0xFF == ord ('q'):
						self.close()
					s=""
		except:
			pass
		finally:
			self.close()

	def close(self):
		print 'Closing socket'
		self.sock.close()
		cv2.destroyAllWindows()
	