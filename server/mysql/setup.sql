DROP DATABASE IF EXISTS avkapp;
CREATE DATABASE IF NOT EXISTS avkapp;

GRANT USAGE ON *.* to root@localhost IDENTIFIED BY 'rootpassword';

USE avkapp;

CREATE TABLE Office (
        Id          INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        Name        VARCHAR(40) NOT NULL,
        Address     VARCHAR(200) NOT NULL,
        PhoneNumber VARCHAR(20) NOT NULL,
        Validated   BOOLEAN NOT NULL,
) ENGINE=INNODB;

CREATE TABLE Profile (
        Id          INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        Name        VARCHAR(40) NOT NULL
) ENGINE=INNODB;

CREATE TABLE Medication (
        Id          INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        Name        VARCHAR(40) NOT NULL,
        INRImpact   INT(1) NOT NULL,
        Note        VARCHAR(500)
) ENGINE=INNODB;

CREATE TABLE Notes (
        Id          INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        Date        DATE NOT NULL,
        Note        VARCHAR(800) NOT NULL
) ENGINE=INNODB;

CREATE TABLE INRMedication (
        Id          INT(1) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        Name        VARCHAR(100)
) ENGINE=INNODB;

CREATE TABLE INRTreatmentPhase (
        Id          INT(1) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        Name        VARCHAR(10)
) ENGINE=INNODB;

CREATE TABLE INRTreatment (
        Id          INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        Med         INT(1) UNSIGNED NOT NULL,
        Phase       INT(1) UNSIGNED NOT NULL,
        CONSTRAINT fkMed1 FOREIGN KEY (Med) REFERENCES INRMedication(Id),
        CONSTRAINT fkPhase1 FOREIGN KEY (Phase) REFERENCES INRTreatmentPhase(Id)
) ENGINE=INNODB;

CREATE TABLE Patient (
        Id          INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        Firstname   VARCHAR(40) NOT NULL,
        Lastname    VARCHAR(40) NOT NULL,
        Email       VARCHAR(100) NOT NULL,
        Phone       VARCHAR(20) NOT NULL,
        BirthDate   VARCHAR(20) NOT NULL,
        Address     VARCHAR(200) NOT NULL,
        INSEE       VARCHAR(14) NOT NULL,
        TargetINR   VARCHAR(20) NOT NULL,
        Treatment   INT(6) UNSIGNED NOT NULL,
        SpecialINR  VARCHAR(20),
        CONSTRAINT fkTreatment FOREIGN KEY (Treatment) REFERENCES INRTreatment(Id)
) ENGINE=INNODB;

CREATE TABLE INRHistoryValue (
        Id          INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        Date        DATE NOT NULL,
        INRValue    FLOAT(4,2) NOT NULL,
        Bleeding    BOOLEAN NOT NULL,
        Phase       INT(1) UNSIGNED NOT NULL,
        Patient     INT(6) UNSIGNED NOT NULL,
        CONSTRAINT fkPatient FOREIGN KEY (Patient) REFERENCES Patient(Id),
        CONSTRAINT fkPhase FOREIGN KEY (Phase) REFERENCES INRTreatmentPhase(Id)
) ENGINE=INNODB;


CREATE TABLE PatientNotes (
        Id          INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        Note        INT(10) UNSIGNED NOT NULL,
        Patient     INT(6) UNSIGNED NOT NULL,
        CONSTRAINT fkNote FOREIGN KEY (Note) REFERENCES Notes(Id),
        CONSTRAINT fkPatient3 FOREIGN KEY (Patient) REFERENCES Patient(Id)
) ENGINE=INNODB;

