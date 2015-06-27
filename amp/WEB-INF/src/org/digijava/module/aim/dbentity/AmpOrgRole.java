package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.util.Output;


public class AmpOrgRole implements Comparable<AmpOrgRole>, Serializable, Versionable, Cloneable
{
	@Interchangeable(fieldTitle="AMP Organization Role ID")
    private Long ampOrgRoleId;
	@Interchangeable(fieldTitle="Activity", pickIdOnly = true)
    private AmpActivityVersion activity;
	@Interchangeable(fieldTitle="Organization")
	private AmpOrganisation organisation;
	@Interchangeable(fieldTitle="Role", descend=true)
	private AmpRole role;
	@Interchangeable(fieldTitle="Percentage")
	private Float 	percentage;
	@Interchangeable(fieldTitle="Budgets")
	private Set <AmpOrgRoleBudget> budgets;
	@Interchangeable(fieldTitle="Additional Info")
	private String additionalInfo;
	
	
    public Float getPercentage() {
		return percentage;
	}
	public void setPercentage(Float percentage) {
		this.percentage = percentage;
	}
	/**
     * @return Returns the activity.
     */
    public AmpActivityVersion getActivity() {
        return activity;
    }
    /**
     * @param activity The activity to set.
     */
    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }
    /**
     * @return Returns the ampOrgRoleId.
     */
    public Long getAmpOrgRoleId() {
        return ampOrgRoleId;
    }
    /**
     * @param ampOrgRoleId The ampOrgRoleId to set.
     */
    public void setAmpOrgRoleId(Long ampOrgRoleId) {
        this.ampOrgRoleId = ampOrgRoleId;
    }
    /**
     * @return Returns the organisation.
     */
    public AmpOrganisation getOrganisation() {
        return organisation;
    }
    /**
     * @param organisation The organisation to set.
     */
    public void setOrganisation(AmpOrganisation organisation) {
        this.organisation = organisation;
    }
    /**
     * @return Returns the role.
     */
    public AmpRole getRole() {
        return role;
    }
    /**
     * @param role The role to set.
     */
    public void setRole(AmpRole role) {
        this.role = role;
    }
    
        
	/**
	 * @return the additionalInfo
	 */
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	/**
	 * @param additionalInfo the additionalInfo to set
	 */
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpOrgRole aux = (AmpOrgRole) obj;
		String original = "" + this.organisation.getAmpOrgId() + "-" + this.role.getAmpRoleId();
		String copy = "" + aux.organisation.getAmpOrgId() + "-" + aux.role.getAmpRoleId();
		if (original.equals(copy)) {
			return true;
		}
		return false;
	}

	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		out.getOutputs().add(
				new Output(null, new String[] { "Organization" }, new Object[] { this.organisation.getName() }));
		out.getOutputs().add(new Output(null, new String[] { "Role" }, new Object[] { TranslatorWorker.translateText(this.role.getName()) }));
		if (this.percentage != null) {
			out.getOutputs().add(new Output(null, new String[] { "Percentage" }, new Object[] { this.percentage }));
		}
		if (this.additionalInfo != null && this.additionalInfo.trim().length() > 0)
			out.getOutputs().add(new Output(null, new String[] {"Department/Division"}, new Object[] {this.additionalInfo}));
		if (this.budgets != null){
			StringBuffer budgetCode = new StringBuffer();
			for (AmpOrgRoleBudget budget :budgets) {
			budgetCode.append(budget.getBudgetCode()+ ",");	
			}
			if (budgetCode.length()>0) {
				out.getOutputs().add(new Output(null, new String[] {"Budget Code"}, new Object[] {budgetCode.substring(0,budgetCode.length()-1)}));
			}
		}
		return out;
	}

	@Override
	public Object getValue() {
		StringBuffer budgetCode = new StringBuffer();
		for (AmpOrgRoleBudget budget :budgets) {
		budgetCode.append(budget.getBudgetCode()+ ",");	
		}
		return "" + this.percentage + "" + this.additionalInfo + ""+budgetCode.toString();
	}
	
	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
		AmpOrgRole aux = (AmpOrgRole) clone();
		aux.activity = newActivity;
		aux.ampOrgRoleId = null;
		return aux;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	@Override
	public int compareTo(AmpOrgRole arg0) {
		if(this.getAmpOrgRoleId()!=null)
		return this.getAmpOrgRoleId().compareTo(arg0.getAmpOrgRoleId());
		else return -1;
	}
	
	public final static Comparator<AmpOrgRole> BY_ACRONYM_AND_NAME_COMPARATOR = new Comparator<AmpOrgRole>() {
		@Override
		public int compare(AmpOrgRole o1, AmpOrgRole o2) {
			if (o1 == null || o1.getOrganisation() == null ||o1.getOrganisation().getAcronymAndName() == null)
				return 1;
			if (o2 == null || o2.getOrganisation() == null ||o2.getOrganisation().getAcronymAndName() == null)
				return -1;
			return o1.getOrganisation().getAcronymAndName().compareTo(o2.getOrganisation().getAcronymAndName());
		}
	};


	public Set<AmpOrgRoleBudget> getBudgets() {
		return budgets;
	}
	public void setBudgets(Set<AmpOrgRoleBudget> budgets) {
		this.budgets = budgets;
	}
}	
