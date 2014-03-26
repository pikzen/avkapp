"use strict"
var AideRapide = (function() {
	var DEFAULT_VALUE		= -1;
	var MED_PREVISCAN 		= 1;
	var MED_COUMADINE 		= 2;
	var STA_INITIATION		= 1;
	var STA_ENTRETIEN		= 2;
	var CAT_OVER75			= 1;
	var CAT_UNDER75			= 2;

	/**
	* Affiche le message principal
	* @param message Message à afficher
	*/
	function setMessage(message) {
		$("#inr-result").html(message);
	}

	/**
	* Affiche des informations additionnelles, sous le message principal
	* @param message Message à afficher
	* @param append Ajouter a ce qui existe déjà ?
	*/
	function setAdditionnalInfo(message, append) {
		// On ajoute à ce qui existe déjà ?
		if (append) {
			$("#inr-result-additionnal").append(message);
		}
		else {
			$("#inr-result-additionnal").html(message);
		}
	}

	function getHelp() {
	var hasErrors = updateErrors();

	if (!hasErrors) {
			var inr 		   = parseFloat($("#inr-value").val().replace(",", "."));
			var stage 		   = $("#treatment-stage").val();
			var typeMed 	   = $("#med-type").val();
			var catPatient 	   = $("#patient-cat").val();
			var hemorragie 	   = $("#hemorragie").prop("checked");
			var inrtarget      = $("#inr-target").val();

			setAdditionnalInfo("", false);
			setMessage("");

			if (stage == STA_INITIATION) {
				setValueInitiation(inr, typeMed, catPatient, inrtarget)
			}
			else  if (stage == STA_ENTRETIEN) {
				setValueEntretien(inr, typeMed, inrtarget, hemorragie);
			}
			$("#inr-result-value").html("INR: " + inr);
		}
	}

	function updateSelects() {
		updateErrors();

		var treatmentValue = $("#treatment-stage").val();

		/* Traitement */
		if (treatmentValue == -1) {
			$(".previscan-phase").hide(); 		 // Tout cacher
			$(".patient-cat").hide();
			$(".hemorragie").hide();
			$(".coumadine-phase").hide();
		}
		else if (treatmentValue == STA_INITIATION) {
			$(".coumadine-phase").hide(); 		 // Cacher ce qui est spécifique à la coumadine
			$(".hemorragie").hide();
			$(".patient-cat").show();
		}
		else if (treatmentValue == STA_ENTRETIEN) {
			$(".previscan-phase").hide(); 		 // Cacher ce qui est spécifique au préviscan
			$(".patient-cat").hide();
			$(".hemorragie").show();
			$(".coumadine-phase").hide();
		}
	}

	/**
	* Met à jour l'affichage des erreurs
	*/
	function updateErrors() {
		// Flags des erreurs
		var Flag_INR = 1;  // 0001
		var Flag_Med = 2;  // 0010
		var Flag_Cat = 4;  // 0100
		var Flag_Sta = 8;  // 1000
		var ErrorFlags = 0;

		// Valeurs récuperées sur le form
		var treatmentValue = $("#treatment-stage").val();
		var medValue 	   = $("#med-type").val();
		var catValue 	   = $("#patient-cat").val();
		var inrValue 	   = $("#inr-value").val();

		// On cache toutes les erreurs
		$("#inr-value-error").hide();
		$(".patient-cat .error").hide();
		$(".med-type .error").hide();
		$(".treatment-stage .error").hide();

		// Vérification de tous les paramètres.
		// Si il y a une erreur, on set le flag
		// Après, on vérifiera ce qu'il faut afficher
		// INR relevé
		if (isNaN(inrValue)) {
			ErrorFlags |= Flag_INR;
		}

		if (treatmentValue == DEFAULT_VALUE) {
			ErrorFlags |= Flag_Sta;
		}

		if (medValue == DEFAULT_VALUE ) {
			ErrorFlags |= Flag_Med;
		}

		if (catValue == DEFAULT_VALUE && treatmentValue == STA_INITIATION) {
			ErrorFlags |= Flag_Cat;
		}


		// Affichage des erreurs
		if (ErrorFlags & Flag_INR) { $("#inr-value-error").show();    		}
		if (ErrorFlags & Flag_Cat) { $(".patient-cat .error").show(); 		}
		if (ErrorFlags & Flag_Med) { $(".med-type .error").show();    		}
		if (ErrorFlags & Flag_Sta) { $(".treatment-stage .error").show(); 	}

		// Si il n'y a pas eu d'erreurs, ErrorFlags = 0000 (false)
		return ErrorFlags;
	}

	/**
	* Affichage de l'aide rapide dans le cas de l'entretien
	* @param inr INR relevé
	* @param medicament Medicament utilisé
	* @param target INR Cible
	* @param hemorragie Le patient est il en hémorragie ?
	*/
	function setValueEntretien(inr, medicament, target, hemorragie) {
		if (hemorragie) {
			setMessage("En cas de surdosage, appeler le médecin traitant et/ou hospitaliser. 10mg de vitamine K en attendant.");
			return;
		}

		var delta = (target == 1 ? 0.0 : 1.0);
		console.warn(delta);

		if (medicament == MED_COUMADINE) {
			if (inr < 1.8 + delta) {
				console.log(2.0 + delta);

				setMessage("<strong>Augmenter</strong> la posologie actuelle d'1mg. Contrôler dans 7 jours");
				setAdditionnalInfo("<h5>En cas de sous-dosage</h5>" +
									"<ol>" +
									"<li>Identifier la cause</li>" +
									"<li>Majorer le traitement d'1mg de Coumadine</li>" +
									"<li>Discuter de l'intéret d'introduction d'Héparine</li>" +
									"<li>Contrôler l'INR dans 4 jours</li>" +
									"</ol>", false);
			}
			else if (inr > 3.2 + delta) {
				setMessage("<strong>Diminuer</strong> la posologie actuelle d'1mg. Contrôler dans 7 jours");

				setAdditionnalInfo("<h5>En cas d'INR trop elevé asymptomatique : </h5>" +
								   "<ol><li>Identifier le facteur intercurrent</li>", false);
				if (inr < 6.0 + delta) {
					setAdditionnalInfo("<li>Sauter une prise, pas d'apport de vitamine K. Contrôler l'INR le lendemain. Diminuer la posologie actuelle de 1mg. Reprise du traitement si l'INR est inferieur à 2.5</li>",
										true);
				}
				else if (inr < 10.0 + delta) {
					setAdditionnalInfo("<li><strong>Arrêt</strong> du traitement. Prendre 1 à 2mg de vitamine K par voie orale (ampoule pédiatrique). Contrôler l'INR le lendemain. Recommencer le traitement lorsque l'INR est inferieur à 2.5, en commencant a 1mg.</li>",
										true);
				}
				else if (inr > 10.0) {
					setAdditionnalInfo("<li><strong>Arrêt</strong> du traitement. Prendre 5mg de vitamine K par voie orale. Contrôler l'INR le lendemain. Recommencer le traitement lorsque l'INR est inferieur à 2.5, en commencant a 1mg.</li>",
										true);
				}
				setAdditionnalInfo("</ol>", true);
			}
			else {
				setMessage("INR situé dans les bornes normales.");
			}

		}
		else if (medicament == MED_PREVISCAN) {
			if (inr < 1.8 + delta) {
				setMessage("<strong>Augmenter</strong> la posologie actuelle de 20% par palier de 5mg (1/4 comprimé) sur 3 jours.");
				setAdditionnalInfo("<h5>En cas de sous-dosage</h5>" +
									"<ol>" +
									"<li>Identifier la cause</li>" +
									"<li>Majorer le traitement de 5mg (1/4 comprimé) de Previscan</li>" +
									"<li>Discuter de l'intéret d'introduction d'Héparine</li>" +
									"<li>Contrôler l'INR dans 3 jours</li>" +
									"</ol>", false);
			}
			else if (inr > 3.2 + delta) {
				setMessage("<strong>Diminuer</strong> la posologie actuelle de 20% par palier de 5mg (1/4 comprimé) sur 3 jours.");

				setAdditionnalInfo("<h5>Conduite a tenir en cas d'INR trop elevé asymptomatique : </h5>" +
								   "<ol><li>Identifier le facteur intercurrent</li>", false);
				if (inr < 6.0 + delta) {
					setAdditionnalInfo("<li>Sauter une prise, pas d'apport de vitamine K. Contrôler l'INR le lendemain. Diminuer la posologie actuelle de 20% par palier de 5mg (1/4 comprimé) sur 3 jours. Reprise du traitement si l'INR est inferieur à 2.5</li>",
										true);
				}
				else if (inr < 10.0 + delta) {
					setAdditionnalInfo("<li><strong>Arrêt</strong> du traitement. Prendre 1 à 2mg de vitamine K par voie orale (ampoule pédiatrique). Contrôler l'INR le lendemain. Recommencer le traitement lorsque l'INR est inferieur à 2.5, en commencant a 1mg.</li>",
										true);
				}
				else if (inr > 10.0) {
					setAdditionnalInfo("<li><strong>Arrêt</strong> du traitement. Prendre 5mg de vitamine K par voie orale. Contrôler l'INR le lendemain. Recommencer le traitement lorsque l'INR est inferieur à 2.5, en commencant a 1mg.</li>",
										true);
				}
				setAdditionnalInfo("</ol>", true);
			}
			else {
				setMessage("INR situé dans les bornes normales.");
			}
		}
	}

	/**
	* Affiche l'aide rapide dans le cas de l'initiation
	* @param inr INR relevé
	* @param medicament Medicament utilisé
	* @param categPatient Catégorie du patient
	* @param target INR Cible
	*/
	function setValueInitiation(inr, medicament, categPatient, target) {
		var delta = (target == 1 ? 0 : 1);

		if (medicament == MED_COUMADINE) {
			if (categPatient == CAT_UNDER75) {
				if (inr > 0 && inr < 1.3 + delta) {
					setMessage("6mg de Coumadine");
				}
				else if (inr < 1.5 + delta) {
					setMessage("5mg de Coumadine");
				}
				else if (inr < 1.7 + delta) {
					setMessage("4mg de Coumadine");
				}
				else if (inr < 1.9 + delta) {
					setMessage("3mg de Coumadine");
				}
				else if (inr < 2.5 + delta) {
					setMessage("1mg de Coumadine");
				}
				else {
					setMessage("<strong>Arrêt<strong> du traitement jusqu'a ce que l'INR soit inférieur à 2.5, et reprendre à 1mg");
				}
			}
				// Patient < 75 ans & <45 kgs
			else if (categPatient == CAT_OVER75) {
				if (inr > 0 && inr < 1.3 + delta) {
					setMessage("5mg de Coumadine");
				}
				else if (inr < 1.5 + delta) {
					setMessage("4mg de Coumadine");
				}
				else if (inr < 1.7 + delta) {
					setMessage("3mg de Coumadine");
				}
				else if (inr < 1.9 + delta) {
					setMessage("2mg de Coumadine");
				}
				else if (inr < 2.5 + delta) {
					setMessage("1mg de Coumadine");
				}
				else {
					setMessage("<strong>Arrêt<strong> du traitement jusqu'a ce que l'INR soit inférieur à 2.5, et reprendre à 1mg");
				}
			}
		}
		else if (medicament == MED_PREVISCAN) {
			if (inr > 0 && inr < 1.6 + delta) {
				setMessage("<strong>Augmenter</strong> la posologie actuelle de 10mg (1/2 comprimé)");
			}
			else if (inr < 2.0 + delta) {
				setMessage("<strong>Augmenter</strong> la posologie actuelle de 5mg (1/4 comprimé)");
			}
			else if (inr < 2.5 + delta) {
				setMessage("Aucun changement à la posologie.");
			}
			else if (inr < 3.0 + delta) {
				setMessage("Si la dose actuelle est supérieur à 20mg (1 comprimé), <strong>diminuer</strong> la posologie actuelle de 5mg (1/4 comprimé). Sinon, pas de changement");
			}
			else {
				setMessage("Si la dose actuelle est supérieur à 15mg (3/4 comprimé), <strong>diminuer</strong> la posologie actuelle de 10mg (1/2 comprimé). Sinon, pas de changement");
			}
		}
	}
	return {
		GetHelp: function() {
			getHelp();
		},
		UpdateSelects: function() {
			updateSelects();
		}
	}
}());



