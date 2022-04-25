package hu.webuni.hr.dodi.model.enums;

public enum CompanyType {
	
	BT("BT"),
	KFT("KFT"),
	ZRT("ZRT"),
	NYRT("NYRT");

	private final String type;

	private CompanyType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return type;
	}
		
}
