package org.digijava.module.orgProfile.helper;

import java.util.Iterator;
import java.util.Set;

import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpOrganisation;

import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicatorCalcFormula;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AmpMath;
import org.digijava.module.aim.util.CurrencyUtil;

import org.digijava.module.aim.util.DbUtil;

/**
 *
 * @author medea
 */
public class ParisIndicatorHelper {

    private AmpAhsurveyIndicator prIndicator;// indicator
    private AmpOrganisation organization;
    private Long year;
    private AmpOrgGroup orgGroup;
    private String currency;
    private TeamMember member;

    public AmpOrgGroup getOrgGroup() {
        return orgGroup;
    }

    public void setOrgGroup(AmpOrgGroup orgGroup) {
        this.orgGroup = orgGroup;
    }

    public TeamMember getMember() {
        return member;
    }

    public void setMember(TeamMember member) {
        this.member = member;
    }

    public long getAllDonorBaseLineValue() {
        long allDonorBaseLineValue = 0;
        if (prIndicator.getIndicatorCode().equals("3")) {
            Double valueQ2 = DbUtil.getValue(new int[]{2, 1}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, 2005l, false,member);
            Double valueQ1 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, 2005l, false,member);
            if (valueQ1 != null&&valueQ1!=0) {
                allDonorBaseLineValue = Math.round(valueQ2 / valueQ1) * 100;
            }

        } else {
            if (prIndicator.getIndicatorCode().equals("4")) {
                Double valueQ3 = DbUtil.getValue(new int[]{3}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, 2005l, true,member);
                Double valueQ1 = DbUtil.getValue(new int[]{0}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, 2005l, true,member);
                if (valueQ1 != null&&valueQ3!=null&&valueQ1!=0) {
                    allDonorBaseLineValue = Math.round(valueQ3 / valueQ1)*  100;
                }
            } else {
                if (prIndicator.getIndicatorCode().equals("6")) {
                    allDonorBaseLineValue = DbUtil.getPIUValue(prIndicator.getAmpIndicatorId(), null,null, 2005l,member);
                } else {
                    if (prIndicator.getIndicatorCode().equals("5a")) {
                        Double valueQ15 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, 2005l, false,member);
                        Double valueQ67 = DbUtil.getValue(new int[]{1, 5, 6, 7}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, 2005l, false,member);
                        if (valueQ67 != null&&valueQ15!=null&&valueQ15!=0) {
                            allDonorBaseLineValue = Math.round(valueQ67 / valueQ15) * 100;
                        }
                    } else {
                        if (prIndicator.getIndicatorCode().equals("7")) {
                            Double valueQ1 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 1, currency, null, null,2005l, false,member);
                            Double valueQ2 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 0, currency, null, null,2005l, false,member);
                            if (valueQ2 != null && valueQ2 != 0) {
                                allDonorBaseLineValue = Math.round(valueQ1 / valueQ2) * 100;
                            }
                        } else {
                            if (prIndicator.getIndicatorCode().equals("9")) {
                                Double valueQ1 = DbUtil.getValue(new int[]{10}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, 2005l, false,member);
                                Double valueQ2 = DbUtil.getValue(new int[]{0}, prIndicator.getAmpIndicatorId(), 0, currency, null,null, 2005l, false,member);
                                if (valueQ2 != null && valueQ2 != 0) {
                                    allDonorBaseLineValue = Math.round(valueQ1 / valueQ2) * 100;
                                }
                            } else {
                                if (prIndicator.getIndicatorCode().equals("5b")) {
                                    Double valueQ15 = DbUtil.getValue(new int[]{8, 1}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, 2005l, false,member);
                                    Double valueQ67 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, 2005l, false,member);
                                    if (valueQ67 != null&&valueQ15!=null&&valueQ15!=0) {
                                        allDonorBaseLineValue = Math.round(valueQ67 / valueQ15) * 100;
                                    }
                                } else {
                                    if (prIndicator.getIndicatorCode().equals("5bii")) {
                                        allDonorBaseLineValue = DbUtil.getDonorsCount(new int[]{8, 1}, prIndicator.getAmpIndicatorId(), null,null, 2005l,member);
                                    } else {
                                        if (prIndicator.getIndicatorCode().equals("5aii")) {
                                            allDonorBaseLineValue = DbUtil.getDonorsCount(new int[]{1, 5, 6, 7}, prIndicator.getAmpIndicatorId(),null, null, 2005l,member);
                                        }
                                        else{
                                            if(prIndicator.getIndicatorCode().equals("10a")){
                                                allDonorBaseLineValue = DbUtil.getIndicator10aValue(2005l, null,null);
                                            }
                                        }

                                    }

                                }
                            }

                        }
                    }

                }

            }
        }
        return allDonorBaseLineValue;
    }

    public ParisIndicatorHelper(AmpAhsurveyIndicator prIndicator, FilterHelper helper) {
        this.prIndicator = prIndicator;
        this.organization = helper.getOrganization();
        this.year = helper.getYear();
        AmpCurrency curr = CurrencyUtil.getAmpcurrency(helper.getCurrId());
        this.currency = curr.getCurrencyCode();
        this.member=helper.getTeamMember();
        this.orgGroup=helper.getOrgGroup();


    }

    public long getAllCurrentValue() {
        long previousYearValue = 0;
        if (prIndicator.getIndicatorCode().equals("3")) {
            Double valueQ2 = DbUtil.getValue(new int[]{2, 1}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, year, false,member);
            Double valueQ1 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, year, false,member);
            if (valueQ1 != null&& valueQ1!=0) {
                previousYearValue = Math.round(valueQ2 / valueQ1) * 100;
            }

        } else {
            if (prIndicator.getIndicatorCode().equals("4")) {
                Double valueQ3 = DbUtil.getValue(new int[]{3}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, year, true,member);
                Double valueQ1 = DbUtil.getValue(new int[]{0}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, year, true,member);
                if (valueQ1 != null&& valueQ1!=0) {
                    previousYearValue = Math.round(valueQ3 / valueQ1) * 100;
                }
            } else {
                if (prIndicator.getIndicatorCode().equals("6")) {
                    previousYearValue = DbUtil.getPIUValue(prIndicator.getAmpIndicatorId(), null,null, year,member);
                } else {
                    if (prIndicator.getIndicatorCode().equals("5a")) {
                        Double valueQ15 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, year, false,member);
                        Double valueQ67 = DbUtil.getValue(new int[]{1, 5, 6, 7}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, year, false,member);
                        if (valueQ67 != null&&valueQ15!=null&&valueQ15!=0) {
                            previousYearValue = Math.round(valueQ67 / valueQ15) * 100;
                        }
                    } else {
                        if (prIndicator.getIndicatorCode().equals("7")) {
                            Double valueQ1 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, year, false,member);
                            Double valueQ2 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 0, currency, null,null, year, false,member);
                            if (valueQ2 != null && valueQ2 != 0) {
                                previousYearValue = Math.round(valueQ1 / valueQ2 )* 100;
                            }
                        } else {
                            if (prIndicator.getIndicatorCode().equals("9")) {
                                Double valueQ1 = DbUtil.getValue(new int[]{10}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, year, false,member);
                                Double valueQ2 = DbUtil.getValue(new int[]{0}, prIndicator.getAmpIndicatorId(), 0, currency, null,null, year, false,member);
                                if (valueQ2 != null && valueQ2 != 0) {
                                    previousYearValue = Math.round(valueQ1 / valueQ2 )* 100;
                                }
                            } else {
                                if (prIndicator.getIndicatorCode().equals("5b")) {
                                    Double valueQ15 = DbUtil.getValue(new int[]{8, 1}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, year, false,member);
                                    Double valueQ67 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 1, currency, null,null, year, false,member);
                                    if (valueQ67 != null&&valueQ15!=null&&valueQ15!=0) {
                                        previousYearValue = Math.round(valueQ67 / valueQ15) * 100;
                                    }
                                } else {
                                    if (prIndicator.getIndicatorCode().equals("5bii")) {
                                        previousYearValue = DbUtil.getDonorsCount(new int[]{8, 1}, prIndicator.getAmpIndicatorId(), null,null, year,member);
                                    } else {
                                        if (prIndicator.getIndicatorCode().equals("5aii")) {
                                            previousYearValue = DbUtil.getDonorsCount(new int[]{1, 5, 6, 7}, prIndicator.getAmpIndicatorId(), null,null, year,member);
                                        }
                                         else {
                                            if (prIndicator.getIndicatorCode().equals("10a")) {
                                                previousYearValue = DbUtil.getIndicator10aValue(year, null,null);
                                            }
                                        }

                                    }

                                }
                            }


                        }
                    }

                }

            }
        }
        return previousYearValue;

    }

    public long getAllTargetValue() {
        long targetValue = 0;
        AmpAhsurveyIndicatorCalcFormula formula = getFormula();
        if (formula != null) {
            if (formula.getEnabled() != null && formula.getEnabled()) {

                String form = getFormulaText(formula, getAllDonorBaseLineValue());

                targetValue = AmpMath.calcExp(form);

            } else {
                if (formula.getTargetValue() != null && !formula.getTargetValue().equals("")) {
                    targetValue = Long.parseLong(formula.getTargetValue());
                }
            }
        }
        return targetValue;
    }

    private String getFormulaText(AmpAhsurveyIndicatorCalcFormula formula, long constant) {
        String flText = null;
        if (formula != null) {
            flText = formula.getCalcFormula().replace(formula.getConstantName(), String.valueOf(constant));
        }
        return flText;
    }

    public long getOrgBaseLineValue() {
        long orgBaseLineValue = 0;
        if (prIndicator.getIndicatorCode().equals("3")) {
            Double valueQ2 = DbUtil.getValue(new int[]{2, 1}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), 2005l, false,member);
            Double valueQ1 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), 2005l, false,member);
            if (valueQ1 != null) {
                orgBaseLineValue = Math.round(valueQ2 / valueQ1) * 100;
            }


        } else {
            if (prIndicator.getIndicatorCode().equals("4")) {
                Double valueQ3 = DbUtil.getValue(new int[]{3}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), 2005l, true,member);
                Double valueQ1 = DbUtil.getValue(new int[]{0}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), 2005l, true,member);
                if (valueQ1 != null&&valueQ1!=0) {
                    orgBaseLineValue = Math.round(valueQ3 / valueQ1) * 100;
                }
            } else {
                if (prIndicator.getIndicatorCode().equals("6")) {
                    orgBaseLineValue = DbUtil.getPIUValue(prIndicator.getAmpIndicatorId(), organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), 2005l,member);
                } else {
                    if (prIndicator.getIndicatorCode().equals("5a")) {
                        Double valueQ15 = DbUtil.getValue(new int[]{1, 5}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), 2005l, false,member);
                        Double valueQ67 = DbUtil.getValue(new int[]{6, 7}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), 2005l, false,member);
                        if (valueQ15 != null&&valueQ15!=0) {
                            orgBaseLineValue = Math.round(valueQ67 / valueQ15) * 100;
                        }
                    } else {
                        if (prIndicator.getIndicatorCode().equals("7")) {
                            Double valueQ1 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), 2005l, false,member);
                            Double valueQ2 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 0, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), 2005l, false,member);
                            if (valueQ2 != null && valueQ2 != 0) {
                                orgBaseLineValue = Math.round(valueQ1 / valueQ2) * 100;
                            }
                        } else {
                            if (prIndicator.getIndicatorCode().equals("9")) {
                                Double valueQ1 = DbUtil.getValue(new int[]{10}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), 2005l, false,member);
                                Double valueQ2 = DbUtil.getValue(new int[]{0}, prIndicator.getAmpIndicatorId(), 0, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), 2005l, false,member);
                                if (valueQ2 != null && valueQ2 != 0) {
                                    orgBaseLineValue = Math.round(valueQ1 / valueQ2)  *100;
                                }
                            } else {
                                if (prIndicator.getIndicatorCode().equals("5b")) {
                                    Double valueQ15 = DbUtil.getValue(new int[]{8, 1}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), 2005l, false,member);
                                    Double valueQ67 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), 2005l, false,member);
                                    if (valueQ15 != null&&valueQ15!=0) {
                                        orgBaseLineValue = Math.round(valueQ67 / valueQ15) * 100;
                                    }
                                } else {
                                    if (prIndicator.getIndicatorCode().equals("5bii")) {
                                        orgBaseLineValue = DbUtil.getDonorsCount(new int[]{8, 1}, prIndicator.getAmpIndicatorId(), organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), 2005l,member);
                                    } else {
                                        if (prIndicator.getIndicatorCode().equals("5aii")) {
                                            orgBaseLineValue = DbUtil.getDonorsCount(new int[]{1, 5, 6, 7}, prIndicator.getAmpIndicatorId(), organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), 2005l,member);
                                        }
                                        else{
                                             if(prIndicator.getIndicatorCode().equals("10a")){
                                               orgBaseLineValue = DbUtil.getIndicator10aValue(2005l, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId());
                                            }
                                             
                                        }

                                    }
                                }

                            }

                        }
                    }


                }
            }
        }

        return orgBaseLineValue;
    }

    public long getOrgPreviousYearValue() {
        long previousYearValue = 0;
        if (prIndicator.getIndicatorCode().equals("3")) {
            Double valueQ2 = DbUtil.getValue(new int[]{2, 1}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), year, false,member);
            Double valueQ1 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), year, false,member);
            if (valueQ1 != null) {
                previousYearValue = Math.round(valueQ2 / valueQ1) * 100;
            }

        } else {
            if (prIndicator.getIndicatorCode().equals("4")) {
                Double valueQ3 = DbUtil.getValue(new int[]{3}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), year, true,member);
                Double valueQ1 = DbUtil.getValue(new int[]{0}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), year, true,member);
                if (valueQ1 != null) {
                    previousYearValue = Math.round(valueQ3 / valueQ1) * 100;
                }
            } else {
                if (prIndicator.getIndicatorCode().equals("6")) {

                    previousYearValue = DbUtil.getPIUValue(prIndicator.getAmpIndicatorId(), organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), year,member);
                } else {
                    if (prIndicator.getIndicatorCode().equals("5a")) {
                        Double valueQ15 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), year, false,member);
                        Double valueQ67 = DbUtil.getValue(new int[]{1, 5, 6, 7}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), year, false,member);
                        if (valueQ67 != null) {
                            previousYearValue = Math.round(valueQ67 / valueQ15) * 100;
                        }
                    } else {
                        if (prIndicator.getIndicatorCode().equals("7")) {
                            Double valueQ1 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), year, false,member);
                            Double valueQ2 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 0, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), year, false,member);
                            if (valueQ2 != null && valueQ2 != 0) {
                                previousYearValue = Math.round(valueQ1 / valueQ2) * 100;
                            }
                        } else {
                            if (prIndicator.getIndicatorCode().equals("9")) {
                                Double valueQ1 = DbUtil.getValue(new int[]{10}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), year, false,member);
                                Double valueQ2 = DbUtil.getValue(new int[]{0}, prIndicator.getAmpIndicatorId(), 0, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), year, false,member);
                                if (valueQ2 != null && valueQ2 != 0) {
                                    previousYearValue = Math.round(valueQ1 / valueQ2) * 100;
                                }
                            } else {
                                if (prIndicator.getIndicatorCode().equals("5b")) {
                                    Double valueQ15 = DbUtil.getValue(new int[]{8, 1}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), year, false,member);
                                    Double valueQ67 = DbUtil.getValue(new int[]{1}, prIndicator.getAmpIndicatorId(), 1, currency, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), year, false,member);
                                    if (valueQ67 != null) {
                                        previousYearValue = Math.round(valueQ67 / valueQ15 )* 100;
                                    }
                                } else {
                                    if (prIndicator.getIndicatorCode().equals("5bii")) {
                                        previousYearValue = DbUtil.getDonorsCount(new int[]{8, 1}, prIndicator.getAmpIndicatorId(), organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), year,member);
                                    } else {
                                        if (prIndicator.getIndicatorCode().equals("5aii")) {
                                            previousYearValue = DbUtil.getDonorsCount(new int[]{1, 5, 6,}, prIndicator.getAmpIndicatorId(), organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId(), year,member);
                                        }
                                       else {
                                            if (prIndicator.getIndicatorCode().equals("10a")) {
                                                previousYearValue = DbUtil.getIndicator10aValue(year, organization.getAmpOrgId(),orgGroup.getAmpOrgGrpId());
                                            }
                                        }
                                    }

                                }
                            }

                        }
                    }

                }

            }
        }
        return previousYearValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public AmpOrganisation getOrganization() {
        return organization;
    }

    public void setOrganization(AmpOrganisation organization) {
        this.organization = organization;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public AmpAhsurveyIndicator getPrIndicator() {
        return prIndicator;
    }

    public void setPrIndicator(AmpAhsurveyIndicator pi) {
        this.prIndicator = pi;
    }

    public AmpAhsurveyIndicatorCalcFormula getFormula() {
        AmpAhsurveyIndicatorCalcFormula retSurvey = null;
        Set<AmpAhsurveyIndicatorCalcFormula> calcFormulas = this.prIndicator.getCalcFormulas();
        if (calcFormulas != null && !calcFormulas.isEmpty()) {
            Iterator<AmpAhsurveyIndicatorCalcFormula> itr = calcFormulas.iterator();
            retSurvey = (AmpAhsurveyIndicatorCalcFormula) itr.next();
        }

        return retSurvey;
    }
    // all basevalue
    public String getBaseLineValue() {
        return getFormula().getBaseLineValue();
    }
    // all targetvalue 
    public String getTargetValue() {
        return getFormula().getTargetValue();
    }
}
