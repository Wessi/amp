import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as startUp from '../actions/StartUpAction';
import * as commonListsActions from '../actions/CommonListsActions';
require('../styles/main.less');
require('bootstrap');
import ActivityView from '../components/ActivityView';

export default class App extends Component {
    constructor( props, context ) {
        super( props, context );
        this.state = { currentActivity: null};
    }

    getActivity(activityId){       
        var component = <div></div>;  
        if (activityId > 0) {            
            //component = <ActivityView/>;    
        } 
        
        return component;
    }

   
    render() { 
      
      return (
            <div>             
                <div className="title-bar">
                    <div className="container">
                        <h2>{this.props.translations['amp.activity-preview:main-title']}</h2>
                        <div className="activity" >{this.getActivity('16704')}</div>
                    </div>
                    </div>
            </div>
             
        );
    }
}

function mapStateToProps(state, ownProps) { 
    return { 
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps(dispatch) {
    return { actions: bindActionCreators( Object.assign( {}, commonListsActions ), dispatch ) };
}

export default connect(mapStateToProps, mapDispatchToProps)(App);

