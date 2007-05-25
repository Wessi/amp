package org.digijava.module.aim.form ;

import java.util.Collection ;
import org.digijava.module.aim.dbentity.AmpSector ;

public class ChannelOverviewObjectiveForm extends MainProjectDetailsForm
{
	private String name ;
	private String objective ;
	private String perspective ;
	private boolean validLogin;

	public String getName() 
	{
		return name;
	}

	public String getObjective() 
	{
		return objective;
	}

	public boolean getValidLogin() 
	{
		return validLogin;
	}

	public String getPerspective() 
	{
		return perspective;
	}

	public void setObjective(String objective) 
	{
		this.objective = objective ;
	}

	public void setName(String name) 
	{
		this.name = name ;
	}

	public void setValidLogin(boolean bool) 
	{
		this.validLogin = bool ;
	}

	public void setPerspective(String perspective) 
	{
		this.perspective = perspective ;
	}
}
