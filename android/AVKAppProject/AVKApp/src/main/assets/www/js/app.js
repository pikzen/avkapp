var DEFAULT_VALUE		= -1
var MED_PREVISCAN 		= 1;
var MED_COUMADINE 		= 2;
var STA_INITIATION		= 1;
var STA_ENTRETIEN		= 2;
var ETA_PREVISCAN_1		= 1;
var ETA_PREVISCAN_2		= 2;
var ETA_COUMADINE_1		= 1;
var ETA_COUMADINE_2		= 2;
var ETA_COUMADINE_3 	= 3;
var CAT_OVER75			= 1;
var CAT_UNDER75			= 2;
//------------------------------------------------

var hasClass = function(el, cn)
{
	return (' ' + el.className + ' ').indexOf(' ' + cn + ' ') !== -1;
};
var addClass = function(el, cn) {
	if (!hasClass(el, cn)) {
		el.className = (el.className === '') ? cn : el.className + ' ' + cn;
	}
}
var removeClass = function(el, cn)
{
	el.className = trim((' ' + el.className + ' ').replace(' ' + cn + ' ', ' '));
};

var nav_open = true;

function toggleNavigation() {
	if (nav_open) {
		$(".navigation").removeClass("nav-open");
		$(".navigation").addClass("nav-closed");
	}
	else {
		$(".navigation").addClass("nav-open");
		$(".navigation").removeClass("nav-closed");
	}

	nav_open = !nav_open;
}

$(document).ready(function() {

	$(document).foundation();

		$(".toggle-sidebar").click(function() {
		toggleNavigation();
	})

})
// Foundation JavaScript
// Documentation can be found at: http://foundation.zurb.com/docs


/*************************************************************************************\
* Event Handlers
\*************************************************************************************/
/*
* Affiche/cache les interactions medicamenteuses lors d'un clic
*/
function setMessage(message) {
	$("#inr-result").html(message);
}

