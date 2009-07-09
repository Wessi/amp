Insert into amp_category_class(category_name,keyName,is_multiselect,is_ordered) values ("Indicator Risk","indicator_risk",false,false);
Insert into amp_category_value(category_value,amp_category_class_id,index_column,field_type)(select "Very Low",id,0,0 from amp_category_class where keyName="indicator_risk");
Insert into amp_category_value(category_value,amp_category_class_id,index_column,field_type)(select "Low",id,1,0 from amp_category_class where keyName="indicator_risk");
Insert into amp_category_value(category_value,amp_category_class_id,index_column,field_type)(select "Medium",id,2,0 from amp_category_class where keyName="indicator_risk");
Insert into amp_category_value(category_value,amp_category_class_id,index_column,field_type)(select "High",id,3,0 from amp_category_class where keyName="indicator_risk");
Insert into amp_category_value(category_value,amp_category_class_id,index_column,field_type)(select "Very High",id,4,0 from amp_category_class where keyName="indicator_risk");
Insert into amp_category_value(category_value,amp_category_class_id,index_column,field_type)(select "Critical",id,5,0 from amp_category_class where keyName="indicator_risk");
