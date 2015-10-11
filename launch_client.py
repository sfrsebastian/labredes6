from Tkinter import *  # AFAIK Tkinter is always capitalized
from tkFileDialog import askopenfilename

class App:
	characterPrefix = "character_"
	def __init__(self, master):
		self.master = master  # You'll want to keep a reference to your root window
		frame = Frame(master)
		frame.pack()

		self.charbox = Listbox(frame)  # You'll want to keep this reference as an attribute of the class too.
		for chars in []:
			self.charbox.insert(END, chars)
		self.charbox.grid(row = 1, column = 0, rowspan = 5)
		charadd = Button(frame, text = " Agregar  ", command = self.addVideo).grid(row = 1, column = 1)
		charremove = Button(frame, text = " Eliminar ", command = self.removeVideo).grid(row = 2, column = 1)
		charedit = Button(frame, text = "Reproducir", command = self.playVideo).grid(row = 3, column = 1)

	def addVideo(self, initialCharacter='', initialInfo=''):
		def create():
			nombre_video = characterEntry.get()
			filename = askopenfilename()
			#funcion de agregar video
			self.charbox.insert(END, nombre_video)
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
		for index in self.charbox.curselection():
			item = self.charbox.get(int(index))
			self.charbox.delete(int(index))
			try:
				os.remove(characterPrefix + item)
			except IOError:
				print "Could not delete file", characterPrefix + item
	
	def playVideo(self):
		#funcion de reproducir video
		print 'entro'

class Login:
	def __init__(self, master):
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
		self.logged = True
		self.t.pack_forget()
		App(self.master)



root = Tk()
root.wm_title("Videos")
login = Login(root)
root.mainloop()