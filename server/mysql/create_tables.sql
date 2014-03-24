CREATE TABLE Office (
        Id          INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        Name        VARCHAR(40) NOT NULL,
        Address     VARCHAR(200) NOT NULL,
        PhoneNumber VARCHAR(20) NOT NULL
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
        OfficeId    INT(6) UNSIGNED,
        CONSTRAINT fkUserIdOS FOREIGN KEY (UserId) REFERENCES Users(Id),
        CONSTRAINT fkOfficeIdOS FOREIGN KEY (OfficeId) REFERENCES Office(Id)
) ENGINE=INNODB;

CREATE TABLE OfficePatients (
        Id          INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        OfficeId        INT(6) UNSIGNED NOT NULL,
        Patient     INT(6) UNSIGNED NOT NULL,
        CONSTRAINT fkUser FOREIGN KEY (UserId) REFERENCES Office(Id),
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