var nav_open = true;

function toggleNavigation() {
	if (nav_open) {
		$(".navigation").removeClass("nav-open");
		$(".navigation").addClass("nav-closed");
		$(".content").addClass("expand");
	}
	else {
		$(".navigation").addClass("nav-open");
		$(".navigation").removeClass("nav-closed");
		$(".content").removeClass("expand");
	}

	nav_open = !nav_open;
}

$(document).ready(function() {
	$(".toggle-sidebar").click(function() {
		toggleNavigation();
	})
})


/*
* Declaration du module angular, + magie.
*/


var app = angular.module('avkapp', ['ngRoute']);
app.factory('loginService', ['$http', function($http){
	var User = {
		isLogged: false,
		username: '',
		pin: '',
		password: ''
	};

	return {
		login: function(loginInfo, pin, callback) {
			$http.post("/avkapp/rest/login", loginInfo)
			.success(function(data) {
				User.isLogged = true;
				User.username = loginInfo.username;
				User.password = loginInfo.password;
				User.pin = pin;

        		User.firstname = data.firstname;
				User.lastname = data.lastname;
				User.email = data.email;
				User.phone = data.phone;

				User.role = {};
				User.role.id = data.profile;
				User.role.text = data.profileText;

				User.office = {};
				User.office.id = data.office;
				User.office.text = data.officeText;

				User.isAdmin = (data.profile == 1);
				User.isResponsable = (data.profile == 2);
				User.isMedecin = (data.profile == 3);
				User.isInfirmier = (data.profile == 4);
				User.isAutoMed = (data.profile == 5);

				User.hasOffice = (data.office != 1 && data.office != 2);

				callback();
			})
			.error(function(data) {
				console.log("Error ! Error !");
			});
		},
		logout: function(callback) {
			User = {};
      		User.isLogged = false;
			User.username = '';
			User.pin = '';
			User.password = '';

			callback();
		},

		isLogged: function() {
			return User.isLogged;
		},
		getUser: function() {
			return User;
		},
		listWaiting: function(out) {

		},
		listUsers: function(out) {
			if (User.isLogged) {
				var body = {username: User.username, password: User.password}

				$http.post("/avkapp/rest/users", body)
				.success(function(data) {
					out = data;
				});
			}
		}
	}
}]);

