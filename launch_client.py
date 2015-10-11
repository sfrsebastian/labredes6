from Tkinter import *  # AFAIK Tkinter is always capitalized
from tkFileDialog import askopenfilename
from client import Client

class App:
	characterPrefix = "character_"
	def __init__(self, master, client):
		self.client = client
		self.master = master
		frame = Frame(master)
		frame.pack()

		self.videos = Listbox(frame)
		for video in client.videos:
			self.videos.insert(END, video)
		self.videos.grid(row = 1, column = 0, rowspan = 5)
		charadd = Button(frame, text = " Agregar  ", command = self.addVideo).grid(row = 1, column = 1)
		charremove = Button(frame, text = " Eliminar ", command = self.removeVideo).grid(row = 2, column = 1)
		charedit = Button(frame, text = "Reproducir", command = self.playVideo).grid(row = 3, column = 1)

	def addVideo(self, initialCharacter='', initialInfo=''):
		def create():
			nombre_video = characterEntry.get()
			filename = askopenfilename()
			client.add_video(name=nombre_video, path = filename)
			self.videos.insert(END, nombre_video)
			t.destroy()

		t = Toplevel(root)  # Creates a new window
		t.title("Agregar video")
		characterEntry = Entry(t)
		createButton = Button(t, text="Crear", command=create)
		cancelButton = Button(t, text="Cancelar", command=t.destroy)
		Label(t, text="Nombre:").grid(row=0, column=0)
		characterEntry.grid(row=1, column=0, columnspan=2)
		createButton.grid(row=2, column=0)
		cancelButton.grid(row=2, column=1)

	def removeVideo(self):
		for index in self.videos.curselection():
			item = self.videos.get(int(index))
			self.videos.delete(int(index))
	
	def playVideo(self):
		index = self.videos.curselection()
		item = self.videos.get(int(index[0]))
		self.client.start_listening(item)

class Login:
	def __init__(self, master, client):
		self.client = client
		self.master = master
		self.t = Frame(master)
		self.t.pack()
		self.logged = False
		Label(self.t, text="Username").grid(row=2, column=0, columnspan=2)
		Label(self.t, text="Password").grid(row=3, column=0, columnspan=2)

		self.e1 = Entry(self.t)
		self.e2 = Entry(self.t)

		self.e1.grid(row=2, column=3, columnspan=2)
		self.e2.grid(row=3, column=3, columnspan=2)

		btn = Button(self.t, text='Submit', command=self.login)
		btn.grid(row=4, column=0, columnspan=5)

	def login(self):
		username = self.e1.get()
		password = self.e2.get()
		print username
		print password
		self.logged = self.client.login()
		self.t.pack_forget()
		App(self.master, self.client)



root = Tk()
client = Client()
root.wm_title("Videos")
login = Login(root, client)
root.mainloop()