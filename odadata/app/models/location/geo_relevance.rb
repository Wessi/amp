class GeoRelevance < ActiveRecord::Base
  belongs_to :project
  belongs_to :province
  belongs_to :district
  
  before_save :update_province_id_for_district
  
private
  # If the inserted record is a target, also insert the id of the corresponding mdg
  def update_province_id_for_district
    if self.district
      self.province_id = self.district.province_id
    end
  end
end