app.config(function($routeProvider) {
	$routeProvider

	.when('/', {
		templateUrl: 'pages/aiderapide.html',
		controller: 'aiderapideController'
	})
	.when('/interactions', {
		templateUrl: 'pages/interactions.html',
		controller: 'interactionsController'
	})
	.when('/historique', {
		templateUrl: 'pages/historique.html',
		controller: 'historiqueController'
	})
	.when("/inscription", {
		templateUrl: 'pages/inscription.html',
		controller: 'inscriptionController'
	})
	.when("/addoffice", {
		templateUrl: 'pages/addoffice.html',
		controller: 'addofficeController'
	})
	.when("/validate", {
		templateUrl: 'pages/validateuser.html',
		controller: 'validateController'
	})
	.when("/user", {
		templateUrl: 'pages/userinfo.html',
		controller: 'useradminController'
	})
	.when("/login", {
		templateUrl: 'pages/login.html',
		controller: 'loginController'
	})
	.when("/registrationok", {
		templateUrl: 'pages/registrationok.html',
		controller: 'regokController'
	})
	.when("/loginok", {
		templateUrl: 'pages/loginok.html',
		controller: 'loginokController'
	})
	.when("/logout", {
		templateUrl: 'pages/login.html',
		controller: 'loginController'
	})

	.otherwise({redirectTo: '/'});
});


