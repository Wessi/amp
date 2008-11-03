class DonorsController < ApplicationController
  access_rule 'admin'
  
  def index
    @donors = Donor.find_without_localization(:all, :order => "name ASC")
  end
  
  def new
    @donor = Donor.new
  end

  def create
    @donor = Donor.new(params[:donor])
    if @donor.save
      flash[:notice] = 'Donor was successfully created.'
      redirect_to donors_path
    else
      flash[:error] = 'There was an error creating a Donor, please check next to the fields for more information'
      render :action => 'new'
    end
  end

  def edit
    @donor = Donor.find_without_localization(params[:id])
  end

  def update
    @donor = Donor.find_without_localization(params[:id])
    if @donor.update_attributes(params[:donor])
      flash[:notice] = 'Donor was successfully updated.'
      redirect_to donors_path
    else
      flash[:error] = 'There was an error updating a Donor, please check next to the fields for more information'           
      render :action => 'edit'
    end
  end
  

  protected
  # TODO: Move to right place! (admin controller?!)
  # Filter to check whether data input is open
  def data_input_open?
    if Prefs.data_input_open
      true
    else
      session[:redirect_url] = request.referer
      redirect_to :controller => "admin", :action => "data_input_closed"
      false
    end
  end
end