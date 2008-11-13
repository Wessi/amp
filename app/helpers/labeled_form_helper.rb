module LabeledFormHelper  
  # tell all of these methods to use my custom FormBuilder
  [:form_for, :form_remote_for, :remote_form_for].each do |meth|
    src = <<-END_SRC
      def labeled_#{meth}(object_name, *args, &proc)
        options = args.extract_options!
        options.update(:builder => LabeledFormBuilder)
        #{meth}(object_name, *(args << options), &proc)
      end
    END_SRC
    module_eval src, __FILE__, __LINE__
  end
  
  def button_to_remote(name, options = {}, html_options = {})
    button_to_function(name, remote_function(options), html_options) 
  end 
  
  # the custom FormBuilder
  class LabeledFormBuilder < ActionView::Helpers::FormBuilder    
    include ActionView::Helpers
    include LabeledFormHelper
    include I18nHelper
        
    (field_helpers - %w(label check_box radio_button hidden_field date_select fields_for)).each do |selector|
      src = <<-END_SRC
        def #{selector}(method, options = {})
          label_opts = extract_label_options!(options)
          wrap_in_label_row(method, @template.#{selector}(@object_name, method, objectify_options(options)), label_opts)
        end
      END_SRC
      class_eval src, __FILE__, __LINE__
    end
    
    def fields_for(record_or_name_or_array, *args, &block)
      options = args.extract_options!
      options.update(:builder => LabeledFormBuilder)
      
      @template.fields_for(record_or_name_or_array, *(args << options), &block)      
    end
    
    def field_set(legend = nil, options = {}, &block)
      content = @template.capture(&block)
      @template.concat(@template.tag(:fieldset, {}, true))
      @template.concat(@template.content_tag(:legend, legend)) unless legend.blank?
      @template.concat(@template.content_tag(:ol, content))
      @template.concat("</fieldset>")
    end
    
    def hidden_field(method, value)
      @template.hidden_field(@object_name, method, {:object => @object, :value => value})
    end
    
    def date_select(method, options = {}, html_options = {})
      label_opts = extract_label_options!(options)
      # Change default order
      options.reverse_merge!(:order => [:day, :month, :year])
      
      wrap_in_label_row(method, @template.date_select(@object_name, method, objectify_options(options), html_options), label_opts)
    end
    
    def select(method, choices, options = {}, html_options = {})
      label_opts = extract_label_options!(options)
      strip_options_for_select!(choices)
      translated_choices = translate_options(choices)
      
      wrap_in_label_row(method, 
        @template.select(@object_name, method, translated_choices, objectify_options(options), html_options), 
        label_opts)
    end
    
    def collection_select(method, collection, value_method, text_method, options = {}, html_options = {})
      label_opts = extract_label_options!(options)
      wrap_in_label_row(method, 
        @template.collection_select(@object_name, method, collection, value_method, text_method, objectify_options(options), html_options), 
        label_opts)
    end
    
    def submit(value = nil, options = {})
      super(value, options)
    end
    
    def year_select(method, first_year, last_year, options = {}, html_options = {})
      label_opts = extract_label_options!(options) 
      wrap_in_label_row(method, 
        @template.year_select(@object_name, method, first_year, last_year, options, html_options), label_opts)
    end
    
    def check_box(method, options = {}, checked_value = "1", unchecked_value = "0")
      label_opts = extract_label_options!(options)
      wrap_in_label_row(method, 
        @template.check_box(@object_name, method, options, checked_value, unchecked_value), label_opts)
    end
    
    def habtm_check_box(method, options = {}, value = nil)
      label_opts = extract_label_options!(options)
      # We want the label to be on the right side!
      label_opts.merge!({:reverse => true})
      
      assoc_name = "#{@object_name}[#{method}][]"
      box = @template.check_box_tag(assoc_name, value, @object.send(method).include?(value))
      hidden = @template.hidden_field_tag(assoc_name, "")
      wrap_in_label_row(assoc_name, box + hidden, label_opts)
    end
  
    def labeled_row(method, options = {}, &block)
      @template.concat(wrap_in_label_row(method, @template.capture(&block), options), block.binding)
    end
      
    # This overrides methods from the AttributeFu plugin so that they make use of
    # the LabeledFormBuilder automatically and we don't have to give them a :builder
    # argument on every single call.
    def render_associated_form(associated, opts = {})
      opts[:fields_for] ||= {}
      opts[:fields_for].reverse_merge!(:builder => LabeledFormBuilder)
      super(associated, opts)
    end
    
    def add_associated_link(name, object, opts = {})
      opts[:fields_for] ||= {}
      opts[:fields_for].reverse_merge!(:builder => LabeledFormBuilder)
      super(name, object, opts)
    end
    
    def fields_for_associated(associated, *args, &block)
      opts = args.extract_options!
      opts.reverse_merge!(:builder => LabeledFormBuilder)
      args << opts
      super(associated, *args, &block)
    end
  end
  
protected
  def localized_caption(method)
    underscore_class_name = @object.class.to_s.underscore
    method_or_relation_name = method.to_s.sub(/_id$/, '')
  
    ll(underscore_class_name, method_or_relation_name)
  end

  def build_label(method, options)
    options = extract_label_options!(options)
    caption = options[:label] || localized_caption(method)
    #glossary = build_glossary(method, caption, options)
    
    @template.label(@object_name, method, caption)
  end

  def wrap_in_label_row(method, content, options)
    label = build_label(method, options)
    inner_html = [label, content]
    inner_html.reverse! if options.delete(:reverse)
    
    @template.content_tag("li", inner_html.join)
  end
  
  def extract_label_options!(options)
    options.extract!(
      :label => nil,
      :label_class => "sub_title",
      :glossary => false,
      :required => false)
  end
    
  def strip_options_for_select!(options)
    options.map { |e| e.delete_at(2) }
  end
end
