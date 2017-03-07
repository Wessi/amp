import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import AidOnBudgetRow from './AidOnBudgetRow.jsx';
import * as aidOnBudgetActions from '../actions/AidOnBudgetActions.jsx';
import * as commonListsActions from  '../actions/CommonListsActions.jsx';
import * as startUp from '../actions/StartUpAction.jsx';
import { Alert } from 'react-bootstrap';
export default class AidOnBudgetList extends Component {    
    constructor(props, context) {      
        super(props, context);
        this.state = {                
                errors: [],
                infoMessages:[]
        };
        
        this.addNew = this.addNew.bind(this);
        this.showErrors = this.showErrors.bind(this);
        this.showInfoMessages = this.showInfoMessages.bind(this);
        this.saveAllEdits = this.saveAllEdits.bind(this);
        this.goToClickedPage = this.goToClickedPage.bind(this);
        this.goToNextPage = this.goToNextPage.bind(this);
        this.goToPreviousPage = this.goToPreviousPage.bind(this);
        this.sort = this.sort.bind(this); 
        this.showSortCaret =  this.showSortCaret.bind(this);
    }
    
    componentWillMount() {        
        this.props.actions.loadAidOnBudgetList({paging: this.props.paging, sorting: this.props.sorting}); 
        this.props.actions.getCurrencyList();
        this.props.actions.getOrgList();
    }
    
    componentWillReceiveProps(nextProps) { 
        this.setState({errors: nextProps.errors, infoMessages:  nextProps.infoMessages});        
    }
    
    addNew() {
        this.props.actions.addNewAidOnBudget();       
    }
    
    goToClickedPage(event){
        const pageNumber = event.target.getAttribute('data-page');
        this.goToPage(pageNumber);  
    }
    
    goToNextPage() {
        const pageNumber = ++this.props.paging.currentPageNumber
        this.goToPage(pageNumber);
    }
    
    goToPreviousPage(){
        const pageNumber = --this.props.paging.currentPageNumber;
        this.goToPage(pageNumber);        
    }
    
    goToPage(pageNumber){
        const loadParams = {};
        loadParams.paging = this.props.paging;
        loadParams.sorting = this.props.sorting;
        loadParams.paging.currentPageNumber = pageNumber;
        loadParams.paging.offset = ((pageNumber - 1) * this.props.paging.recordsPerPage);  
        this.props.actions.loadAidOnBudgetList(loadParams);  
    }
    
    sort(event) {
        const field = event.target.getAttribute('data-field'); 
        const loadParams = {};
        loadParams.paging = this.props.paging;
        loadParams.sorting = this.props.sorting;
        if (loadParams.sorting.orderBy === field) {
            loadParams.sorting.sortOrder = loadParams.sorting.sortOrder === 'asc' ? 'desc' : 'asc';
        } else {
            loadParams.sorting.orderBy = field;
            loadParams.sorting.sortOrder = 'asc';
        }         
        this.props.actions.loadAidOnBudgetList(loadParams);  
        
    }
    
    saveAllEdits() {
        const list = this.props.aidOnBudgetList.filter(aidOnBudget => {return aidOnBudget.isEditing})
        this.props.actions.saveAllEdits(list)
    }
    
    showErrors() {        
        const errors = this.props.errors.filter(error => {return !error.id && !error.cid });
        return (errors.length > 0 && <div className="alert alert-danger" role="alert">
                {errors.map(error =>                    
                    <span>{this.props.translations[error.messageKey]} <br/></span>                
                )}
        </div>) 
    }
    
    showInfoMessages() {
        return (this.state.infoMessages.length > 0 &&
                <div className="alert alert-info" role="alert">
                {this.state.infoMessages.map(info =>
                <span>{this.props.translate(info.messageKey, info.params)} <br/></span>
                )}
        </div>) 
    }
    
    showSortCaret(field){        
        var className = '';         
        if(this.props.sorting.sortOrder == 'asc' && field === this.props.sorting.orderBy){
            className = 'glyphicon glyphicon-chevron-up';
        } else if(this.props.sorting.sortOrder == 'desc' && field === this.props.sorting.orderBy){
            className = 'glyphicon glyphicon-chevron-down';
        } 
        
        return className;
    }
    
