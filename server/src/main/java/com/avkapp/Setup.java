package com.avkapp;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import com.avkapp.data.Interaction;
import com.avkapp.dao.*;

public class Setup {
	public static void main(String[] args) {
		DatabaseHelper db = new DatabaseHelper();
		Connection conn = null;

		System.out.println("--------------- Installation d'AVKAPP/Server ---------");
		System.out.println("######################################################");
		System.out.println("--- Installation de la BDD ");
		try {
			conn = db.getConnection();
		}
		catch (SQLException e) {
			System.out.println("[Error] Impossible de se connecter à le base de données. Vérifiez que la base de données est bien installée, et que les informations de connexion sont bonnes");
			return;
		}
		finally {
			try {
				if (conn != null) conn.close();
			}
			catch (SQLException e) {
				// Can't do much
			}
		}
		System.out.println("   Création de la base");
		try {
			db.initDb();
		}
		catch(SQLException e) {
			System.out.println("[Error] Impossible d'initialiser db." + e.getMessage());
			return;
		}

		System.out.println("   Table: Office");
		OfficeDAO office = new OfficeDAO();
		try {
			office.initDb();
		}
		catch(SQLException e) {
			System.out.println("[Error] Impossible d'initialiser office." + e.getMessage());
			return;
		}

		System.out.println("   Table: Profile");
		ProfileDAO profile = new ProfileDAO();
		try {
			profile.initDb();
		}
		catch(SQLException e) {
			System.out.println("[Error] Impossible d'initialiser profile." + e.getMessage());
			return;
		}

		System.out.println("   Table: Medication");
		// Instant gros blob de données.
		String input = "INSERT INTO Medication(Name, INRImpact, Note) VALUES " +
                    "('Allopurinol','1','Jusqu\'a 8 jours après l\'arrêt')," +
                    "('Amiodarone','1','Jusqu\'à 8 jours après l’arrêt')," +
                    "('Androgenes','1','Jusqu\'a 8 jours après l\'arrêt')," +
                    "('ISRS : Citalopram','1','')," +
                    "('ISRS : Escitalopram','1','')," +
                    "('ISRS : Fluoxetine','1','')," +
                    "('ISRS : Fluvoxamine','1','')," +
                    "('ISRS : Sertraline','1','')," +
                    "('ISRS : Paroxetine','1','')," +
                    "('Benzbromaron','1','')," +
                    "('Aprepitants','-1','')," +
                    "('Aminogluthetimides','-1','Cancer du sein et de la prostate. Jusqu\'a 2 semaines après l\'arrêt')," +
                    "('Azathioprine','-1','')," +
                    "('Cefamandole','1','')," +
                    "('Cefoperazone','1','')," +
                    "('Cefotetan','1','')," +
                    "('Ceftriaxone','1','')," +
                    "('Cimetidine','1','Jusqu\'a 8 jours après l\'arrêt')," +
                    "('Cisapride','1','Jusqu\'a 8 jours après l\'arret')," +
                    "('Colchicine','1','Jusqu\'a 8 jours après l\'arret')," +
                    "('Cycline','1','')," +
                    "('Danazol','1','')," +
                    "('Econazole','1','Quelque soit le mode d\'administration')," +
                    "('Fibrate','1','Jusqu\'a 8 jours après l\'arrêt')," +
                    "('Fluconazole','1','Jusqu\'a 8 jours après l\'arrêt')," +
                    "('Itraconazole','1','Jusqu\'a 8 jours après l\'arrêt')," +
                    "('Voriconazole','1','Jusqu\'a 8 jours après l\'arrêt')," +
                    "('Oflaxacine','1','')," +
                    "('Pefloxacine','1','')," +
                    "('Enoxacine','1','')," +
                    "('Lomefloxacine','1','')," +
                    "('Moxifloxacine','1','')," +
                    "('Ciprofloxacine','1','')," +
                    "('Levofloxacine','1','')," +
                    "('Norfloxacine','1','')," +
                    "('Glucocorticoïdes','1','Faire un contrôle biologique au 8e jour, puis tous les 15 jours pendant la corticothérapie et après son arrêt. Contrôle de l\'INR 2 à 4 jours après le bolus de méthylprednisolone ou en présence de tous signes hémorragiques.')," +
                    "('Methylprednisolone','1','Faire un contrôle biologique au 8e jour, puis tous les 15 jours pendant la corticothérapie et après son arrêt. Contrôle de l\'INR 2 à 4 jours après le bolus de méthylprednisolone ou en présence de tous signes hémorragiques.')," +
                    "('HBPM','1','')," +
                    "('Levotyhroxine','1','')," +
                    "('Liothyronine Sodique','1','')," +
                    "('Thyroxines','1','')," +
                    "('Tiratricol','1','')," +
                    "('Statines','1','')," +
                    "('Azithromycine','1','')," +
                    "('Clarithromycine','1','')," +
                    "('Dirithromycine','1','')," +
                    "('Erythromycine','1','')," +
                    "('Josamycine','1','')," +
                    "('Midecamycine','1','')," +
                    "('Roxithromycine','1','')," +
                    "('Telithromycine','1','')," +
                    "('Troleandomycine','1','')," +
                    "('Metronidazole','1','')," +
                    "('Ornidazole','1','')," +
                    "('Secnidazole','1','')," +
                    "('Tinidazole','1','')," +
                    "('Orlistat','1','')," +
                    "('Paracetamol','1','Aux doses maximales (4g/j) pendant au moins 4 jours.')," +
                    "('Pentoxyfilline','1','Jusqu\'a 8 jours après l\'arrêt.')," +
                    "('Proguanil','1','')," +
                    "('Propafenone','1','')," +
                    "('Sulfamethoxazole','1','Jusqu\'a 8 jours après l\'arrêt.')," +
                    "('Sulfafurazole','1','Jusqu\'a 8 jours après l\'arrêt.')," +
                    "('Sulfamethizol','1','Jusqu\'a 8 jours après l\'arrêt.')," +
                    "('Tamoxifene','1','')," +
                    "('Tibolone','1','')," +
                    "('Tramadol','1','')," +
                    "('Viloxazine','1','')," +
                    "('Vitamine E','1','')," +
                    "('Anti agrégents plaquettaires','1','')," +
                    "('Alcoolisme aigü','1','')," +
                    "('Bosentan','-1','')," +
                    "('Coletstyramine','-1','Prendre la Coletstyramine à distance de la Coumadine (plus de 2h)')," +
                    "('Griseofulvine','-1','Jusqu\'a 8 jours après l\'arrêt')," +
                    "('Mercaptopurine','-1','')," +
                    "('Nevirapine','-1','')," +
                    "('Efavirenz','-1','')," +
                    "('Rifampicine','-1','Jusqu\'a 8 jours après l\'arrêt.')," +
                    "('Ritonavir','-1','')," +
                    "('Sucralfate','-1','Prendre à distance de la Coumadine (plus de 2h)')";

	try {
		db.query(input);
	}
	catch (SQLException e) {
		System.out.println("[Error] Impossible d'initialiser Medication. " + e.getMessage());
		return;
	}

	System.out.println("   Table: Notes");
	NoteDAO note = new NoteDAO();
	try {
			note.initDb();
		}
		catch(SQLException e) {
			System.out.println("[Error] Impossible d'initialiser note." + e.getMessage());
			return;
		}

	System.out.println("   Table: INRMedication");
	INRMedicationDAO inrm = new INRMedicationDAO();
	try {
			inrm.initDb();
		}
		catch(SQLException e) {
			System.out.println("[Error] Impossible d'initialiser inrm." + e.getMessage());
			return;
		}

	System.out.println("   Table:INRTreatmentPhase");
	INRTreatmentPhaseDAO inrtp = new INRTreatmentPhaseDAO();
	try {
			inrtp.initDb();
		}
		catch(SQLException e) {
			System.out.println("[Error] Impossible d'initialiser inrtp." + e.getMessage());
			return;
		}

	System.out.println("   Table: INRHistoryValue");
	INRHistoryValueDAO inrhv = new INRHistoryValueDAO();
	try {
			inrhv.initDb();
		}
		catch(SQLException e) {
			System.out.println("[Error] Impossible d'initialiser inrhv." + e.getMessage());
			return;
		}

	System.out.println("   Table: INRTreatment");
	INRTreatmentDAO inrt = new INRTreatmentDAO();
	try {
			inrt.initDb();
		}
		catch(SQLException e) {
			System.out.println("[Error] Impossible d'initialiser inrt." + e.getMessage());
			return;
		}

	System.out.println("   Table: Patient");
	PatientDAO patient = new PatientDAO();
	try {
			patient.initDb();
		}
		catch(SQLException e) {
			System.out.println("[Error] Impossible d'initialiser patient." + e.getMessage());
			return;
		}

	System.out.println("   Table: PatientNotes");
	PatientNotesDAO pnote = new PatientNotesDAO();
	try {
			pnote.initDb();
		}
		catch(SQLException e) {
			System.out.println("[Error] Impossible d'initialiser pnote." + e.getMessage());
			return;
		}

	System.out.println("   Table: Users");
	UserDAO user = new UserDAO();
	try {
		System.out.print("Mot de passe administrateur : ");
		String pw = System.console().readLine();
		System.out.print("Email administrateur : ");
		String mail = System.console().readLine();
			user.initDb(pw, mail);
		}
		catch(SQLException e) {
			System.out.println("[Error] Impossible d'initialiser user." + e.getMessage());
			return;
		}

	System.out.println("   Table: OfficeResponsable");
	OfficeResponsableDAO offresp = new OfficeResponsableDAO();
	try {
			offresp.initDb();
		}
		catch(SQLException e) {
			System.out.println("[Error] Impossible d'initialiser offresp." + e.getMessage());
			return;
		}

	System.out.println("   Table: OfficeSwitch");
	OfficeSwitchDAO offswitch = new OfficeSwitchDAO();
	try {
			offswitch.initDb();
		}
		catch(SQLException e) {
			System.out.println("[Error] Impossible d'initialiser offswitch." + e.getMessage());
			return;
		}

	System.out.println("   Table: OfficePatients");
	OfficePatientDAO offPat = new OfficePatientDAO();
	try {
			offPat.initDb();
		}
		catch(SQLException e) {
			System.out.println("[Error] Impossible d'initialiser offPat." + e.getMessage());
			return;
		}

	System.out.println("   Table: MedicationDuration");
	MedicationDurationDAO meddur = new MedicationDurationDAO();
	try {
			meddur.initDb();
		}
		catch(SQLException e) {
			System.out.println("[Error] Impossible d'initialiser meddur." + e.getMessage());
			return;
		}

	System.out.println("   Table: TreatmentDuration");
	TreatmentDurationDAO treatdur = new TreatmentDurationDAO();
	try {
			treatdur.initDb();
		}
		catch(SQLException e) {
			System.out.println("[Error] Impossible d'initialiser treatdur." + e.getMessage());
			return;
		}

	System.out.println("--- Tables initialisées");
	}
}