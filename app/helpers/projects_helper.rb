module ProjectsHelper
  def factsheet_link(project)
    img = image_tag('/images/details.gif', :size => "14x15", :class => 'no_print', 
      :title => I18n.t('project.factsheet_link'))
      
    link_to(img, project, :popup => ['_blank', 'height=800,width=800,scrollbars=1'])
  end
  
  def aid_modality_select(form)
    choices = AidModality.all.group_by(&:group_name)
    
    ungrouped = choices.delete(nil).inject("") do |s, values|
      options = { :value => values.id, :class => "fake-heading" }
      options.merge!({ :selected => "selected" }) if form.object.aid_modality_id == values.id
      
      s << content_tag(:option, values.name, options)
    end

    grouped = choices.inject("") do |s, (k, values)|
      s << content_tag(:optgroup, options_from_collection_for_select(values, :id, :name, form.object.aid_modality_id), :label => k)
    end
    
    form.label(:aid_modality_id) + select_tag("#{form.object_name}[aid_modality_id]", ungrouped + grouped)
  end
end