module MultiCurrency
  class ConvertibleCurrency
    include Comparable
    include ActionView::Helpers::NumberHelper
  
    attr_reader :base_value, :currency, :year, :nature
  
    # The nature attribute can be one of "historic, current and forecasts" and is required to allow the use of
    # different exchange rate tables
    def initialize(value, currency = nil, base_year = nil, nature = 'current')
      @currency = currency
      @year = base_year
      @nature = nature || 'current'
  
      case value
      when String
        #TODO: Improve this hack to get the regexp to do the right replacement for negative numbers with = 3.n digits
        if value.to_f < 0 #negative
          @base_value = value.gsub(/\D(\d{3})/, '\1').sub(/\D(\d{1,2})$/, '.\1').to_f
          @base_value = @base_value * (-1) if @base_value > 0
        else
          @base_value = value.gsub(/\D(\d{3})/, '\1').sub(/\D(\d{1,2})$/, '.\1').to_f
        end
      else
        @base_value = value.to_f
      end
    end
  
    # ========================
    # = General Calculations =
    # ========================
    def +(other)
      operation_on_common_currency(self, other, :+)
    end
  
    def -(other)
      operation_on_common_currency(self, other, :-)
    end
  
    def /(other)
      operation_on_common_currency(self, other, :/)
    end
  
    def *(other)
      operation_on_common_currency(self, other, :*)
    end
  
    def ==(other)
      @base_value == other.to_currency.base_value
    end
  
    def ===(other)
      @base_value == other.to_currency(@currency).base_value
    end
  
    def <=>(other)
      @base_value <=> other.to_currency(@currency).base_value
    end
  
    # ===============
    # = Conversions =
    # ===============  
    def to_i
      @base_value.round
    end
  
    def to_f
      @base_value
    end
  
    def to_s(unit = true)
      output_currency = MultiCurrency.output_currency || @currency
      number = number_with_delimiter(self.in(output_currency).to_i)
  
      if output_currency && unit
        number + " #{output_currency}"
      else
        number
      end
    end
  
    def to_currency(currency = nil, base_year = nil)
      self.in(currency)
    end
  
    def in(currency)
      # If no currency have been given at all or we don't need to convert just return the current object
      return self if currency.blank? || @currency == currency
      
      # If this is an anonymous "0", we can simply set the currency
      if (@base_value == 0) && !@currency
        @currency = currency 
        return self
      end
      
      # If we don't know the currency of the old value, we can't convert it
      raise ConversionOfAnonymousCurrency,
        "Can't convert anonymous currency into #{currency}" unless @currency
  
      # Find exchange rate for old currency -> new currency
      begin
        rates_source = MultiCurrency.send("#{@nature}_rates_source") || ExchangeRate::SOURCES.first
        rate = ExchangeRate.find_rate(@currency, currency, @year, rates_source) 
      rescue ActiveRecord::RecordNotFound, NoMethodError
        raise ExchangeRateUnavailable, 
          "Can't find exchange rate for conversion from #{@currency} into #{currency} for year #{@year} at source #{rates_source}"
      end
  
      ConvertibleCurrency.new(@base_value * rate, currency, @year)
    end
    
  private
    def operation_on_common_currency(a, b, operation)
      return a unless b
      
      year = (a.year == b.try(:year)) ? a.year : nil
      converted_b = b.to_currency(a.currency, (b.try(:year) || year)).base_value
      
      ConvertibleCurrency.new(a.base_value.send(operation, converted_b), (a.currency || b.currency), year)
    end
  end
end