CREATE TABLE Users (
        Id          INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        Firstname   VARCHAR(40) NOT NULL,
        Lastname    VARCHAR(40) NOT NULL,
        Email       VARCHAR(40) NOT NULL,
        Login       VARCHAR(20) UNIQUE NOT NULL,
        Phone       VARCHAR(20) NOT NULL,
        -- SHA256 génère des hash de 256 bits, soit 64 caractères.
        Password    VARCHAR(64) NOT NULL,
        PIN         VARCHAR(64) NOT NULL,
        Profile     INT(6) UNSIGNED NOT NULL,
        Office      INT(6) UNSIGNED NOT NULL,
        Validated   BOOLEAN NOT NULL,
        CONSTRAINT fkProfile FOREIGN KEY (Profile) REFERENCES Profile(Id),
        CONSTRAINT fkOffice FOREIGN KEY (Office) REFERENCES Office(Id)
) ENGINE=INNODB;

CREATE TABLE OfficeResponsable (
        UserId      INT(6) UNSIGNED,
        OfficeId    INT(6) UNSIGNED,
        CONSTRAINT fkUserId FOREIGN KEY (UserId) REFERENCES Users(Id),
        CONSTRAINT fkOfficeId FOREIGN KEY (OfficeId) REFERENCES Office(Id)
) ENGINE=INNODB;

CREATE TABLE OfficeSwitch (
        UserId      INT(6) UNSIGNED,
        OldOfficeId    INT(6) UNSIGNED,
        NewOfficeId    INT(6) UNSIGNED,
        CONSTRAINT fkUserIdOS FOREIGN KEY (UserId) REFERENCES Users(Id),
        CONSTRAINT fkOfficeIdOS FOREIGN KEY (OldOfficeId) REFERENCES Office(Id),
        CONSTRAINT fkNewOfficeIdOS FOREIGN KEY (NewOfficeId) REFERENCES Office(Id)
) ENGINE=INNODB;

CREATE TABLE OfficePatients (
        Id          INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        OfficeId        INT(6) UNSIGNED NOT NULL,
        Patient     INT(6) UNSIGNED NOT NULL,
        CONSTRAINT fkUser FOREIGN KEY (Id) REFERENCES Office(Id),
        CONSTRAINT fkPatient1 FOREIGN KEY (Patient) REFERENCES Patient(Id)
) ENGINE=INNODB;


# Liaisons

CREATE TABLE MedicationDuration (
        Id          INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        Med         INT(10) UNSIGNED NOT NULL,
        Patient     INT(6) UNSIGNED NOT NULL,
        Begin      DATE NOT NULL,
        End         DATE NOT NULL,
        CONSTRAINT fkMed2 FOREIGN KEY (Med) REFERENCES Medication(Id),
        CONSTRAINT fkPatient2 FOREIGN KEY (Patient) REFERENCES Patient(Id)
) ENGINE=INNODB;

CREATE TABLE INRTreatmentDuration (
        Id          INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        Treatment   INT(6) UNSIGNED NOT NULL,
        Phase       INT(1) UNSIGNED NOT NULL,
        Begin       DATE NOT NULL,
        End         DATE NOT NULL,
        CONSTRAINT fkTreatment1 FOREIGN KEY (Treatment) REFERENCES INRTreatment(Id),
        CONSTRAINT fkPhase2 FOREIGN KEY (Phase) REFERENCES INRTreatmentPhase(Id)
) ENGINE=INNODB;