    render() {       
        const pages = ([...Array(this.props.paging.totalPageCount + 1).keys()]).slice(1);        
        return (
                <div >                
                <h2>{this.props.translations['amp.gpi-data-aid-on-budget:title']}</h2>
                <div className="panel panel-default">                 
                <div className="panel-body custom-panel">
                <span className="glyphicon glyphicon-plus" onClick={this.addNew}></span>
                <span  onClick={this.addNew}> Add Data</span>
                <span className="success-color"> (insert data to the new field)</span>
                <span> / </span> <span className="glyphicon glyphicon-ok-circle success-color"> </span> <span > Click the Save Symbol to save the added data row</span>
                <span className="float-right"> <button type="button" className="btn btn-success" onClick = {this.saveAllEdits}>Save all Edits</button></span>
                </div>                 
                </div>  
                {this.showErrors()}
                {this.showInfoMessages()} 
                <table className="table table-striped">
                <thead>
                <tr>
                <th></th>
                <th><span data-field="indicatorDate" onClick={this.sort} >{this.props.translations['amp.gpi-data-aid-on-budget:date']}</span><span className="error-color" >*</span><span className = {this.showSortCaret('indicatorDate')} ></span></th>
                <th><span data-field="donor"  onClick={this.sort}>{this.props.translations['amp.gpi-data-aid-on-budget:donor-agency']}</span><span className="error-color" >*</span><span className = {this.showSortCaret('donor')} ></span></th>
                <th><span data-field="amount" onClick={this.sort}>{this.props.translations['amp.gpi-data-aid-on-budget:amount']}</span><span className="error-color" >*</span><span className = {this.showSortCaret('amount')} ></span></th>
                <th><span data-field="currency" onClick={this.sort}>{this.props.translations['amp.gpi-data-aid-on-budget:currency']}</span><span className="error-color" >*</span><span className = {this.showSortCaret('currency')} ></span></th>
                <th>{this.props.translations['amp.gpi-data-aid-on-budget:action']}</th>
                </tr>
                </thead>
                <tbody>               
                {this.props.aidOnBudgetList.map(aidOnBudget => 
                <AidOnBudgetRow aidOnBudget={aidOnBudget} key={aidOnBudget.id} currencyList={this.props.currencyList} orgList={this.props.orgList} key={aidOnBudget.id || 'c' + aidOnBudget.cid} errors={this.props.errors}/>  
                )}                
                </tbody>
                </table> 
                {pages.length > 1 &&
                    <nav>
                    <ul className="pagination fixed-pagination">
                    <li className={this.props.paging.currentPageNumber == 1 ? 'page-item disabled': 'page-item' }>
                    <a className="page-link previ pagination-link" aria-label="Previous" data-page="-"  onClick={this.goToPreviousPage}>
                    <span aria-hidden="true" data-page="-">&laquo;</span>
                <span className="sr-only">Previous</span>
                </a>
                </li>                     
                {pages.map(pageNumber => 
                <li className={this.props.paging.currentPageNumber == pageNumber ? 'page-item  active': 'page-item' } key={pageNumber} ><a className="page-link pagination-link" data-page={pageNumber} onClick={this.goToClickedPage}>{pageNumber}</a></li>  
                )}
                <li className={this.props.paging.currentPageNumber == this.props.paging.totalPageCount ? 'page-item disabled': 'page-item' }>
                <a className="page-link pagination-link"  aria-label="Next" data-page="+" onClick={this.goToNextPage}>
                <span aria-hidden="true" data-page="+">&raquo;</span>
                <span className="sr-only">Next</span>
                </a>
                </li>
                </ul>
                </nav>}
                </div>
        );
    }
}

function mapStateToProps(state, ownProps) { 
    return {
        aidOnBudgetList: state.aidOnBudget.aidOnBudgetList || [],
        paging: state.aidOnBudget.paging,
        sorting: state.aidOnBudget.sorting,
        errors: state.aidOnBudget.errors || [],
        infoMessages: state.aidOnBudget.infoMessages || [],        
        currencyList: state.commonLists.currencyList || [],
        orgList: state.commonLists.orgList || [],
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps(dispatch) {
    return {actions: bindActionCreators(Object.assign({}, aidOnBudgetActions, commonListsActions), dispatch)}
}

export default connect(mapStateToProps, mapDispatchToProps)(AidOnBudgetList);
