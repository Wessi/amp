package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.IPAContractDisbursement;

/**
 * @author mihai
 *
 */
public class IPAContractForm extends ActionForm  {
	
	private List<IPAContractDisbursement> contractDisbursements;
	private Collection<AmpCategoryValue> activitiyCategories;
        private Collection<AmpCategoryValue> statuses;
        private Collection<AmpCategoryValue> types;
        private Long statusId;
        private Long typeId;

        public Long getStatusId() {
            return statusId;
        }

        public void setStatusId(Long statusId) {
            this.statusId = statusId;
        }

        public Collection<AmpCategoryValue> getStatuses() {
            return statuses;
        }

        public void setStatuses(Collection<AmpCategoryValue> statuses) {
            this.statuses = statuses;
        }
	
	public Collection<AmpCategoryValue> getActivitiyCategories() {
		return activitiyCategories;
	}

	public void setActivitiyCategories(Collection<AmpCategoryValue> activitiyCategories) {
		this.activitiyCategories = activitiyCategories;
	}

	 public IPAContractDisbursement getContractDisbursement(int index) {
		int currentSize = contractDisbursements.size();
		if (index >= currentSize) {
			for (int i = 0; i <= index - currentSize; i++) {
				contractDisbursements.add(new IPAContractDisbursement());
			}
		}
		return contractDisbursements.get(index);
	}
         


	
	private List currencies;
	private Long activityCategoryId;
	
	
	private Long id;
	private String contractName;
	private String description;
	
	
	private String startOfTendering;
	
	private String signatureOfContract;
	private String contractValidity;
	private String contractCompletion;
	
	private String totalECContribIBAmount;
	private Long totalECContribIBCurrency;
	
	private String totalAmount;
	private Long totalAmountCurrency;
	private Long dibusrsementsGlobalCurrency;
	
	private String totalECContribINVAmount;
	private Long totalECContribINVCurrency;
	
	private String totalNationalContribCentralAmount;
	private Long totalNationalContribCentralCurrency;
	
	private String totalNationalContribRegionalAmount;
	private Long totalNationalContribRegionalCurrency;
	
	private String totalNationalContribIFIAmount;
	private Long totalNationalContribIFICurrency;
	
	private String totalPrivateContribAmount;
	private Long totalPrivateContribCurrency;
	private Double totalDisbursements;
    private Double executionRate;
	
	private List disbursements;
        private List<AmpOrganisation> organisations;
        private Long contrOrg;
        private Integer indexId;
        private Long selContractDisbursements[];

        public Long[] getSelContractDisbursements() {
            return selContractDisbursements;
        }

        public void setSelContractDisbursements(Long[] selContractDisbursements) {
            this.selContractDisbursements = selContractDisbursements;
        }
        

        public Integer  getIndexId() {
            return indexId;
        }

        public void setIndexId(Integer indexId) {
            this.indexId = indexId;
        }
        
       

        public Long getContrOrg() {
            return contrOrg;
        }

        public void setContrOrg(Long contrOrg) {
            this.contrOrg = contrOrg;
        }

        public List getOrganisations() {
            return organisations;
        }

        public void setOrganisations(List organisations) {
            this.organisations = organisations;
        }

	public String getContractCompletion() {
		return contractCompletion;
	}

	public void setContractCompletion(String contractCompletion) {
		this.contractCompletion = contractCompletion;
	}

	public List<IPAContractDisbursement> getContractDisbursements() {
		return contractDisbursements;
	}

