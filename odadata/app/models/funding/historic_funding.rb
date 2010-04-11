class HistoricFunding < ActiveRecord::Base
  belongs_to :project
  
  validates_presence_of :currency
    
  def has_data?
    [:payments, :commitments].any? { |c| self.send(c).to_i > 0 }
  end
  
  # Formatted output for all currency fields
  currency_columns :payments, :commitments,
    :currency => lambda { |f| f.currency }, :year => Project::FIRST_YEAR_OF_RELEVANCE-1, 
    :validations => false
end
