class Province < ActiveRecord::Base
  CAPTION = "Region"
  
  has_many                :districts, :dependent => :delete_all, :order => 'name ASC'
  has_many                :geo_relevances, :dependent => :delete_all
  has_many                :projects, :through => :geo_relevances, :uniq => true
  
  has_and_belongs_to_many :sector_details
  
  named_scope :ordered, :order => "name ASC"
    
  def self.list
    ActiveSupport::Deprecation.warn("named_scope :ordered should be used here!")
    ordered.all
  end
  
  def <=>(comp)
    self.id <=> comp.id
  end
end

# Bluebook
class Province < ActiveRecord::Base
  has_many :payments, :class_name => "ProvincePayment"
end