	public void setContractDisbursements(
			List<IPAContractDisbursement> contractDisbursements) {
		this.contractDisbursements = contractDisbursements;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public List getCurrencies() {
		return currencies;
	}

	public void setCurrencies(List currencies) {
		this.currencies = currencies;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List getDisbursements() {
		return disbursements;
	}

	public void setDisbursements(List disbursements) {
		this.disbursements = disbursements;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSignatureOfContract() {
		return signatureOfContract;
	}

	public void setSignatureOfContract(String signatureOfContract) {
		this.signatureOfContract = signatureOfContract;
	}

	public String getStartOfTendering() {
		return startOfTendering;
	}

	public void setStartOfTendering(String startOfTendering) {
		this.startOfTendering = startOfTendering;
	}

	public String getTotalECContribIBAmount() {
		return totalECContribIBAmount;
	}

	public void setTotalECContribIBAmount(String totalECContribIBAmount) {
		this.totalECContribIBAmount = totalECContribIBAmount;
	}

	public Long getTotalECContribIBCurrency() {
		return totalECContribIBCurrency;
	}

	public void setTotalECContribIBCurrency(Long totalECContribIBCurrency) {
		this.totalECContribIBCurrency = totalECContribIBCurrency;
	}

	public String getTotalECContribINVAmount() {
		return totalECContribINVAmount;
	}

	public void setTotalECContribINVAmount(String totalECContribINVAmount) {
		this.totalECContribINVAmount = totalECContribINVAmount;
	}

	public Long getTotalECContribINVCurrency() {
		return totalECContribINVCurrency;
	}

	public void setTotalECContribINVCurrency(Long totalECContribINVCurrency) {
		this.totalECContribINVCurrency = totalECContribINVCurrency;
	}

	public String getTotalNationalContribCentralAmount() {
		return totalNationalContribCentralAmount;
	}

	public void setTotalNationalContribCentralAmount(
			String totalNationalContribCentralAmount) {
		this.totalNationalContribCentralAmount = totalNationalContribCentralAmount;
	}

	public Long getTotalNationalContribCentralCurrency() {
		return totalNationalContribCentralCurrency;
	}

	public void setTotalNationalContribCentralCurrency(
			Long totalNationalContribCentralCurrency) {
		this.totalNationalContribCentralCurrency = totalNationalContribCentralCurrency;
	}

	public String getTotalNationalContribIFIAmount() {
		return totalNationalContribIFIAmount;
	}

	public void setTotalNationalContribIFIAmount(
			String totalNationalContribIFIAmount) {
		this.totalNationalContribIFIAmount = totalNationalContribIFIAmount;
	}

	public Long getTotalNationalContribIFICurrency() {
		return totalNationalContribIFICurrency;
	}

	public void setTotalNationalContribIFICurrency(
			Long totalNationalContribIFICurrency) {
		this.totalNationalContribIFICurrency = totalNationalContribIFICurrency;
	}

	public String getTotalNationalContribRegionalAmount() {
		return totalNationalContribRegionalAmount;
	}

	public void setTotalNationalContribRegionalAmount(
			String totalNationalContribRegionalAmount) {
		this.totalNationalContribRegionalAmount = totalNationalContribRegionalAmount;
	}

	public Long getTotalNationalContribRegionalCurrency() {
		return totalNationalContribRegionalCurrency;
	}

	public void setTotalNationalContribRegionalCurrency(
			Long totalNationalContribRegionalCurrency) {
		this.totalNationalContribRegionalCurrency = totalNationalContribRegionalCurrency;
	}

	public String getTotalPrivateContribAmount() {
		return totalPrivateContribAmount;
	}

	public void setTotalPrivateContribAmount(String totalPrivateContribAmount) {
		this.totalPrivateContribAmount = totalPrivateContribAmount;
	}

	public Long getTotalPrivateContribCurrency() {
		return totalPrivateContribCurrency;
	}

	public void setTotalPrivateContribCurrency(
			Long totalPrivateContribCurrency) {
		this.totalPrivateContribCurrency = totalPrivateContribCurrency;
	}

	public Long getActivityCategoryId() {
		return activityCategoryId;
	}

	public void setActivityCategoryId(Long activityCategoryId) {
		this.activityCategoryId = activityCategoryId;
	}
        
        public Collection<AmpCategoryValue> getTypes() {
            return types;
        }

        public void setTypes(Collection<AmpCategoryValue> types) {
            this.types = types;
        }
        
        public Long getTypeId() {
            return typeId;
        }

        public void setTypeId(Long typeId) {
            this.typeId = typeId;
        }

        
        public void reset(ActionMapping mapping, HttpServletRequest request) {
            if (request.getParameter("new")!=null){
            contractDisbursements=null;
	    activityCategoryId = null;
            contractName = null;
            description = null;


            startOfTendering = null;

            signatureOfContract = null;
            contractCompletion = null;

            totalECContribIBAmount = null;
            totalECContribIBCurrency = null;
            
            totalAmount = null;
            totalAmountCurrency = null;
            dibusrsementsGlobalCurrency=null;
            executionRate=null;

            totalECContribINVAmount = null;
            totalECContribINVCurrency = null;

            totalNationalContribCentralAmount = null;
            totalNationalContribCentralCurrency = null;

            totalNationalContribRegionalAmount = null;
            totalNationalContribRegionalCurrency = null;

            totalNationalContribIFIAmount = null;
            totalNationalContribIFICurrency = null;

            totalPrivateContribAmount = null;
            totalPrivateContribCurrency = null;
            indexId=null;
            id=null;
            statusId=null;
            activityCategoryId=null;
            selContractDisbursements=null;
            contrOrg=null;
            types=null;
            typeId=null;
            }
	
            
        }

		public String getContractValidity() {
			return contractValidity;
		}

		public void setContractValidity(String contractValidity) {
			this.contractValidity = contractValidity;
		}

		public String getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(String totalAmount) {
			this.totalAmount = totalAmount;
		}

		public Long getTotalAmountCurrency() {
			return totalAmountCurrency;
		}

		public void setTotalAmountCurrency(Long totalAmountCurrency) {
			this.totalAmountCurrency = totalAmountCurrency;
		}

		public Long getDibusrsementsGlobalCurrency() {
			return dibusrsementsGlobalCurrency;
		}

		public void setDibusrsementsGlobalCurrency(Long dibusrsementsGlobalCurrency) {
			this.dibusrsementsGlobalCurrency = dibusrsementsGlobalCurrency;
		}

		public Double getTotalDisbursements() {
			return totalDisbursements;
		}

		public void setTotalDisbursements(Double totalDisbursements) {
			this.totalDisbursements = totalDisbursements;
		}

		public Double getExecutionRate() {
			return executionRate;
		}

		public void setExecutionRate(Double executionRate) {
			this.executionRate = executionRate;
		}


}
