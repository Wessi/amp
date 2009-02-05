# This controller handles the login/logout function of the site.  
class SessionsController < ApplicationController

  # render new.rhtml
  def new
  end

  def create
    #logout_keeping_session!
    data = params[:session]
     
    if user = User.authenticate(data[:email], data[:password])
      # Protects against session fixation attacks, causes request forgery
      # protection if user resubmits an earlier form using back
      # button. Uncomment if you understand the tradeoffs.
      # TODO: It would certainly be good to use this but it would also reset the 
      # output locale. It might make sense to store the locale in a separate cookie
      # for that purpose. 
      # reset_session
      current_user = user
      #new_cookie_flag = (data[:remember_me] == "1")
      #handle_remember_cookie! new_cookie_flag
      redirect_back_or_default('/')
      flash[:notice] = "Logged in successfully"
    else
      note_failed_signin
      @email       = data[:email]
      @remember_me = data[:remember_me]
      render :action => 'new'
    end
  end

  def destroy
    logout_killing_session!
    session[:user_id] = nil
    flash[:notice] = "You have been logged out."
    redirect_back_or_default('/')
  end

protected
  # Track failed login attempts
  def note_failed_signin
    flash[:error] = "Couldn't log you in as '#{params[:email]}'"
    logger.warn "Failed login for '#{params[:email]}' from #{request.remote_ip} at #{Time.now.utc}"
  end
end