INSERT INTO Medication(Name, INRImpact, Note) VALUES
("Allopurinol","1","Jusqu'a 8 jours après l'arrêt"),
("Amiodarone","1","Jusqu'à 8 jours après l’arrêt"),
("Androgenes","1","Jusqu'a 8 jours après l'arrêt"),
("ISRS : Citalopram","1",""),
("ISRS : Escitalopram","1",""),
("ISRS : Fluoxetine","1",""),
("ISRS : Fluvoxamine","1",""),
("ISRS : Sertraline","1",""),
("ISRS : Paroxetine","1",""),
("Benzbromaron","1",""),
("Aprepitants","-1",""),
("Aminogluthetimides","-1","Cancer du sein et de la prostate. Jusqu'a 2 semaines après l'arrêt"),
("Azathioprine","-1",""),
("Cefamandole","1",""),
("Cefoperazone","1",""),
("Cefotetan","1",""),
("Ceftriaxone","1",""),
("Cimetidine","1","Jusqu'a 8 jours après l'arrêt"),
("Cisapride","1","Jusqu'a 8 jours après l'arret"),
("Colchicine","1","Jusqu'a 8 jours après l'arret"),
("Cycline","1",""),
("Danazol","1",""),
("Econazole","1","Quelque soit le mode d'administration"),
("Fibrate","1","Jusqu'a 8 jours après l'arrêt"),
("Fluconazole","1","Jusqu'a 8 jours après l'arrêt"),
("Itraconazole","1","Jusqu'a 8 jours après l'arrêt"),
("Voriconazole","1","Jusqu'a 8 jours après l'arrêt"),
("Oflaxacine","1",""),
("Pefloxacine","1",""),
("Enoxacine","1",""),
("Lomefloxacine","1",""),
("Moxifloxacine","1",""),
("Ciprofloxacine","1",""),
("Levofloxacine","1",""),
("Norfloxacine","1",""),
("Glucocorticoïdes","1","Faire un contrôle biologique au 8e jour, puis tous les 15 jours pendant la corticothérapie et après son arrêt. Contrôle de l'INR 2 à 4 jours après le bolus de méthylprednisolone ou en présence de tous signes hémorragiques."),
("Methylprednisolone","1","Faire un contrôle biologique au 8e jour, puis tous les 15 jours pendant la corticothérapie et après son arrêt. Contrôle de l'INR 2 à 4 jours après le bolus de méthylprednisolone ou en présence de tous signes hémorragiques."),
("HBPM","1",""),
("Levotyhroxine","1",""),
("Liothyronine Sodique","1",""),
("Thyroxines","1",""),
("Tiratricol","1",""),
("Statines","1",""),
("Azithromycine","1",""),
("Clarithromycine","1",""),
("Dirithromycine","1",""),
("Erythromycine","1",""),
("Josamycine","1",""),
("Midecamycine","1",""),
("Roxithromycine","1",""),
("Telithromycine","1",""),
("Troleandomycine","1",""),
("Metronidazole","1",""),
("Ornidazole","1",""),
("Secnidazole","1",""),
("Tinidazole","1",""),
("Orlistat","1",""),
("Paracetamol","1","Aux doses maximales (4g/j) pendant au moins 4 jours."),
("Pentoxyfilline","1","Jusqu'a 8 jours après l'arrêt."),
("Proguanil","1",""),
("Propafenone","1",""),
("Sulfamethoxazole","1","Jusqu'a 8 jours après l'arrêt."),
("Sulfafurazole","1","Jusqu'a 8 jours après l'arrêt."),
("Sulfamethizol","1","Jusqu'a 8 jours après l'arrêt."),
("Tamoxifene","1",""),
("Tibolone","1",""),
("Tramadol","1",""),
("Viloxazine","1",""),
("Vitamine E","1",""),
("Anti agrégents plaquettaires","1",""),
("Alcoolisme aigü","1",""),
("Bosentan","-1",""),
("Coletstyramine","-1","Prendre la Coletstyramine à distance de la Coumadine (plus de 2h)"),
("Griseofulvine","-1","Jusqu'a 8 jours après l'arrêt"),
("Mercaptopurine","-1",""),
("Nevirapine","-1",""),
("Efavirenz","-1",""),
("Rifampicine","-1","Jusqu'a 8 jours après l'arrêt."),
("Ritonavir","-1",""),
("Sucralfate","-1","Prendre à distance de la Coumadine (plus de 2h)");

INSERT INTO Profile(Name) VALUES ("Administrateur"), ("Responsable de cabinet"), ("Medecin"), ("Infirmier"), ("Patient");
INSERT INTO Office(Name, Address, PhoneNumber, Validated) VALUES ("Aucun cabinet", "no-address", "no-phone", 1), ("Je créérai mon cabinet", "no-address", "no-phone", 1);
