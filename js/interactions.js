var interactions = [
	{
		"name": "Allopurinol",
		"inr": "up",
		"desc": "Jusqu'a 8 jours après l'arrêt"
	},
	{
		"name": "Amiodarone",
		"inr": "up",
		"desc": "Jusqu'à 8 jours après l’arrêt"
	},
	{
		"name": "Androgenes",
		"inr": "up",
		"desc": "Jusqu'a 8 jours après l'arrêt"
	},
	{
		"name": "ISRS : Citalopram, Escitalopram, Fluoxetine, Fluvoxamine, Paroxetine, Sertraline",
		"inr": "up",
		"desc": ""
	},
	{
		"name": "Benzbromaron",
		"inr": "up",
		"desc": ""
	},
	{
		"name": "Aprepitants",
		"inr": "down",
		"desc": ""
	},
	{
		"name": "Aminogluthetimides",
		"inr": "down",
		"desc": "Cancer du sein et de la prostate. Jusqu'a 2 semaines après l'arrêt"
	},
	{
		"name": "Azathioprine",
		"inr": "down",
		"desc": ""
	},
	{
		"name": "Céphalosporine : Cefamandole, Cefoperazone, Cefotetan",
		"inr": "up",
		"desc": ""
	},
	{
		"name": "Ceftriaxone",
		"inr": "up",
		"desc": ""
	},
	{
		"name": "Cimetidine",
		"inr": "up",
		"desc": "Jusqu'a 8 jours après l'arrêt"
	},
	{
		"name": "Cisapride",
		"inr": "up",
		"desc": "Jusqu'a 8 jours après l'arret"
	},
	{
		"name": "Colchicine",
		"inr": "up",
		"desc": "Jusqu'a 8 jours après l'arret"
	},
	{
		"name": "Cycline",
		"inr": "up",
		"desc": ""
	},
	{
		"name": "Danazol",
		"inr": "up",
		"desc": ""
	},
	{
		"name": "Econazole",
		"inr": "up",
		"desc": "Quelque soit le mode d'administration"
	},
	{
		"name": "Fibrate",
		"inr": "up",
		"desc": "Jusqu'a 8 jours après l'arrêt"
	},
	{
		"name": "Fluconazole, Itraconazole, Voriconazole",
		"inr": "up",
		"desc": "Jusqu'a 8 jours après l'arrêt"
	},
	{
		"name": "Oflaxacine, Pefloxacine, Enoxacine, Lomefloxacine, Moxifloxacine, Ciprofloxacine, Levofloxacine, Norfloxacine",
		"inr": "up",
		"desc": ""
	},
	{
		"name": "Glucocorticoïdes, Methylprednisolone",
		"inr": "up",
		"desc": "Faire un contrôle biologique au 8e jour, puis tous les 15 jours pendant la corticothérapie et après son arrêt. Contrôle de l'INR 2 à 4 jours après le bolus de méthylprednisolone ou en présence de tous signes hémorragiques."
	},
	{
		"name": "HBPM",
		"inr": "up",
		"desc": ""
	},
	{
		"name": "Hormones thyroïdiennes : Levotyhroxine, Liothyronine Sodique, Thyroxines, Tiratricol",
		"inr": "up",
		"desc": ""
	},
	{
		"name": "Statines",
		"inr": "up",
		"desc": ""
	}
	,
	{
		"name": "Macrolides : Azithromycine, Clarithromycine, Dirithromycine, Erythromycine, Josamycine, Midecamycine, Roxithromycine, Telithromycine, Troleandomycine",
		"inr": "up",
		"desc": ""
	}
	,
	{
		"name": "Nitro-5-imidazolés : Metronidazole, Ornidazole, Secnidazole, Tinidazole",
		"inr": "up",
		"desc": ""
	}
	,
	{
		"name": "Orlistat",
		"inr": "up",
		"desc": ""
	}
	,
	{
		"name": "Paracetamol",
		"inr": "up",
		"desc": "Aux doses maximales (4g/j) pendant au moins 4 jours."
	}
	,
	{
		"name": "Pentoxyfilline",
		"inr": "up",
		"desc": "Jusqu'a 8 jours après l'arrêt."
	},
	{
		"name": "Proguanil",
		"inr": "up",
		"desc": ""
	},
	{
		"name": "Propafenone",
		"inr": "up",
		"desc": ""
	},
	{
		"name": "Sulfamethoxazole, Sulfafurazole, Sulfamethizol",
		"inr": "up",
		"desc": "Jusqu'a 8 jours après l'arrêt."
	},
	{
		"name": "Tamoxifene",
		"inr": "up",
		"desc": ""
	},
	{
		"name": "Tibolone",
		"inr": "up",
		"desc": ""
	},
	{
		"name": "Tramadol",
		"inr": "up",
		"desc": ""
	},
	{
		"name": "Viloxazine",
		"inr": "up",
		"desc": ""
	},
	{
		"name": "Vitamine E",
		"inr": "up",
		"desc": ""
	},
	{
		"name": "Anti agrégents plaquettaires",
		"inr": "up",
		"desc": ""
	},
	{
		"name": "Alcoolisme aigü",
		"inr": "up",
		"desc": ""
	},
	{
		"name": "Bosentan",
		"inr": "down",
		"desc": ""
	},
	{
		"name": "Coletstyramine",
		"inr": "down",
		"desc": "Prendre la Coletstyramine à distance de la Coumadine (plus de 2h)"
	},
	{
		"name": "Griseofulvine",
		"inr": "down",
		"desc": "Jusqu'a 8 jours après l'arrêt"
	},
	{
		"name": "Mercaptopurine",
		"inr": "down",
		"desc": ""
	},
	{
		"name": "Nevirapine, Efavirenz",
		"inr": "down",
		"desc": ""
	},
	{
		"name": "Rifampicine",
		"inr": "down",
		"desc": "Jusqu'a 8 jours après l'arrêt."
	},
	{
		"name": "Ritonavir",
		"inr": "down",
		"desc": ""
	},
	{
		"name": "Sucralfate",
		"inr": "down",
		"desc": "Prendre à distance de la Coumadine (plus de 2h)"
	}


]
