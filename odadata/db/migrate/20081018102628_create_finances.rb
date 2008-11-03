class CreateFinances < ActiveRecord::Migration
  def self.up
    create_table :cofundings do |t|
      t.references :project, :donor
      
      t.integer :amount
      t.string  :currency,   :limit => nil
    end
    
    create_table :exchange_rates do |t|
      t.integer :year
      t.string  :currency
      t.float   :euro_rate
    end
  
    create_table :fundings do |t|
      t.references :project
      
      t.integer :year
      t.integer :payments_q1
      t.integer :payments_q2
      t.integer :payments_q3
      t.integer :payments_q4
      t.integer :commitments
      t.boolean :on_budget,     :default => false
      t.boolean :on_treasury,   :default => false
    end
    
    create_table :funding_forecasts do |t|
      t.references :project
      
      t.integer :year
      t.integer :payments
      t.integer :commitments
      t.boolean :on_budget,     :default => false
      t.boolean :on_treasury,   :default => false
    end
    
    create_table :historic_fundings do |t|
      t.references :project
      
      t.integer :payments
      t.integer :commitments
    end
    
    add_index :cofundings, [:project_id, :donor_id]
    add_index :fundings, :project_id
    add_index :funding_forecasts, :project_id
    add_index :historic_fundings, :project_id
  end
  
  def self.down
    drop_table :cofundings
    drop_table :consistency_finances
    drop_table :exchange_rates
    drop_table :fundings
    drop_table :funding_forecasts
    drop_table :historic_fundings
  end
end