function getHelp() {
	var hasErrors = updateErrors();

	if (!hasErrors) {

		var inr 		   = parseFloat($("#inr-value").val().replace(",", "."), "10");
		var stage 		   = $("#treatment-stage").val();
		var typeMed 	   = $("#med-type").val();
		var prevPhase 	   = $("#previscan-phase").val();
		var coumPhase 	   = $("#coumadine-phase").val();
		var catPatient 	   = $("#patient-cat").val();
		var target		   = $("#inr-result");
		var hemorragie 	   = $("#hemorragie").prop("checked");
		var inrtarget      = parseFloat($("#inr-target").val().replace(",", "."), "10");

		var hasTarget      = (!isNaN(inrtarget) && $("#inr-target").val() != "");

		if (stage == STA_INITIATION) {
			// Initiation non prise en compte dans l'aide rapide (?????)
			if (typeMed == MED_PREVISCAN) {
				// Initiation
				// Patient > 75ans & >45kgs
				if (catPatient == CAT_UNDER75) {
					if (prevPhase == ETA_PREVISCAN_1) {
						setMessage("5mg");
					}
					else if (prevPhase == ETA_PREVISCAN_2) {
						if (inr > 0 && inr < 1.3) {
							setMessage("6mg");
						}
						else if (inr < 1.5) {
							setMessage("5mg");
						}
						else if (inr < 1.7) {
							setMessage("4mg");
						}
						else if (inr < 1.9) {
							setMessage("3mg");
						}
						else if (inr < 2.5) {
							setMessage("1mg")
						}
						else {
							setMessage("Arrêt du traitement jusqu'a ce que l'INR soit inférieur à 2.5, et reprendre à 1mg");
						}
					}
				}
				// Patient < 75 ans & <45 kgs
				else if (catPatient == CAT_OVER75) {
					if (prevPhase == ETA_PREVISCAN_1) {
						setMessage("4mg");
					}
					else if (prevPhase == ETA_PREVISCAN_2) {
						if (inr > 0 && inr < 1.3) {
							setMessage("5mg");
						}
						else if (inr < 1.5) {
							setMessage("4mg");
						}
						else if (inr < 1.7) {
							setMessage("3mg");
						}
						else if (inr < 1.9) {
							setMessage("2mg");
						}
						else if (inr < 2.5) {
							setMessage("1mg")
						}
						else {
							setMessage("Arrêt du traitement jusqu'a ce que l'INR soit inférieur à 2.5, et reprendre à 1mg");
						}
					}
				}
			}
			else if (typeMed == MED_COUMADINE) {
				if (coumPhase == ETA_COUMADINE_1) {
					setMessage("20 mg");
				}
				else if (coumPhase == ETA_COUMADINE_2) {
					if (inr > 0 && inr < 1.2) {
						setMessage("30mg");
					}
					else if (inr < 1.5) {
						setMessage("25mg");
					}
					else if (inr < 2) {
						setMessage("20mg");
					}
					else if (inr < 2.2) {
						setMessage("10mg");
					}
					else if (inr < 2.3) {
						setMessage("???? [Données non fournies]");
					}
					else {
						setMessage("5mg");
					}
				}
				else if (coumPhase == ETA_COUMADINE_3) {
					if (inr > 0 && inr < 1.6) {
						setMessage("Posologie actuelle + 10mg");
					}
					else if (inr < 2.0) {
						setMessage("Posologie actuelle + 5mg");
					}
					else if (inr < 2.5) {
						setMessage("Aucun changement.");
					}
					else if (inr < 3.0) {
						setMessage("Si la dose actuelle est > à 20mg, posologie actuelle -5mg. Sinon, pas de changement");
					}
					else {
						setMessage("Si la dose actuelle est > à 15mg, posologie actuelle -10mg. Sinon, pas de changement");
					}
				}
			}
		}
		else  if (stage == STA_ENTRETIEN) {
			if (hemorragie) {
				setMessage("Appel du médecin traitant et/ou hospitalisation. 10mg de vitamine K en attendant.");
			}
			else {
				if (typeMed == MED_PREVISCAN) {
					if (hasTarget) {
						if (inr < inrtarget) {
							setMessage("+ 20% de la dose antérieure par palier de 5 mg sur 3 jours.");
						}
						else if (inr > inrtarget) {
							setMessage("- 20% de la dose antérieure par palier de 5 mg sur 3 jours.")
						}
						else {
							setMessage("INR cible atteint.");
						}
					}
					else {
						if (inr > 0 && inr < 4) {
							setMessage("1mg de Fluindione (Previscan)");
						}
						else if (inr < 6) {
							setMessage("Sauter une prise, pas d'apport de vitamine K.");
						}
						else if (inr < 10) {
							setMessage("Arrêt du traitement. 1 à 2mg de vitamine K par voie orale");
						}
						else {
							setMessage("Arrêt du traitement. 5mg de vitamine K par voie orale. Prélever l'INR le lendemain. Preferer la Warfarine et débuter selon schéma d’initiation classique, en dehors de facteurs décompensant.");
						}
					}
				}
				else if (typeMed == MED_COUMADINE) {
					if (hasTarget) {
						if (inr < inrtarget) {
							setMessage("+ 1mg de Warfarine (Coumadine), contrôler dans 7 jours.");
						}
						else if (inr > inrtarget){
							setMessage("- 1mg de Warfarine (Coumadine), contrôler dans 7 jours.")
						}
						else {
							setMessage("INR cible atteint.");
						}
					}
					else {
						if (inr > 3.2 && inr < 4) {
							setMessage("20% de la dose antérieure.");
						}
						else if (inr < 6) {
							setMessage("Sauter une prise. Pas d'apport de vitamine K. Prelever l'INR le lendemain. Diminuer de 1mg la dose antérieure, reprise du traitement quand l'INR est < à 2.5");
						}
						else if (inr < 10) {
							setMessage("Arrêt du traitement. 1 à 2mg de vitamine K par voie orale. Prelever l'INR le lendemain. Diminuer de 10mg la dose, reprise du traitement que l'INR est < 2.5");
						}
						else {
							setMessage("Arrêt du traitement. 5mg de vitamine K par voie orale. Prelever l'INR le lendemain. Débuter selon schéma d'initation classique, en dehors des facteurs décompensants.");
						}
					}
				}
			}
		}
		$("#inr-result-value").html("INR: " + inr);
	}
}

