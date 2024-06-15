package org.research.kadda.nuclide;

public enum TracerTypeEnum {

	PARENT("mother",1),
	DAUGHTER("daughter",2),
	INVIVO("in-vivo",3),
	EXTERNAL("external",4);

	private final String tracerTypeName;
	private final int tracerTypeId;

	private TracerTypeEnum(String tracerTypeName, int tracerTypeId) {
		this.tracerTypeName = tracerTypeName;
		this.tracerTypeId = tracerTypeId;
	}
	
	public String getTracerTypeName() {
		return tracerTypeName;
	}
	
	public int getTracerTypeId() {
		return tracerTypeId;
	}

	@Override
	public String toString() {
		return tracerTypeName;
	}
}