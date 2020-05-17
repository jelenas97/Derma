package model;

import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.cbrcore.CaseComponent;

public class CaseDescription implements CaseComponent {


	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id",this.getClass());
	}
}