app.controller('regokController', ['$scope', 'loginService', function($scope, Login) {
	$scope.pageName = "Pls halp";
	$scope.userConnected = Login.isLogged();
}]);

app.controller('aiderapideController',['$scope', 'loginService', function($scope, Login) {
	$scope.pageName = "Aide Rapide";

	$(".error").hide();

	$("#medicaments-toggler").click(function() {
		$("#medicaments-inter").toggle();
	});

	$("select").change(function() {
		AideRapide.UpdateSelects();
	});

	$("#inr-validate").click(function() {
		AideRapide.GetHelp();
	});

	$("#inr-value").on('input', function() {
		var inr = parseFloat($("#inr-value").val().replace(",", "."));
		if (isNaN(inr)) {
			$("#inr-value-error").show();
		}
		else {
			$("#inr-value-error").hide();
		}
	});
$scope.userConnected = Login.isLogged();
}]);

/*
 * Controller for interactions
 * Requires : - $scope, for data injection
 *            - $http, for requests to the REST server
 */
app.controller('interactionsController', ['$scope', '$http', 'loginService',function($scope, $http, Login) {
	$scope.pageName = "Interactions Médicamenteuses";
	$http({method:'GET', url:"/avkapp/rest/interactions"}).success(function(data) {
		$scope.interactions = data;
	}).error(function() {
		console.log("Could not contact the REST Server");
	});
	$scope.userConnected = Login.isLogged();
}]);

