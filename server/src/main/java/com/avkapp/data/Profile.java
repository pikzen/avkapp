package com.avkapp.data;
import org.codehaus.jackson.annotate.JsonProperty;
import java.util.HashMap;

public class Profile {
	@JsonProperty("id")
	private int Id;

	@JsonProperty("name")
	private String Name;

	public static int ROLE_ADMIN = 1;
	public static int ROLE_RESPONSABLE = 2;
	public static int ROLE_MEDECIN = 3;
	public static int ROLE_INFIRMIER = 4;
	public static int ROLE_AUTOMED = 5;

	// Liste des permissions (bitflags)
	public static final int PERM_LISTALLUSERS 				= 1;
	public static final int PERM_LISTSOMEUSERS				= 2;
	public static final int PERM_VALIDATEALLUSERS			= 4;
	public static final int PERM_VALIDATESOMEUSERS			= 8;
	public static final int PERM_CREATEOFFICE				= 16;
	public static final int PERM_VALIDATEALLOFFICE 			= 32;
	public static final int PERM_VALIDATEALLOFFICEJOIN 		= 64;
	public static final int PERM_VALIDATESOMEOFFICEJOIN 	= 128;
	public static final int PERM_ADDRECORD					= 256;
	public static final int PERM_CREATEPATIENT				= 512;
	public static final int PERM_ADDMEDICATION				= 1024;
	public static final int PERM_MODIFYPATIENT				= 2048;
	public static final int PERM_BECOMEOFFICERESP			= 4096;
	public static final int PERM_VALIDATEBECOMEOFFICERESP  	= 8192;
	public static final int PERM_LISTOFFICEPATIENT			= 16384;
	public static final int PERM_LISTSELFPATIENT			= 32768;
	public static final int PERM_ADDPATIENTMED				= 65536;

	public static HashMap<Integer, Integer> ROLES  = new HashMap<Integer, Integer>();
	// Initialisation des valeurs
	static {
		ROLES.put(ROLE_ADMIN, PERM_LISTALLUSERS | 
							  PERM_VALIDATEALLUSERS | 
							  PERM_CREATEOFFICE | 
							  PERM_VALIDATEALLOFFICE | 
							  PERM_VALIDATEALLOFFICEJOIN | 
							  PERM_VALIDATEBECOMEOFFICERESP |
							  PERM_ADDMEDICATION);

		ROLES.put(ROLE_RESPONSABLE, PERM_LISTSOMEUSERS | 
			                        PERM_VALIDATESOMEUSERS | 
			                        PERM_VALIDATESOMEOFFICEJOIN | 
			                        PERM_CREATEPATIENT | 
			                        PERM_MODIFYPATIENT | 
			                        PERM_BECOMEOFFICERESP | 
			                        PERM_LISTOFFICEPATIENT | 
			                        PERM_LISTSELFPATIENT);

		ROLES.put(ROLE_MEDECIN, PERM_ADDRECORD |
								PERM_MODIFYPATIENT |
								PERM_LISTOFFICEPATIENT |
								PERM_LISTSELFPATIENT |
								PERM_ADDPATIENTMED);

		ROLES.put(ROLE_INFIRMIER, PERM_ADDRECORD | 
								  PERM_LISTSELFPATIENT | 
								  PERM_LISTOFFICEPATIENT);

		ROLES.put(ROLE_AUTOMED, PERM_ADDRECORD | 
								PERM_LISTSELFPATIENT);
	}

	public int getId() {
		return this.Id;
	}

	public String getName() {
		return this.Name;
	}

	public Profile(int i, String n) {
		this.Id = i;
		this.Name = n;
	}

	@Override
	public String toString() {
		return "Profile[" + 
						"id=" + Id + "," + 
				        "name=" + Name +
				     "]";
	}

}