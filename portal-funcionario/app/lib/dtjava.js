var dtjava = function() {
	function r(d) {
		return (d != undefined && d != null)
	}
	function s(d) {
		return (d != null && typeof d != "undefined")
	}
	function B(d, aU) {
		for (var w = 0; w < d.length; w++) {
			if (aU.indexOf(d[w]) != -1) {
				return true
			}
		}
		return false
	}
	var x = (function() {
		var d = document.getElementsByTagName("script");
		var w = d[d.length - 1].getAttribute("src");
		return w.substring(0, w.lastIndexOf("/") + 1)
	})();
	var ai = false;
	var az = "1.7.0_06";
	var V = document;
	var O = window;
	var aG = false;
	var A = [];
	var Y = null;
	function k(d) {
		if (aG) {
			d()
		} else {
			A[A.length] = d
		}
	}
	function I() {
		if (!aG) {
			try {
				var w = V.getElementsByTagName("body")[0].appendChild(V
						.createElement("div"));
				w.parentNode.removeChild(w)
			} catch (aU) {
				return
			}
			aG = true;
			for (var d = 0; d < A.length; d++) {
				A[d]()
			}
		}
	}
	function a(w) {
		if (s(O.addEventListener)) {
			O.addEventListener("load", w, false)
		} else {
			if (s(V.addEventListener)) {
				V.addEventListener("load", w, false)
			} else {
				if (s(O.attachEvent)) {
					O.attachEvent("onload", w)
				} else {
					if (typeof O.onload == "function") {
						var d = O.onload;
						O.onload = function() {
							d();
							w()
						}
					} else {
						O.onload = w
					}
				}
			}
		}
	}
	function au() {
		var a7 = s(V.getElementById) && s(V.getElementsByTagName)
				&& s(V.createElement);
		var a1 = navigator.userAgent.toLowerCase(), a5 = navigator.platform
				.toLowerCase();
		var ba = a5 ? /win/.test(a5) : /win/.test(a1), aW = a5 ? /mac/.test(a5)
				: /mac/.test(a1), a0 = a5 ? /linux/.test(a5) : /linux/.test(a1), a3 = /chrome/
				.test(a1), d = !a3 && /webkit/.test(a1) ? parseFloat(a1
				.replace(/^.*webkit\/(\d+(\.\d+)?).*$/, "$1")) : false, a9 = /opera/
				.test(a1), aX = null, w = null;
		var a4 = false;
		try {
			a4 = s(window.execScript)
		} catch (aY) {
			a4 = false
		}
		if (aW) {
			if ((a5 && /intel/.test(a5)) || /intel/.test(a1)) {
				aX = "intel"
			}
			var a2 = a1.match(/(1[0-9_\.]+)[^0-9_\.]/);
			w = r(a2) ? a2[0].replace(/_/g, ".") : null
		}
		var a8 = navigator.mimeTypes;
		var aZ = null;
		var aV = null;
		var aU = null;
		for (var a2 = 0; a2 < a8.length; a2++) {
			var a6 = navigator.mimeTypes[a2].type;
			if (a6.indexOf("application/x-java-applet;jpi-version") != -1
					&& a6.indexOf("=") != -1) {
				aZ = a6.substring(a6.indexOf("=") + 1)
			}
			if (a6.indexOf("application/x-java-applet;deploy") != -1
					&& a6.indexOf("=") != -1) {
				aV = a6.substring(a6.indexOf("=") + 1)
			}
			if (a6.indexOf("application/x-java-applet;javafx") != -1
					&& a6.indexOf("=") != -1) {
				aU = a6.substring(a6.indexOf("=") + 1)
			}
		}
		return {
			haveDom : a7,
			wk : d,
			ie : a4,
			win : ba,
			linux : a0,
			mac : aW,
			op : a9,
			chrome : a3,
			jre : aZ,
			deploy : aV,
			fx : aU,
			cputype : aX,
			osVersion : w
		}
	}
	var av = false;
	function ak() {
		if (av) {
			return
		}
		Y = au();
		if (!Y.haveDom) {
			return
		}
		if ((s(V.readyState) && V.readyState == "complete")
				|| (!s(V.readyState) && (V.getElementsByTagName("body")[0] || V.body))) {
			I()
		}
		if (!aG) {
			if (s(V.addEventListener)) {
				V.addEventListener("DOMContentLoaded", I, false)
			}
			if (Y.ie && Y.win) {
				V.attachEvent("onreadystatechange", function() {
					if (V.readyState == "complete") {
						V.detachEvent("onreadystatechange", arguments.callee);
						I()
					}
				});
				if (O == top) {
					(function() {
						if (aG) {
							return
						}
						try {
							V.documentElement.doScroll("left")
						} catch (d) {
							setTimeout(arguments.callee, 0);
							return
						}
						I()
					})()
				}
			}
			if (Y.wk) {
				(function() {
					if (aG) {
						return
					}
					if (!/loaded|complete/.test(V.readyState)) {
						setTimeout(arguments.callee, 0);
						return
					}
					I()
				})()
			}
			a(I)
		}
		if (!ah()) {
			T()
		}
	}
	function F(d) {
		for ( var w in d) {
			this[w] = d[w]
		}
		this.toString = function() {
			return "MISMATCH [os=" + this.os + ", browser=" + this.browser
					+ ", jre=" + this.jre + ", fx=" + this.fx + ", relaunch="
					+ this.relaunch + ", platform=" + this.platform + "]"
		};
		this.isUnsupportedPlatform = function() {
			return this.os
		};
		this.isUnsupportedBrowser = function() {
			return this.browser
		};
		this.jreStatus = function() {
			return this.jre
		};
		this.jreInstallerURL = function(aU) {
			if (this.os && (this.jre == "old" || this.jre == "none")) {
				return am(aU)
			}
			return null
		};
		this.javafxStatus = function() {
			return this.fx
		};
		this.javafxInstallerURL = function(aU) {
			if (!this.os && (this.fx == "old" || this.fx == "none")) {
				return R(aU)
			}
			return null
		};
		this.canAutoInstall = function() {
			return G(this.platform, this.jre, this.fx)
		};
		this.isRelaunchNeeded = function() {
			return this.relaunch
		}
	}
	function aI(aU) {
		if (Y.fx != null && i(aU, Y.fx)) {
			return Y.fx
		}
		var w = f();
		if (r(w)) {
			try {
				return w.getInstalledFXVersion(aU)
			} catch (d) {
			}
		}
		return null
	}
	function C(d) {
		if (d != null) {
			return d.join(" ")
		} else {
			return null
		}
	}
	function U(w, d) {
		if (r(w)) {
			w.push(d);
			return w
		} else {
			var aU = [ d ];
			return aU
		}
	}
	function Q(aV, w, d) {
		var aX = ao(aV, true);
		if (!(r(aX) && r(aX.url))) {
			throw "Required attribute missing! (application url need to be specified)"
		}
		w = new dtjava.Platform(w);
		d = new dtjava.Callbacks(d);
		var aU = function() {
			var a1 = r(w.jvmargs) ? w.jvmargs : null;
			if (r(w.javafx)) {
				var a7 = aI(w.javafx);
				a1 = U(a1, " -Djnlp.fx=" + a7);
				if (!r(aV.toolkit) || aV.toolkit == "fx") {
					a1 = U(a1, " -Djnlp.tk=jfx")
				}
			}
			if (ah() && !(Y.linux && Y.chrome)) {
				if (aM(aX, a1, d)) {
					return
				}
			}
			var aY = f();
			if (r(aY)) {
				try {
					try {
						if (aQ("10.6+", Y.deploy)) {
							var a4 = {
								url : aX.url
							};
							if (r(a1)) {
								a4.vmargs = a1
							}
							if (r(aX.params)) {
								var a0 = {};
								for ( var a3 in aX.params) {
									a0[a3] = String(aX.params[a3])
								}
								a4.params = a0
							}
							if (r(aX.jnlp_content)) {
								a4.jnlp_content = aX.jnlp_content
							}
							var a2 = aY.launchApp(a4);
							if (a2 == 0) {
								if (s(d.onRuntimeError)) {
									d.onRuntimeError(aX.id)
								}
							}
						} else {
							if (!aY.launchApp(aX.url, aX.jnlp_content, C(a1))) {
								if (s(d.onRuntimeError)) {
									d.onRuntimeError(aX.id)
								}
							}
						}
						return
					} catch (a5) {
						if (!aY.launchApp(aX.url, aX.jnlp_content)) {
							if (s(d.onRuntimeError)) {
								d.onRuntimeError(aX.id)
							}
						}
						return
					}
				} catch (a6) {
				}
			}
			var aZ = ag(aX.url);
			if (r(V.body)) {
				V.body.appendChild(aZ)
			} else {
				V.write(aZ.innerHTML)
			}
		};
		var aW = N(w);
		if (aW != null) {
			W(aX, w, aW, d, aU)
		} else {
			aU()
		}
	}
	function K(aU, w, d) {
		if (s(d.onDeployError)) {
			d.onDeployError(aU, w)
		}
	}
	function aL(d) {
		return d != null && s(d.version)
	}
	function ab(d, aU) {
		var aV = f();
		if (aV == null) {
			return
		}
		if (aL(aV)) {
			aU(aV)
		} else {
			var w = null;
			if (!s(dtjava.dtPendingCnt) || dtjava.dtPendingCnt == 0) {
				w = function() {
					if (aL(aV)) {
						if (r(dtjava.dtPending)) {
							for ( var aW in dtjava.dtPending) {
								dtjava.dtPending[aW]()
							}
						}
						return
					}
					if (dtjava.dtPendingCnt > 0) {
						dtjava.dtPendingCnt--;
						setTimeout(w, 500)
					}
				}
			}
			if (!r(dtjava.dtPending) || dtjava.dtPendingCnt == 0) {
				dtjava.dtPending = {}
			}
			dtjava.dtPending[d] = aU;
			dtjava.dtPendingCnt = 1000;
			if (w != null) {
				w()
			}
		}
	}
	function W(aW, aV, a2, aX, aZ) {
		var w = f();
		if (Y.chrome && Y.win && w != null && !aL(w)) {
			var d;
			if (r(aW.placeholder)) {
				var aY = function() {
					O.open("http://www.java.com/en/download/faq/chrome.xml");
					return false
				};
				var a1 = "Please give Java permission to run on this browser web page.";
				var a0 = "Click for more information.";
				var a3 = "";
				ay(aW, a1, a0, a3, "javafx-chrome.png", aY);
				d = aW.id + "-embed"
			} else {
				a2.jre = "blocked";
				K(aW, a2, aX);
				d = "launch"
			}
			var aU = function() {
				var a5 = N(aV);
				if (a5 == null) {
					aZ()
				} else {
					W(aW, aV, a5, aX, aZ)
				}
			};
			ab(d, aU);
			return
		}
		if (!a2.isUnsupportedPlatform() && !a2.isUnsupportedBrowser()) {
			if (an(a2) && s(aX.onInstallNeeded)) {
				var a4 = function() {
					var a5 = N(aV);
					if (a5 == null) {
						aZ()
					} else {
						K(aW, a5, aX)
					}
				};
				aX.onInstallNeeded(aW, aV, aX, a2.canAutoInstall(), a2
						.isRelaunchNeeded(), a4);
				return
			}
		}
		K(aW, a2, aX)
	}
	function ah() {
		if (Y.deploy != null) {
			return aQ("10.6+", Y.deploy)
		}
		return false
	}
	function ad(d) {
		return d != null && s(d.version)
	}
	function aP() {
		return document.getElementById("dtlite")
	}
	function j() {		
		if (aP() != null) {
			return
		}
		var w = document.createElement("embed");
		w.width = "10";
		w.height = "10";
		w.id = "dtlite";
		w.type = "application/x-java-applet";
		var aU = document.createElement("div");
		aU.style.position = "relative";
		aU.style.left = "-10000px";
		aU.appendChild(w);
		var d = document.getElementsByTagName("body");
		d[0].appendChild(aU)
	}
	function y(w) {
		var aU = aP();
		if (aU == null) {
			j();
			aU = aP()
		}
		if (ad(aU)) {
			w(aU)
		} else {
			var d = null;
			if (!s(dtjava.dtlitePendingCnt) || dtjava.dtlitePendingCnt == 0) {
				d = function() {
					if (s(aU.version)) {
						if (dtjava.pendingLaunch != null) {
							dtjava.pendingLaunch(aU)
						}
						dtjava.pendingLaunch = null;
						return
					}
					if (dtjava.dtlitePendingCnt > 0) {
						dtjava.dtlitePendingCnt--;
						setTimeout(d, 500)
					}
				}
			}
			dtjava.pendingLaunch = w;
			dtjava.dtlitePendingCnt = 1000;
			if (d != null) {
				d()
			}
		}
	}
	function aM(aV, w, d) {
		var aU = function() {
			var aW = aP();
			if (aW == null) {
				if (s(d.onRuntimeError)) {
					d.onRuntimeError(aV.id)
				}
			}
			var aZ = {
				url : aV.url
			};
			if (r(w)) {
				aZ.vmargs = w
			}
			if (r(aV.params)) {
				var a0 = {};
				for ( var aX in aV.params) {
					a0[aX] = String(aV.params[aX])
				}
				aZ.params = a0
			}
			if (r(aV.jnlp_content)) {
				aZ.jnlp_content = aV.jnlp_content
			}
			var aY = aW.launchApp(aZ);
			if (aY == 0) {
				if (s(d.onRuntimeError)) {
					d.onRuntimeError(aV.id)
				}
			}
		};
		if (aQ("10.4+", Y.deploy)) {
			y(aU);
			return true
		}
		return false
	}
	function ag(w) {
		var d = null;
		if (Y.ie) {
			d = V.createElement("object");
			d.width = "1px";
			d.height = "1px";
			var aU = V.createElement("param");
			aU.name = "launchjnlp";
			aU.value = w;
			d.appendChild(aU);
			aU = V.createElement("param");
			aU.name = "docbase";
			aU.value = r(V.documentURI) ? V.documentURI : V.URL;
			d.appendChild(aU);
			if (!Y.ie) {
				d.type = "application/x-java-applet;version=1.7"
			} else {
				d.classid = "clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"
			}
		} else {
			d = V.createElement("embed");
			d.width = "0";
			d.height = "0";
			d.setAttribute("launchjnlp", w);
			d.setAttribute("docbase",
					(r(V.documentURI) ? V.documentURI : V.URL));
			d.type = "application/x-java-applet;version=1.7"
		}
		var aV = V.createElement("div");
		aV.style.position = "relative";
		aV.style.left = "-10000px";
		aV.appendChild(d);
		return aV
	}
	function i(aU, d) {
		if (aU == null || aU.length == 0) {
			return true
		}
		var w = (aU.charAt(aU.length - 1) == "*");
		if (!w) {
			return aQ(aU, d)
		} else {
			return (aQ(aU.charAt(0) + ".*", d) && aQ(aU.substring(0,
					aU.length - 1)
					+ "+", d))
		}
	}
	function t(w) {
		if (w != null) {
			var aU = w.charAt(w.length - 1);
			if (aU <= "0" || aU >= "9") {
				w = w.substring(0, w.length - 1)
			}
		}
		if (w == null || w.length == 0) {
			return [ 0, 0, 0, 0 ]
		}
		var d = w.split(".");
		while (d.length < 4) {
			d.push(0)
		}
		return d
	}
	function aQ(aV, w) {
		if (aV == null || aV.length == 0) {
			return true
		}
		var aY = aV.charAt(aV.length - 1);
		if (aY != "+" && aY != "*" && (aV.indexOf("_") != -1 && aY != "_")) {
			aV = aV + "*";
			aY = "*"
		}
		aV = aV.substring(0, aV.length - 1);
		if (aV.length > 0) {
			var aX = aV.charAt(aV.length - 1);
			if (aX == "." || aX == "_") {
				aV = aV.substring(0, aV.length - 1)
			}
		}
		if (aY == "*") {
			return (w.indexOf(aV) == 0)
		} else {
			if (aY == "+") {
				var aW = t(aV);
				var aU = t(w);
				for (var d = 0; d < aW.length; d++) {
					if (aW[d] < aU[d]) {
						return true
					} else {
						if (aW[d] < aU[d]) {
							return false
						}
					}
				}
				return true
			}
		}
		return false
	}
	function aJ() {
		if (!ah()) {
			var d = f();
			if (d != null) {
				return true
			}
			return false
		}
		return true
	}
	function u(aZ) {
		if (Y.jre != null) {
			if (aQ(aZ, Y.jre)) {
				return "ok"
			}
		}
		var aY = f();
		if (aY != null) {
			var aV = aY.jvms;
			for (var aX = 0; aV != null && aX < aV.getLength(); aX++) {
				if (aQ(aZ, aV.get(aX).version)) {
					if (!Y.ie && r(navigator.mimeTypes)) {
						if (!r(navigator.mimeTypes["application/x-java-applet"])) {
							return "disabled"
						}
					}
					return "ok"
				}
			}
			return "none"
		}
		if (Y.ie) {
			var d = [ "1.8.0", "1.7.0", "1.6.0", "1.5.0" ];
			for (var aU = 0; aU < d.length; aU++) {
				if (aQ(aZ, d[aU])) {
					try {
						var aW = new ActiveXObject("JavaWebStart.isInstalled."
								+ d[aU] + ".0");
						return "ok"
					} catch (w) {
					}
				}
			}
		}
		return "none"
	}
	function D() {
		var w = [ "iPhone", "iPod" ];
		var aU = B(w, navigator.userAgent);
		var d = (Y.mac && Y.chrome && Y.cputype == "intel");
		auto = aU || (f() != null);
		return {
			os : aU,
			browser : d,
			auto : auto
		}
	}
	function aj() {
		if (Y.ie) {
			try {
				var d = 10 * ScriptEngineMajorVersion()
						+ ScriptEngineMinorVersion();
				if (d < 57) {
					return true
				}
			} catch (w) {
				return true
			}
		}
		return false
	}
	function aE() {
		var d;
		if (Y.win) {
			d = Y.op || Y.wk || aj();
			return {
				os : false,
				browser : d
			}
		} else {
			if (Y.mac && Y.cputype == "intel") {
				var w = !aQ("10.7.3+", Y.osVersion);
				d = Y.op || (Y.mac && Y.chrome);
				return {
					os : w,
					browser : d
				}
			} else {
				if (Y.linux) {
					d = Y.op;
					return {
						os : false,
						browser : d
					}
				} else {
					return {
						os : true,
						browser : false
					}
				}
			}
		}
	}
	function ap(d) {
		if (r(d) && d.length > 0) {
			var w = d.charAt(d.length - 1);
			if (w == "*") {
				d = d.substring(0, d.length - 1) + "+"
			} else {
				if (w != "+") {
					d = d + "+"
				}
			}
		}
		return d
	}
	function N(d) {
		var w = new dtjava.Platform(d);
		w.jvm = ap(w.jvm);
		return g(w)
	}
	function g(aU) {
		aU = new dtjava.Platform(aU);
		var aV = "ok", a2 = "ok", a1 = false, aX = false, aY = false, w, d;
		if (r(aU.jvm) && u(aU.jvm) != "ok") {
			var aZ = u("*");
			if (aZ == "ok") {
				a2 = "old"
			} else {
				a2 = aZ
			}
			d = D();
			if (d.os) {
				a2 = "unsupported";
				aX = true
			}
			aY = d.browser
		}
		if (r(aU.javafx)) {
			d = aE();
			if (d.os || d.browser) {
				aV = "unsupported";
				aX = aX || d.os;
				aY = aY || d.browser
			} else {
				if (Y.fx != null) {
					if (i(aU.javafx, Y.fx)) {
						aV = "ok"
					} else {
						if (i("2.0+", Y.fx)) {
							aV = "old"
						}
					}
				} else {
					if (Y.win) {
						try {
							w = f();
							var a0 = w.getInstalledFXVersion(aU.javafx);
							if (a0 == "" || a0 == null) {
								a0 = w.getInstalledFXVersion("2.0+");
								if (a0 == null || a0 == "") {
									aV = "none"
								} else {
									aV = "old"
								}
							}
						} catch (aW) {
							aV = "none"
						}
					} else {
						if (Y.mac || Y.linux) {
							aV = "none"
						}
					}
				}
			}
		}
		a1 = a1 || (!aX && aY);
		if (aV != "ok" || a2 != "ok" || a1 || aX || aY) {
			return new F({
				fx : aV,
				jre : a2,
				relaunch : a1,
				os : aX,
				browser : aY,
				platform : aU
			})
		} else {
			if (!aJ()) {
				return new F({
					fx : aV,
					jre : "none",
					relaunch : a1,
					os : aX,
					browser : aY,
					platform : aU
				})
			}
		}
		return null
	}
	function S() {
		var d = null;
		d = navigator.userLanguage;
		if (d == null) {
			d = navigator.systemLanguage
		}
		if (d == null) {
			d = navigator.language
		}
		if (d != null) {
			d = d.replace("-", "_")
		}
		return d
	}
	function am(d) {
		if (!r(d)) {
			d = S()
		}
		return "http://jdl.sun.com/webapps/getjava/BrowserRedirect?host=java.com"
				+ (r(V.url) ? ("&returnPage=" + V.url) : "")
				+ (r(d) ? ("&locale=" + d) : "")
	}
	function R(d) {
		return "http://www.oracle.com/technetwork/java/javafx/downloads/index.html"
	}
	function an(d) {
		if (d != null) {
			var aU = d.jreStatus();
			var w = d.javafxStatus();
			return (aU == "none" || w == "none" || aU == "old" || w == "old")
					&& (w != "disabled" && aU != "disabled")
		}
		return false
	}
	function ax(w, aU, aV, a0, aY, a2) {
		var a1, d;
		if (aU) {
			a1 = "Java";
			d = "java"
		} else {
			a1 = "JavaFX";
			d = "javafx"
		}
		var aX, aW, aZ;
		if (aV) {
			aX = "A newer version of " + a1
					+ "is required to view the content on this page.";
			aW = "Please click here to update " + a1;
			aZ = "upgrade_" + d + ".png"
		} else {
			aX = "View the content on this page.";
			aW = "Please click here to install " + a1;
			aZ = "get_" + d + ".png"
		}
		var a3 = "Click to install " + a1;
		ay(w, aX, aW, a3, aZ, a2)
	}
	function ay(w, aX, aW, a0, aY, aZ) {
		var d = V.createElement("div");
		d.width = w.width;
		d.height = w.height;
		var aV = V.createElement("a");
		aV.href = "";
		aV.onclick = function() {
			aZ();
			return false
		};
		if (w.width < 250 || w.height < 160) {
			d.appendChild(V.createElement("p")
					.appendChild(V.createTextNode(aX)));
			aV.appendChild(V.createTextNode(aW));
			d.appendChild(aV)
		} else {
			var aU = V.createElement("img");
			aU.src = x + aY;
			aU.alt = a0;
			aU.style.borderWidth = "0px";
			aU.style.borderStyle = "none";
			aV.appendChild(aU);
			d.appendChild(aV)
		}
		m(w.placeholder);
		w.placeholder.appendChild(d)
	}
	function ar(d) {
		if (aQ(d.jvm, az) && i(d.javafx, "2.2.0")) {
			return true
		}
		return false
	}
	function l(aV, w, aW, aZ, a0, a1) {
		var aY = function() {
			e(aV, w, aW, a1)
		};
		var a2 = g(w);
		if (!r(a2)) {
			if (r(a1)) {
				a1()
			}
		}
		var aX = r(a2)
				&& (a2.javafxStatus() == "old" || a2.jreStatus() == "old");
		if (r(aV.placeholder)) {
			if (ar(w)) {
				ax(aV, true, aX, aZ, a0, aY)
			} else {
				ax(aV, (a2.jreStatus() != "ok"), aX, aZ, a0, aY)
			}
		} else {
			var d = aZ;
			var aU = null;
			if (!d) {
				if (ar(w)) {
					if (aX) {
						aU = "A newer version of Java is required to view the content on this page. Please click here to update Java."
					} else {
						aU = "To view the content on this page, please click here to install Java."
					}
					d = confirm(aU)
				} else {
					if (aX) {
						aU = "A newer version of JavaFX is required to view the content on this page. Please click here to update JavaFX."
					} else {
						aU = "To view the content on this page, please click here to install JavaFX."
					}
					d = confirm(aU)
				}
			}
			if (d) {
				aY()
			}
		}
	}
	function n(d) {
		if (!Y.ie) {
			return true
		}
		if (aQ("10.0.0+", f().version)) {
			return true
		}
		if (d == null) {
			return false
		}
		return !aQ("1.6.0_33+", d)
	}
	function G(d, aV, w) {
		if (!Y.win) {
			return false
		}
		var aU = f();
		if (aU == null || !s(aU.version)) {
			return false
		}
		if (aV != "ok") {
			if (!n(d.jvm)) {
				return false
			}
		}
		if (w != "ok") {
			if (!ar(d)) {
				if (!aQ("10.0.0+", f().version)) {
					return false
				}
			} else {
				if (!n(az)) {
					return false
				}
			}
		}
		return true
	}
	function e(aV, aU, aW, aZ) {
		var a1 = g(aU);
		aW = new dtjava.Callbacks(aW);
		if (r(a1) && a1.isUnsupportedPlatform()) {
			K(aV, a1, aW);
			return false
		}
		var a0 = (aV != null) ? aV.placeholder : null;
		var d, aX;
		if (an(a1)) {
			if (a1.canAutoInstall()) {
				var w = f();
				var aY = function() {
					var a4 = function(a5) {
						if (a5 == 10000 + 1) {
							return
						}
						d = [ "success", "ignore", "error:download",
								"error:generic", "error:generic",
								"error:generic", "error:generic",
								"error:cancelled" ];
						if (a5 > 19900) {
							if (a5 == 20000 + 1602 || a5 === 20000 - 2) {
								aX = "error:cancelled"
							} else {
								aX = "error:generic"
							}
						} else {
							if (a5 >= 10000 && a5 <= 19900) {
								aX = (a5 >= 10000 && a5 < 10000 + d.length) ? d[a5 - 10000]
										: "error:unknown"
							} else {
								aX = "error:generic"
							}
						}
						if (s(aW.onInstallFinished)) {
							aW.onInstallFinished(a0, "javafx", aX, a1
									.isRelaunchNeeded())
						}
						if (a5 == 0) {
							if (r(aZ)) {
								aZ()
							}
						}
					};
					if (s(aW.onInstallStarted)) {
						aW.onInstallStarted(a0, "JavaFX", true, true)
					}
					var a3 = 0;
					try {
						a3 = w.installJavaFX(aU.javafx, a4)
					} catch (a2) {
						a3 = 0
					}
					if (a3 == 0) {
						a3 = w.installJavaFX(aU.javafx);
						setTimeout(function() {
							setTimeout(function() {
								a4(a3 ? 1 : 0)
							}, 0)
						}, 0)
					}
				};
				if (a1.jre != "ok" || ar(aU)) {
					setTimeout(function() {
						var a3 = function(a6) {
							if (a6 == 10000 + 1) {
								return
							}
							if (a6 > 19900) {
								aX = "error:generic"
							} else {
								if (a6 == -1) {
									aX = "error:generic"
								} else {
									if (a6 > 10000) {
										aX = "error:generic"
									} else {
										if (a6 == 0) {
											aX = "success"
										} else {
											aX = "error:generic"
										}
									}
								}
							}
							if (s(aW.onInstallFinished)) {
								aW.onInstallFinished(a0, "jre", aX, a1
										.isRelaunchNeeded())
							}
							if (a6 == 0) {
								a1 = g(aU);
								if (a1 != null && a1.jre == "ok" && !ai
										&& a1.fx != "ok") {
									setTimeout(aY, 0)
								} else {
									if (aZ != null) {
										aZ()
									}
								}
							}
						};
						if (s(aW.onInstallStarted)) {
							aW.onInstallStarted(a0, "Java", true, true)
						}
						var a4 = 0;
						try {
							a4 = w.installJRE(aU.jvm, aU.javafx, a3)
						} catch (a2) {
							a4 = 0
						}
						if (a4 == 0) {
							var a5 = aU.jvm;
							if (a1.fx != "ok" && ar(aU)) {
								a5 = az;
								if (aU.jvm.indexOf("*") != -1) {
									a5 += "*"
								} else {
									if (aU.jvm.indexOf("+") != -1) {
										a5 += "+"
									}
								}
							}
							try {
								a4 = w.installJRE(a5, a3)
							} catch (a2) {
								a4 = 0
							}
							if (a4 == 0) {
								try {
									a4 = w.installJRE(a5)
								} catch (a2) {
									a4 = 0
								}
								setTimeout(function() {
									setTimeout(function() {
										a3(a4)
									}, 0)
								}, 0)
							}
						}
					}, 0)
				} else {
					if (!ai && a1.fx != "ok") {
						setTimeout(aY, 0)
					}
				}
			} else {
				if (a1.jre != "ok" || ar(aU)) {
					if (s(aW.onInstallStarted)) {
						aW.onInstallStarted(a0, "Java", false, f() != null)
					}
					aO()
				} else {
					if (a1.fx != "ok") {
						if (s(aW.onInstallStarted)) {
							aW.onInstallStarted(a0, "JavaFX", false,
									f() != null)
						}
						aK()
					} else {
						K(aV, a1, aW)
					}
				}
			}
		} else {
			if (aZ != null) {
				aZ()
			}
			return true
		}
		return false
	}
	function aO() {
		O.open(am())
	}
	function aK() {
		O.open(ac)
	}
	function aS(aX) {
		if (aX.placeholder != null) {
			var aV = aX.width, aZ = aX.height;
			var aY = !(aV < 100 && aZ < 100);
			var aU = aY ? "javafx-loading-100x100.gif"
					: "javafx-loading-25x25.gif";
			var d = aY ? 80 : 25;
			var aW = aY ? 80 : 25;
		} else {
			return null
		}
	}
	function aD(w) {
		if (w.placeholder != null) {
			var d = V.createElement("p");
			d.appendChild(V.createTextNode("FIXME - add real message!"));
			return d
		}
	}
	function m(d) {
		while (d.hasChildNodes()) {
			d.removeChild(d.firstChild)
		}
	}
	function ae(aW, aU, d, w) {
		if (aW != null) {
			var aV = null;
			if (d) {
				aV = (aU == "JavaFX") ? "install:inprogress:javafx"
						: "install:inprogress:jre"
			} else {
				aV = (aU == "JavaFX") ? "install:inprogress:javafx:manual"
						: "install:inprogress:jre:manual"
			}
			aN(aV)
		}
	}
	function o(aX, w, d, aW) {
		var aU;
		if (d != "success") {
			var aV = null;
			if (w == "javafx") {
				if (!aJ()) {
					aV = "install:fx:error:nojre"
				} else {
					aV = "install:fx:" + d
				}
			} else {
				aV = "install:jre:" + d
			}
			if (aX != null) {
				aU = P(aV, null);
				m(aX);
				aX.appendChild(aU)
			} else {
				O.alert(at(aV))
			}
		} else {
			if (aW) {
				aU = aN("install:fx:restart");
				m(aX);
				aX.appendChild(aU)
			}
		}
	}
	function aT(w, d) {
		if (d == null) {
			code = "success"
		} else {
			if (d.isUnsupportedBrowser()) {
				code = "browser"
			} else {
				if (d.jreStatus() != "ok") {
					code = "jre:" + d.jreStatus()
				} else {
					if (d.javafxStatus() != "ok") {
						code = "javafx:" + d.javafxStatus()
					} else {
						if (d.isRelaunchNeeded()) {
							code = "relaunch"
						} else {
							code = "unknown " + d.toString()
						}
					}
				}
			}
		}
		if (w.placeholder != null) {
			E(w.id, code, null)
		} else {
			O.alert(at(code))
		}
	}
	function X(w) {
		var d = M(w);
		if (L(w) != null) {
			E(w, "launch:fx:generic:embedded", function() {
				aq(M(w), false);
				return false
			})
		} else {
			O.alert(at("launch:fx:generic"))
		}
	}
	function f() {
		navigator.plugins.refresh(false);
		return document.getElementById("dtjavaPlugin")
	}
	function T() {
		if (f() != null) {
			return
		}
		if (!r(V.body) && !aG) {
			k(function() {
				T()
			});
			return
		}
		var aU = null;
		if (Y.ie) {
			aU = V.createElement("object");
			aU.width = "1px";
			aU.height = "1px";
			aU.classid = "clsid:CAFEEFAC-DEC7-0000-0001-ABCDEFFEDCBA"
		} else {
			if (!Y.wk && !Y.op && navigator.mimeTypes != null) {
				var aW = "application/java-deployment-toolkit";
				var aV = false;
				for (var w = 0; w < navigator.mimeTypes.length; w++) {
					var d = navigator.mimeTypes[w];
					aV = aV || ((d.type == aW) && d.enabledPlugin)
				}
				if (aV) {
					aU = V.createElement("embed");
					aU.setAttribute("type", aV ? aW : oldMimeType);
					aU.setAttribute("hidden", "true")
				}
			}
		}
		if (aU != null) {
			aU.setAttribute("id", "dtjavaPlugin");
			V.body.appendChild(aU)
		}
	}
	var z = 0;
	function aA(d) {
		if (r(d.id)) {
			return d.id
		} else {
			z++;
			return ("dtjava-app-" + z)
		}
	}
	function H(aX, w, d) {
		var aZ = V.createElement("div");
		aZ.width = aX.width;
		aZ.height = aX.height;
		aZ.id = aX.id + "-app";
		aZ.style.position = "relative";
		var aY = V.createElement("applet");
		aY.code = "dummy.class";
		aY.id = aX.id;
		aY.width = aX.width;
		aY.height = aX.height;
		var aW = {
			jnlp_href : aX.url,
			java_status_events : true,
			type : "application/x-java-applet"
		};
		if (r(aX.jnlp_content)) {
			aW.jnlp_embedded = aX.jnlp_content
		}
		if (r(w.javafx)) {
			if (!r(aX.toolkit) || aX.toolkit == "fx") {
				aW.javafx_version = ((w.javafx == "*") ? "2.0+" : w.javafx)
			}
			aW.separate_jvm = true;
			aW.javafx_applet_id = aY.id;
			aW.scriptable = true
		} else {
			if (aX.scriptable) {
				aW.scriptable = true
			}
			if (aX.sharedjvm) {
				aW.separate_jvm = true
			}
		}
		if (r(w.jvmargs)) {
			aW.java_arguments = w.jvmargs
		}
		var aV;
		for (aV in aX.params) {
			if (!r(aW[aV])) {
				var p = V.createElement("param");
				p.name = aV;
				p.value = aX.params[aV];
				aY.appendChild(p)
			}
		}
		for (aV in aW) {
			p = V.createElement("param");
			p.name = aV;
			p.value = aW[aV];
			aY.appendChild(p)
		}
		if (s(d.onGetNoPluginMessage)) {
			p = V.createElement("noapplet");
			var aU = d.onGetNoPluginMessage(aX);
			p.appendChild(aU)
		}
		aZ.appendChild(aY);
		return aZ
	}
	function M(w) {
		var d = V.getElementById(w + "-app");
		if (d == null) {
			d = V.getElementById(w)
		}
		return d
	}
	function aq(w, d) {
		if (!r(w)) {
			return
		}
		if (d) {
			w.style.left = -10000
		} else {
			w.style.left = "0px"
		}
	}
	function Z(w, d) {
		if (!r(w)) {
			return
		}
		if (d) {
			w.style.visibility = "hidden"
		} else {
			w.style.visibility = "visible"
		}
	}
	function h(aV) {
		try {
			var aU = L(aV);
			if (aU != null && aU.style != null
					&& aU.style.visibility == "visible") {
				return
			}
			var d = M(aV);
			aq(d, false);
			Z(V.getElementById(aV + "-splash"), true)
		} catch (w) {
		}
	}
	var ac = "http://java.com/javafx";
	var al = {
		"launch:fx:generic" : [
				"JavaFX application could not launch due to system configuration.",
				" See ", "a", "http://java.com/javafx", "java.com/javafx",
				" for troubleshooting information." ],
		"launch:fx:generic:embedded" : [
				"JavaFX application could not launch due to system configuration ",
				"(", "onclick", "show error details", ").", " See ", "a",
				"http://java.com/javafx", "java.com/javafx",
				" for troubleshooting information." ],
		"install:fx:restart" : [
				"Restart your browser to complete the JavaFX installation,",
				" then return to this page." ],
		"install:fx:error:generic" : [ "JavaFX install not completed.",
				" See ", "a", "http://java.com/javafx", "java.com/javafx",
				" for troubleshooting information." ],
		"install:fx:error:download" : [
				"JavaFX install could not start because of a download error.",
				" See ", "a", "http://java.com/javafx", "java.com/javafx",
				" for troubleshooting information." ],
		"install:fx:error:cancelled" : [ "JavaFX install was cancelled.",
				" Reload the page and click on the download button to try again." ],
		"install:jre:error:cancelled" : [ "Java install was cancelled.",
				" Reload the page and click on the download button to try again." ],
		"install:jre:error:generic" : [ "Java install not completed.", " See ",
				"a", "http://java.com/", "java.com",
				" for troubleshooting information." ],
		"install:jre:error:download" : [
				"Java install could not start because of a download error.",
				" See ", "a", "http://java.com/", "java.com/",
				" for troubleshooting information." ],
		"install:inprogress:jre" : [ "Java install in progress." ],
		"install:inprogress:javafx" : [ "JavaFX install in progress." ],
		"install:inprogress:javafx:manual" : [
				"Please download and run JavaFX Setup from ",
				"a",
				R(null),
				"java.com/javafx",
				". When complete, restart your browser to finish the installation,",
				" then return to this page." ],
		"install:inprogress:jre:manual" : [
				"Please download and run Java Setup from ", "a", am(),
				"java.com/download", ". When complete, reload the page." ],
		"install:fx:error:nojre" : [
				"b",
				"Installation failed.",
				"br",
				"Java Runtime is required to install JavaFX and view this content. ",
				"a", am(), "Download Java Runtime",
				" and run the installer. Then reload the page to install JavaFX." ],
		browser : [ "Content can not be displayed using your Web browser. Please open this page using another browser." ],
		"jre:none" : [
				"JavaFX application requires a recent Java runtime. Please download and install the latest JRE from ",
				"a", "http://java.com", "java.com", "." ],
		"jre:old" : [
				"JavaFX application requires a recent Java runtime. Please download and install the latest JRE from ",
				"a", "http://java.com", "java.com", "." ],
		"jre:plugin" : [ "b",
				"A Java plugin is required to view this content.", "br",
				"Make sure that ", "a", "http://java.com",
				"a recent Java runtime",
				" is installed, and the Java plugin is enabled." ],
		"jre:blocked" : [ "Please give Java permission to run. This will allow Java to present content provided on this page." ],
		"jre:unsupported" : [
				"b",
				"Java is required to view this content but Java is currently unsupported on this platform.",
				"br", "Please consult ", "a", "http://java.com",
				"the Java documentation", " for list of supported platforms." ],
		"jre:browser" : [
				"b",
				"Java plugin is required to view this content but Java plugin is currently unsupported in this browser.",
				"br",
				"Please try to launch this application using other browser. Please consult ",
				"a", "http://java.com", "the Java documentation",
				" for list of supported browsers for your OS." ],
		"javafx:unsupported" : [
				"b",
				"JavaFX 2.0 is required to view this content but JavaFX is currently unsupported on this platform.",
				"br", "Please consult ", "a", ac, "the JavaFX documentation",
				" for list of supported platforms." ],
		"javafx:old" : [
				"This application requires newer version of JavaFX runtime. ",
				"Please download and install the latest JavaFX Runtime from ",
				"a", ac, "java.com/javafx", "." ],
		"javafx:none" : [ "b", "JavaFX 2.0 is required to view this content.",
				"br", "a", ac, "Get the JavaFX runtime from java.com/javafx",
				" and run the installer. Then restart the browser." ],
		"javafx:disabled" : [
				"JavaFX is disabled. Please open Java Control Panel, switch to Advanced tab and enable it. ",
				"Then restart the browser." ],
		"jre:oldplugin" : [ "New generation Java plugin is required to view this content. Please open Java Control Panel and enable New Generation Java Plugin." ],
		"jre:disabled" : [
				"Java plugin appear to be disabled in your browser. ",
				" Please enable Java in the browser options." ]
	};
	function aC(w, d, aX) {
		var aV = 0;
		var aU = V.createElement("p");
		if (d != null) {
			aU.appendChild(d)
		}
		var aW;
		while (aV < w.length) {
			switch (w[aV]) {
			case "a":
				aW = V.createElement(w[aV]);
				aW.href = w[aV + 1];
				aW.appendChild(V.createTextNode(w[aV + 2]));
				aV = aV + 2;
				break;
			case "br":
				aW = V.createElement(w[aV]);
				break;
			case "b":
				aW = V.createElement(w[aV]);
				aW.appendChild(V.createTextNode(w[aV + 1]));
				aV++;
				break;
			case "onclick":
				aW = V.createElement("a");
				aW.href = "";
				if (aX == null) {
					aX = function() {
						return false
					}
				}
				aW.onclick = aX;
				aW.appendChild(V.createTextNode(w[aV + 1]));
				aV = aV + 1;
				break;
			default:
				aW = V.createTextNode(w[aV]);
				break
			}
			aU.appendChild(aW);
			aV++
		}
		return aU
	}
	function at(aV) {
		var w = "";
		var d = al[aV];
		var aU = 0;
		if (r(d)) {
			while (aU < d.length) {
				if (d[aU] != "a" && d[aU] != "br" && d[aU] != "b") {
					w += d[aU]
				} else {
					if (d[aU] == "a") {
						aU++
					}
				}
				aU++
			}
		} else {
			w = "Unknown error: [" + aV + "]"
		}
		return w
	}
	function L(d) {
		return V.getElementById(d + "-error")
	}
	function E(aV, d, w) {
		var aU = L(aV);
		if (!r(aU)) {
			return
		}
		m(aU);
		aU.appendChild(P(d, w));
		aU.style.visibility = "visible";
		Z(V.getElementById(aV + "-splash"), true);
		aq(M(aV), true)
	}
	function P(aV, aW) {
		var aU = V.createElement("div");
		var w = V.createElement("img");
		w.src = x + "error.png";
		w.width = 16;
		w.height = 16;
		w.alt = "";
		w.style.cssFloat = "left";
		w.style.styleFloat = "left";
		w.style.margin = "0px 10px 60px 10px";
		w.style.verticalAlign = "text-top";
		var d = al[aV];
		if (!r(d)) {
			d = [ aV ]
		}
		var aX = null;
		if (s(aW)) {
			aX = function() {
				if (r(aU.parentNode)) {
					aU.parentNode.removeChild(aU)
				}
				try {
					aW()
				} catch (aY) {
				}
				return false
			}
		}
		aU.appendChild(aC(d, w, aX));
		return aU
	}
	function aN(aU) {
		var w = V.createElement("div");
		var d = al[aU];
		if (!r(d)) {
			d = [ aU ]
		}
		w.appendChild(aC(d, null, null));
		return w
	}
	function ao(w, d) {
		var aU = null;
		if (r(w)) {
			if (d && typeof w === "string") {
				aU = new dtjava.App(w, null)
			} else {
				if (w instanceof dtjava.App) {
					aU = w
				} else {
					aU = new dtjava.App(w.url, w)
				}
			}
		}
		return aU
	}
	function af(w, aU) {
		var d = new dtjava.Callbacks(aU);
		if (w.javafx == null && d.onGetSplash === aS) {
			d.onGetSplash = null
		}
		return d
	}
	function aa(aU, w, aV) {
		var d = aU.id + "-" + aV;
		var aW = V.createElement("div");
		aW.id = d;
		aW.style.width = aU.width;
		aW.style.height = aU.height;
		aW.style.position = "absolute";
		aW.style.backgroundColor = "white";
		if (w != null) {
			aW.appendChild(w)
		}
		return aW
	}
	var b = {};
	function aB(aV, d) {
		if (d == null) {
			d = b[aV];
			if (r(d)) {
				b[aV] = null
			} else {
				return
			}
		}
		var w = document.getElementById(aV);
		if (!r(w)) {
			return
		}
		if (s(d.onJavascriptReady)) {
			var aU = d.onJavascriptReady;
			if (w.status < 2) {
				w.onLoad = function() {
					aU(aV);
					w.onLoad = null
				}
			}
		}
		if (s(d.onRuntimeError)) {
			if (w.status < 3) {
				w.onError = function() {
					d.onRuntimeError(aV)
				}
			} else {
				if (w.status == 3) {
					d.onRuntimeError(aV)
				}
			}
		}
	}
	function aF(aU, d) {
		if (!r(d) || !(s(d.onDeployError) || s(d.onJavascriptReady))) {
			return null
		}
		var w = V.createElement("script");
		b[aU] = d;
		w.text = "dtjava.installCallbacks('" + aU + "')";
		return w
	}
	function aR(w) {
		var d = aa(w, null, "error");
		d.style.visibility = "hidden";
		return d
	}
	function J(aV, w, aW) {
		var aZ = ao(aV, false);
		if (!(r(aZ) && r(aZ.url) && r(aZ.width) && r(aZ.height) && r(aZ.placeholder))) {
			throw "Required attributes are missing! (url, width, height and placeholder are required)"
		}
		aZ.id = aA(aZ);
		if ((typeof aZ.placeholder == "string")) {
			var aX = V.getElementById(aZ.placeholder);
			if (aX == null) {
				throw "Application placeholder [id=" + aZ.placeholder
						+ "] not found."
			}
			aZ.placeholder = aX
		}
		aZ.placeholder.appendChild(aR(aZ));
		w = new dtjava.Platform(w);
		var d = af(w, aW);
		var aU = N(w);
		var aY = function() {
			var a1 = H(aZ, w, d);
			var a2 = (d.onGetSplash == null) ? null : d.onGetSplash(aV);
			aZ.placeholder.style.position = "relative";
			if (a2 != null) {
				var a0 = aa(aZ, a2, "splash");
				Z(a0, false);
				aq(a1, true);
				m(aZ.placeholder);
				aZ.placeholder.appendChild(aR(aZ));
				aZ.placeholder.appendChild(a0);
				aZ.placeholder.appendChild(a1)
			} else {
				m(aZ.placeholder);
				aZ.placeholder.appendChild(aR(aZ));
				aZ.placeholder.appendChild(a1)
			}
			setTimeout(function() {
				aB(aZ.id, d)
			}, 0)
		};
		if (aU != null) {
			W(aZ, w, aU, d, aY)
		} else {
			aY()
		}
	}
	function q(aW) {
		if (r(aW)) {
			var d = aW.width;
			var aV = aW.height;
			var aU = "dummy";
			return new dtjava.App(aU, {
				id : aW.id,
				width : d,
				height : aV,
				placeholder : aW.parentNode
			})
		} else {
			throw "Can not find applet with null id"
		}
	}
	function aH(aZ, w, aX) {
		var aU = V.getElementById(aZ);
		var aY = q(aU);
		var d = af(w, aX);
		w = new dtjava.Platform(w);
		var aW = function() {
			aY.placeholder.insertBefore(aR(aY), aU);
			if (d.onGetSplash != null) {
				var a1 = d.onGetSplash(aY);
				if (r(a1)) {
					var a0 = aa(aY, a1, "splash");
					if (r(a0)) {
						aY.placeholder.style.position = "relative";
						aY.placeholder.insertBefore(a0, aU);
						aq(aU, true)
					}
				}
			}
		};
		var aV = N(w);
		if (aV != null) {
			W(aY, w, aV, d, aW)
		} else {
			aW()
		}
	}
	function aw(aU, w, d) {
		k(function() {
			aH(aU, w, d)
		})
	}
	ak();
	return {
		version : "20120720",
		validate : function(d) {
			return g(d)
		},
		install : function(d, w) {
			return e(null, d, w, null)
		},
		launch : function(w, d, aU) {
			return Q(w, d, aU)
		},
		embed : function(aU, w, d) {
			return J(aU, w, d)
		},
		register : function(aU, d, w) {
			return aw(aU, d, w)
		},
		hideSplash : function(d) {
			return h(d)
		},
		addOnloadCallback : function(w, d) {
			if (d || (Y.chrome && !Y.win)) {
				a(w)
			} else {
				k(w)
			}
		},
		installCallbacks : function(w, d) {
			aB(w, d)
		},
		Platform : function(d) {
			this.jvm = "1.6+";
			this.javafx = null;
			this.plugin = "*";
			this.jvmargs = null;
			for (var v in d) {
				this[v] = d[v];
				if (this["jvmargs"] != null && typeof this.jvmargs == "string") {
					this["jvmargs"] = this["jvmargs"].split(" ")
				}
			}
			this.toString = function() {
				return "Platform [jvm=" + this.jvm + ", javafx=" + this.javafx
						+ ", plugin=" + this.plugin + ", jvmargs="
						+ this.jvmargs + "]"
			}
		},
		App : function(d, w) {
			this.url = d;
			this.scriptable = true;
			this.sharedjvm = true;
			if (w != undefined && w != null) {
				this.id = w.id;
				this.jnlp_content = w.jnlp_content;
				this.width = w.width;
				this.height = w.height;
				this.params = w.params;
				this.scriptable = w.scriptable;
				this.sharedjvm = w.sharedjvm;
				this.placeholder = w.placeholder;
				this.toolkit = w.toolkit
			}
			this.toString = function() {
				var aU = "null";
				var aV = true;
				if (r(this.params)) {
					aU = "{";
					for (p in this.params) {
						aU += ((aV) ? "" : ", ") + p + " => " + this.params[p];
						aV = false
					}
					aU += "}"
				}
				return "dtjava.App: [url="
						+ this.url
						+ ", id="
						+ this.id
						+ ", dimensions=("
						+ this.width
						+ ","
						+ this.height
						+ "), toolkit="
						+ this.toolkit
						+ ", embedded_jnlp="
						+ (r(this.jnlp_content) ? (this.jnlp_content.length + " bytes")
								: "NO") + ", params=" + aU + "]"
			}
		},
		Callbacks : function(d) {
			this.onGetSplash = aS;
			this.onInstallNeeded = l;
			this.onInstallStarted = ae;
			this.onInstallFinished = o;
			this.onDeployError = aT;
			this.onGetNoPluginMessage = aD;
			this.onJavascriptReady = null;
			this.onRuntimeError = X;
			for (c in d) {
				this[c] = d[c]
			}
		}
	}
}();