app.controller('inscriptionController',['$scope', '$http', '$location', 'loginService', function($scope, $http, $location, Login) {
	$scope.pageName = "Inscription";
	$scope.register = {};

	$http.get("/avkapp/rest/profiles")
		.success(function(data) {
			$scope.roles = data;
		});

	$http.get("/avkapp/rest/offices")
		.success(function(data) {
			$scope.cabinets = data;
		});

	$scope.processForm = function() {
		$scope.register.id = -1;
		var errors = false;

		if (!$scope.register.lastname.length) {
			errors = true;
		}
		if (!$scope.register.firstname.length) {
			errors = true;
		}
		if (!$scope.register.login.length) {
			errors = true;
		}
		if (!$scope.register.password.length) {
			errors = true;
		}
		if (!$scope.register.email.length) {
			errors = true;
		}
		if (!$scope.register.phone.length) {
			errors = true;
		}
		if (!$scope.register.profile) {
			errors = true;
		}
		if (!$scope.register.pin.length) {
			errors = true;
		}

		if (!errors) {
			$http.post('/avkapp/rest/inscription', $scope.register)
				.success(function(data) {
					if (data.status == 400) {
						$scope.errorEmail = data.errorEmail;
						$scope.errorLogin = data.errorLogin;
					}
				else {
					$scope.status = "Registration OK";
					$location.path("/registrationok");
				}

				$scope.response = data;
				console.log(data);
			})
			.error(function(data) {
				$scope.status = "Le serveur est indisponible. Impossible de se connecter.";
			});
		}
	}
	$scope.userConnected = Login.isLogged();
}]);

app.controller('adminController',['$scope', '$http', 'loginService', function($scope, $http, Login) {
	$http.get("/avkapp/rest/users")
		.success(function(data) {
			$scope.users = data;
		});
		$scope.userConnected = Login.isLogged();
}]);
app.controller('addofficeController',['$scope', 'loginService', function($scope, Login) {
	$scope.pageName = "Ajouter un cabinet";
	$scope.userConnected = Login.isLogged();
}]);
app.controller('validateController',['$scope', 'loginService', function($scope, Login) {
	$scope.pageName = "Validation d'utilisateurs";
	$scope.userConnected = Login.isLogged();
}]);
app.controller('loginokController', ['$scope', 'loginService', function($scope, Login) {
	$scope.pageName = "Validation d'utilisateurs";

	$scope.fullname = Login.getUser().firstname + " " + Login.getUser().lastname;
	$scope.userConnected = Login.isLogged();
}]);

