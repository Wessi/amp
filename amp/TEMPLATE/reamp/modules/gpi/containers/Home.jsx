import React, { Component, PropTypes } from 'react';
import AidOnBudgetList from "../components/AidOnBudgetList.jsx";
import DonorNotesList from "../components/DonorNotesList.jsx"; 

export default class App extends Component {

    constructor(props, context) {
      
        super(props, context);
    }
   
    render() {        
        return (
            <div >
                <div>

                <ul className="nav nav-tabs" role="tablist">     
                  
                  <li role="presentation" ><a href="#indicator1" aria-controls="indicator1" role="tab" data-toggle="tab">Indicator 1</a></li>
                  <li role="presentation" className="active"><a href="#indicator6" aria-controls="indicator6" role="tab" data-toggle="tab">Indicator 6</a></li>
                </ul>

                <div className="tab-content panel">
                 <div role="tabpanel" className="tab-pane " id="indicator1">
                  
                  <DonorNotesList/>
                  
                  </div>
                      
                   <div role="tabpanel" className="tab-pane active" id="indicator6"><AidOnBudgetList/></div>                  
                </div>

              </div>
                 
            </div>
        );
    }
}
