class Donor < ActiveRecord::Base
  extend Translator
  translates :name
  
  has_many :users
    
  has_many :projects
  has_many :country_strategies, :dependent => :delete_all
  has_many :agencies, :class_name => "DonorAgency", :dependent => :delete_all
  
  has_many :cofundings
  has_many :cofinanced_projects, :through => :cofundings, :source => :project
  has_many :consistency_finances, :class_name => "ConsistencyFinances"
  
  
  ##
  # Validation
  validates_presence_of :name

  
  # List of main donors (not cofunding only!)
  named_scope :main, :conditions => "cofunding_only IS DISTINCT FROM true"
  named_scope :ordered, :order => "name ASC"
  
  
  
  
  
  # Total commitments of donor
  def total_commitments
    annual_commitments.values.sum
  end
  
  # Fetches the donor's commitments per year from the database
  # They are returned as Currency objects in the donor's currency and 
  # can be accessed as an array (e.g. Donor.annual_commitments[2007])
  # Forecasts are *not* included!
  def annual_commitments
    # Use lazy loading to minimize database queries
    @annual_commitments ||= Funding.find(:all, 
      :select=>'SUM(fundings.commitments) AS total_commitments, fundings.year as year',
      :joins => 'JOIN projects ON fundings.project_id = projects.id',
      :conditions => ['projects.donor_id = ? AND projects.data_status = ?', self.id, Project::PUBLISHED],
      :group => 'fundings.year'
    ).inject({}) {|totals, rec| totals[rec.year] = rec.total_commitments.to_currency(currency, rec.year); totals} 
  end
  
  # Fetches the donor's commitment forecasts per year from the database
  # They are returned as Currency objects in the donor's currency and 
  # can be accessed as an array (e.g. Donor.annual_commitments_forecasts[2011])
  def annual_commitments_forecasts
    # Use lazy loading to minimize database queries
    @annual_commitments_forecasts ||= FundingForecast.find(:all, 
      :select=>'SUM(funding_forecasts.commitments) AS forecast, funding_forecasts.year as year',
      :joins => 'JOIN projects ON funding_forecasts.project_id = projects.id',
      :conditions => ['projects.donor_id = ? AND projects.data_status = ?', self.id, Project::PUBLISHED],
      :group => 'funding_forecasts.year'
    ).inject({}) {|totals, rec| totals[rec.year] = rec.forecast.to_currency(currency, rec.year); totals} 
  end
  
  
  
  # Total payments of donor
  def total_payments
    annual_payments.values.sum
  end
  
  # Fetches the donor's payments per year from the database
  # They are returned as Currency objects in the donor's currency and 
  # can be accessed as an array (e.g. Donor.annual_payments[2007])
  # Forecasts are *not* included!
  def annual_payments
    # Use lazy loading to minimize database queries
    @annual_payments ||= Funding.find(:all, 
      :select=>'SUM(fundings.payments_q1 + fundings.payments_q2 + fundings.payments_q3 + fundings.payments_q4) AS pay, fundings.year as year',
      :joins => 'JOIN projects ON fundings.project_id = projects.id',
      :conditions => ['projects.donor_id = ? AND projects.data_status = ?', self.id, Project::PUBLISHED],
      :group => 'fundings.year'
    ).inject({}) {|totals, rec| totals[rec.year] = rec.pay.to_currency(currency, rec.year); totals} 
  end
  
  # Fetches the donor's payment forecasts per year from the database
  # They are returned as Currency objects in the donor's currency and 
  # can be accessed as an array (e.g. Donor.annual_payments_forecasts[2011])
  def annual_payments_forecasts
    # Use lazy loading to minimize database queries
    @annual_payments_forecasts ||= FundingForecast.find(:all, :select=>'SUM(funding_forecasts.payments) AS forecast, funding_forecasts.year as year',
      :joins => 'JOIN projects ON funding_forecasts.project_id = projects.id',
      :conditions => ['projects.donor_id = ? AND projects.data_status = ?', self.id, Project::PUBLISHED],
      :group => 'funding_forecasts.year'
    ).inject({}) {|totals, rec| totals[rec.year] = rec.forecast.to_currency(currency, rec.year); totals} 
  end

  def payments_by_type_of_aid(year = Time.now.year)
    # Use lazy loading to minimize database queries
    @annual_payments_by_toa ||= Funding.find(:all, 
      :select=>'SUM(fundings.payments_q1 + fundings.payments_q2 + fundings.payments_q3 + fundings.payments_q4) AS pay, fundings.year as year, projects.type_of_aid AS type_of_aid',
      :joins => 'JOIN projects ON fundings.project_id = projects.id',
      :conditions => ['projects.donor_id = ? AND projects.data_status = ? AND fundings.year = ?', self.id, Project::PUBLISHED, year],
      :group => 'projects.type_of_aid, fundings.year'
    ).inject([]) {|totals, rec| totals[rec.type_of_aid.to_i] = rec.pay.to_currency(currency, rec.year); totals} 
  end
  
  def payments_forecasts_by_type_of_aid(year = Time.now.year)
    # Use lazy loading to minimize database queries
    @annual_payments_forecasts_by_toa ||= Finances.find(:all, :select=>'SUM(finances.payments_forecast) AS payments, finances.year as year, projects.type_of_aid AS type_of_aid',
      :joins => 'JOIN projects ON finances.project_id = projects.id',
      :conditions => ['projects.donor_id = ? AND projects.data_status = ? AND finances.year = ?', self.id, Project::PUBLISHED, year],
      :group => 'projects.type_of_aid, finances.year'
    ).inject([]) {|totals, rec| totals[rec.type_of_aid.to_i] = rec.payments.to_currency(currency, rec.year); totals} 
  end
  
  # Returns total loan payments for a given year (or all if no argument given)
  def total_loan_payments(year = nil)
    # TODO: Speed up!
    query_projects = projects.published.loan
    query_projects.inject(0.to_currency(currency)) { |sum, p| sum + p.funding.find_by_year(year).andand.payments.to_currency }
  end 
  
  # Returns total loan forecasts for a given year (or all if no argument given)
  def total_loan_forecasts(year = nil)
    query_projects = projects.published.loan
    query_projects.inject(0.to_currency(currency)) { |sum, p| sum + p.funding_forecasts.find_by_year(year).andand.payments.to_currency }
  end 
  
  # Returns total grant payments for a given year (or all if no argument given)
  def total_grant_payments(year = nil)
    query_projects = projects.published.grant
    query_projects.inject(0.to_currency(currency)) { |sum, p| sum + p.fundings.find_by_year(year).andand.payments.to_currency }
  end
  
  # Returns total grant forecasts for a given year (or all if no argument given)
  def total_grant_forecasts(year = nil)
    query_projects = projects.published.loan
    query_projects.inject(0.to_currency(currency)) { |sum, p| sum + p.funding_forecasts.find_by_year(year).andand.payments.to_currency }
  end



  #  Find provinces, ordered by donor's payments to each
  #  Returns: [[province, amount], ..]
  def provinces_by_amount(year = nil)  
    conditions = if year
      ["year = ? AND data_status = ? AND donor_id = ? AND pay_per_region > 0", year, Project::PUBLISHED, self.id]
    else
      ["data_status = ? AND donor_id = ? AND pay_per_region > 0", Project::PUBLISHED, self.id]
    end
        
    res = Finances.find(:all,
      :select => "SUM(pay_per_region) AS total, geo_level1s_name, year",
      :from => "project_payment_by_regions",
      :conditions => conditions,
      :group => "geo_level1s_name, year")
      
    
    res.inject(OrderedHash.new) do |province_amounts, r|
      province_amounts[r.geo_level1s_name] = r.total.to_currency(self.currency, r.year)
      province_amounts
    end
  end
  
  # List of total payments per sector
  def total_payments_in_sectors(year = nil)
    conditions = if year
      ["finances.year = ? AND projects.data_status = ? AND projects.donor_id = ? AND dac_sectors.name IS NOT NULL", year, Project::PUBLISHED, self.id]
    else
      ["projects.data_status = ? AND projects.donor_id = ? AND dac_sectors.name IS NOT NULL", Project::PUBLISHED, self.id]
    end
    
    res = Finances.find(:all, 
      :select => "SUM(payments_q1 + payments_q2 + payments_q3 + payments_q4) AS payments, year, dac_sectors.code AS sector_code, dac_sectors.name as sector_name",
      :joins => "LEFT OUTER JOIN projects ON finances.project_id = projects.id LEFT OUTER JOIN crs_sectors ON projects.crs_sector_id = crs_sectors.id LEFT OUTER JOIN dac_sectors ON crs_sectors.dac_sector_id = dac_sectors.id",
      :conditions => conditions,
      :group => "crs_sectors.dac_sector_id, dac_sectors.code, dac_sectors.name, finances.year",
      :order => "sector_name ASC")
          
    res.inject(OrderedHash.new) do |sector_amounts, r|
      # Merge
      # if [111, 112, 113, 114].include?(r.sector_code.to_i)
      #         sector = DacSector.new(:code => 110, :name => "EDUCATION", :name_es => "EDUCACION") # 11000 EDUCATION
      #       elsif [121, 122].include?(r.sector_code.to_i)
      #         sector = DacSector.new(:code => 120, :name => "HEALTH", :name_es => "SALUD") # 12000 HEALTH
      #       else
        sector = DacSector.find_by_code(r.sector_code)
      # end
      
      sector_amounts[sector] ||= 0.to_currency(self.currency) 
      sector_amounts[sector] += r.payments.to_currency(self.currency, r.year)
      
      sector_amounts
    end
  end
  
  # Returns total payments to national projects (no provinces/regions chosen)
  def payments_to_national_projects(year = nil)
    conditions = if year
      ["finances.year = ? AND projects.data_status = ? AND projects.donor_id = ? AND provinces.name IS NULL", year, Project::PUBLISHED, self.id]
    else
      ["projects.data_status = ? AND projects.donor_id = ? AND provinces.name IS NULL", Project::PUBLISHED, self.id]
    end
    
    res = Funding.find(:first, 
      :select => "SUM(payments_q1 + payments_q2 + payments_q3 + payments_q4) AS pay",
      :joins => "LEFT OUTER JOIN projects ON finances.project_id = projects.id LEFT OUTER JOIN geo_relevances ON geo_relevances.project_id = projects.id LEFT OUTER JOIN provinces ON geo_relevances.province_id = provinces.id",
      :conditions => conditions)
                
    res.pay.to_currency(self.currency)
  end
end