/*
* #/user
*/
app.controller('useradminController',['$scope', 'loginService', '$http', function($scope, Login, $http) {
	$scope.pageName = "Utilisateur";
  $scope.user = {};
  $scope.user.isLogged = false;
  $scope.officeInfo = {};
  $scope.admin = {};

  $scope.user = Login.getUser();

  if ($scope.user.isAdmin) {
		var body = {username: $scope.user.username, password: $scope.user.password}

		$http.post("/avkapp/rest/users/waiting", body)
		.success(function(data) {
			$scope.admin.waiting = data;
		});

		$http.post("/avkapp/rest/users", body)
		.success(function(data) {
			$scope.admin.users = data;
		});

		$http.post("/avkapp/rest/offices", body)
		.success(function(data) {
			$scope.admin.offices = data;
		});

		$http.post("/avkapp/rest/offices/waiting", body)
		.success(function(data) {
			$scope.admin.officesWaiting = data;
		});
  }

  // --------------------------- Création d'un cabinet
  $scope.createOffice = function() {
    var body = {login: {username: $scope.user.username, password: $scope.user.password}, office: $scope.officeInfo};
    console.log(body);

    $http.put("/avkapp/rest/offices", body)
    .success(function(data) {
      alert("Ok");
    })
    .error(function(data) {
      alert("Error");
    });
  };
}]);
app.controller('loginController', ['$http', '$location', '$scope', 'loginService', function($http, $location, $scope, Login) {
	$scope.pageName = "Connexion";
	$scope.userConnected = Login.isLogged();
	$scope.error = "";

	$scope.loginInfo = {};

	$scope.signin = function() {
		var body = {username: $scope.loginInfo.login, password: $scope.loginInfo.password};

		Login.login(body, $scope.loginInfo.pin, function(){
			if (Login.isLogged()) {
				$scope.userConnected = true;
				$location.path("/loginok");
			}
			else {
				$scope.userConnected = false;
				$scope.error = "Connexion refusée. Vos identifiants sont incorrects, ou bien l'administrateur n'a pas encore validé votre compte.";
			}
		});

	};

	$scope.signout = function() {
		Login.logout(function() {
			$location.path("/");
			$scope.userConnected = false;
		});
	};
}]);

app.controller('historiqueController',['$scope', 'loginService','$http', function($scope, Login, $http) {
	$scope.pageName = "Fiches Patient";

	$scope.user = Login.getUser();
	$scope.userData = {};
	$scope.currentPat = {};
	$scope.histo = {};
	$scope.patientSelected = false;
	$scope.patientId = -1;

	$scope.swapPatient = function(id) {
		$scope.patientId = id;
		$scope.patientSelected = true;

		var body = {username: $scope.user.username, 
					password: $scope.user.password,
					pin:      $scope.user.pin}

		$http.post("/avkapp/rest/historique/" + $scope.patientId, body)
		.success(function(data) {
			$scope.currentPat = data;
			$scope.histo =
		})
		.error(function(data) {
			$scope.message = "Impossible de récupérer les informations du patient."
		})
	};

	$scope.addHistorique = function(id) {
		var body = {loginpin: {
						username: $scope.user.username, 
						password: $scope.user.password,
						pin:      $scope.user.pin
					},
					historique: {
						date: $scope.histo.date,
						amount: $scope.histo.amount,
						newMed: $scope.histo.med,
						inr: $scope.histo.inr
					}};

		$http.post("/avkapp/rest/historique/" + $scope.patientId, body)
		.success(function(data) {
			$scope.currentPat.historique.push(data);
			$scope.message = "Valeurs inserées dans la base de données.";
		})
		.error(function(data) {
			$scope.message = "Impossible d'insérer dans la base de données. Vérifiez que vous avez entré le bon PIN.";
		});
	};


}]);
