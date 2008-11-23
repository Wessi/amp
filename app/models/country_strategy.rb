class CountryStrategy < ActiveRecord::Base
  translates :description, :strategy_number
  
  belongs_to  :donor
  has_many    :projects  
  
  has_many    :sector_details, :dependent => :delete_all
  has_many    :total_odas, :attributes => true
  
  
  RESPONSIBILITY_OPTIONS = [["Not Available", 0], ["Field Office", 1], ["Head Quarters", 2], 
    ["Field Office / Head Quarters", 3], ["Head Quarters / Field Office", 4]]
  
  
  def focal_provinces
    sector_details.map { |s| s.projects }.flatten.uniq
  end
  
  def focal_sectors
    sector_details.map { |s| s.focal_sector }.flatten.uniq
  end

  def sector_list
    sectors = Hash.new
    focal_sectors.each do |sector|
      if sector.is_a?(DacSector)
        sectors[sector] ||= []
      elsif sector.is_a?(CrsSector)
        (sectors[sector.dac_sector] ||= []) << sector
      end
    end        
    sectors
  end
  
  def current_total_oda
    total_odas.find_or_initialize_by_year(Time.now.year-1)
  end
  
  def last_total_oda
    total_odas.find_or_initialize_by_year(Time.now.year-2)
  end

  
  def sector_detail_attributes=(attributes)
    sector_details.destroy_all
   
    attributes.values.each do |attribute|
      sector_detail = sector_details.build(attribute)
      if !attribute[:crs_sector_id].blank?
        sector_detail.focal_sector = CrsSector.find(attribute[:crs_sector_id])
      elsif !attribute[:dac_sector_id].blank?
        sector_detail.focal_sector = DacSector.find(attribute[:dac_sector_id])
      end
    end
  end
  
  currency_columns :total_amount_foreseen, :currency => lambda { |cs| cs.donor.currency }
end
