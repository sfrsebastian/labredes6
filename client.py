from clientUDP import ClientUDP
import threading
import os
from httplib import *
import json
import requests

class Client():
	connection = HTTPConnection("localhost",8081)
	auth_connection = HTTPConnection("localhost",8080)

	def __init__(self):
		self.get_videos()
	
	def login(self):
		#debe retornar Boolean
		print 'login'
		return True

	def start_listening(self, video):
		#Make post with video name
		print 'video name ' + video
		client = ClientUDP()
		print 'Stream Started'
		client.listen(video)

	def add_video(self, name="", path=""):
		if(name != "" and path != ""):
			print name
			print path
			file = open(path, 'rb')
			print file
			url = "http://localhost:8081/labredes6-server/api/video-player/upload/{videoName}".format(videoName=name+".mp4")
			files = {'videoFile': open(path,'rb')}
			headers = {'content-type': 'multipart/form-data'}
			res = requests.post(url, files=files)

	def get_videos(self):
		query = "/labredes6-server/api/video-player"
		self.connection.request("GET", url=query)
		response = self.connection.getresponse()
		if response.status == 200:
			ans = response.read()
			parsed = json.loads(ans)
			print json.dumps(parsed,indent=4)
			self.videos = parsed
		else:
			self.videos = []