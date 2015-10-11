from clientUDP import ClientUDP
import threading
import os
from httplib import *
import json
import requests
import hashlib

class Client():
	connection = HTTPConnection("localhost",8081)
	auth_connection = HTTPConnection("localhost",8080)
	
	def login(self, username, password):
		query = "/api/users/{username}/tokens".format(username=username)
		header = {"content-type":"application/json"}
		password = hashlib.sha256(password).hexdigest()
		print password
		data = {"username":username, "password":password}
		self.auth_connection.request("POST", query, json.dumps(data),header)
		response = self.auth_connection.getresponse()
		if response.status == 201:
			ans = response.read()
			parsed = json.loads(ans)
			try:
				parsed["error"]
				return False
			except:
				return True
		else:
			return False

	def signup(self, username, password):
		query = "/api/users"
		header = {"content-type":"application/json"}
		password = hashlib.sha256(password).hexdigest()
		print password
		data = {"name":"algo",
    			"username":username,
    			"password":password,
    			"apiClient":"true",
    			"organizationId":"algo",
    			"roleId":"algo"}
		self.auth_connection.request("POST", query, json.dumps(data),header)
		response = self.auth_connection.getresponse()
		if response.status == 201:
			ans = response.read()
			parsed = json.loads(ans)
			try:
				parsed["error"]
				return False
			except:
				return True
		else:
			return False

	def start_listening(self, video):
		query = "/labredes6-server/api/video-player/play/{videoName}".format(videoName=video)
		print query
		self.connection.request("GET", url=query)
		response = self.connection.getresponse()
		if response.status == 200:
			ans = response.read()
			print ans
			parsed = json.loads(ans)
			print parsed
			client = ClientUDP(parsed["ip"],parsed["port"])
			client.listen(video)
		else:
			pass
		

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
			return parsed
		else:
			return []