function updateErrors() {
	var treatmentValue = $("#treatment-stage").val();
	var medValue 	   = $("#med-type").val();
	var prevPhaseValue = $("#previscan-phase").val();
	var coumPhaseValue = $("#coumadine-phase").val();
	var catValue 	   = $("#patient-cat").val();
	var inrValue 	   = parseFloat($("#inr-value").val().replace(",", "."), "10");
	var inrtValue      = parseFloat($("#inr-target").val().replace(",", "."), "10");
	var errors 		   = false;

	if (isNaN(inrValue)) {
		$("#inr-value-error").show();
		console.log("INR");
		errors = true;
	}
	else {
		$("#inr-value-error").hide();
	}

	if (isNaN(inrtValue) && $("#inr-target").val() != "") {
		$("#inr-target-error").show();
		console.log("INR");
		errors = true;
	}
	else {
		$("#inr-target-error").hide();
	}

	if (treatmentValue == DEFAULT_VALUE) {
		$(".treatment-stage .error").show();
		errors = true;
		console.log("Phase");
	}
	else {
		$(".treatment-stage .error").hide();
	}

	if (medValue == DEFAULT_VALUE ) {
		$(".med-type .error").show();
		console.log("Médicament");
		errors = true;
	}
	else {
		$(".med-type .error").hide();
		if( medValue == MED_PREVISCAN && treatmentValue == STA_INITIATION) {
			if (prevPhaseValue == DEFAULT_VALUE) {
				$(".previscan-phase .error").show();
				console.log("Phase (Previscan)");
				errors = true;
			}
			else {
				$(".previscan-phase .error").hide();
			}
		}
		else if (medValue == MED_COUMADINE && treatmentValue == STA_INITIATION) {
			if (coumPhaseValue == DEFAULT_VALUE) {
				$(".coumadine-phase .error").show();
				console.log("Phase (Coumadine)");
				errors = true;
			}
			else {
				$(".coumadine-phase .error").hide();
			}
		}
	}

	if (catValue == DEFAULT_VALUE && treatmentValue == STA_INITIATION) {
		$(".patient-cat .error").show();
		console.log("Catégorie patient");
		errors = true;
	}
	else {
		$(".patient-cat .error").hide();
	}

	return errors;


}

function updateSelects() {
	updateErrors();

	var treatmentValue = $("#treatment-stage").val();
	var medValue = $("#med-type").val();
	var MED_PREVISCAN = 1;
	var MED_COUMADINE = 2;

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
		/* choix du médicament */
		if (medValue == -1) {
			$(".coumadine-phase").hide();
			$(".previscan-phase").hide();
		}
		else if (medValue == 1) {
			$(".coumadine-phase").hide();
			$(".previscan-phase").show();
		}
		else if (medValue == 2) {
			$(".previscan-phase").hide();

			$(".coumadine-phase").show();
		}
	}
	else if (treatmentValue == STA_ENTRETIEN) {
		$(".previscan-phase").hide(); 		 // Cacher ce qui est spécifique au préviscan
		$(".patient-cat").hide();
		$(".hemorragie").show();
		$(".coumadine-phase").hide();
	}


}




/*
* Declaration du module angular, + magie.
*/

	var app = angular.module('avkapp', ['ngRoute']);

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

		.otherwise({redirectTo: '/'});

	});

	app.controller('aiderapideController', function($scope) {
		$(".error").hide();

	$("#medicaments-toggler").click(function() {
		$("#medicaments-inter").toggle();
	});

	$("select").change(function() {
		updateSelects();
	});

	$("#inr-validate").click(function() {
		getHelp();
	});

	$("#inr-value").on('input', function() {
		var inr = parseFloat($("#inr-value").val().replace(",", "."), "10");
		console.log(inr);
		if (isNaN(inr)) {
			$("#inr-value-error").show();
		}
		else {
			$("#inr-value-error").hide();
		}
	});

	$("#inr-target").on('input', function() {
		var inr = parseFloat($("#inr-target").val().replace(",", "."), "10");
		console.log(inr);
		if (isNaN(inr) && $("#inr-target").val() != "") {
			$("#inr-target-error").show();
		}
		else {
			$("#inr-target-error").hide();
		}
	});
	});

	app.controller('interactionsController', function($scope) {
		$scope.interactions = interactions;
	});
