class CommonListsApi {
    
    static getCurrencyList() {
        const request = new Request('/rest/settings-definitions/gpi', {
            method: 'GET'      
        });
        
        return fetch(request).then(response => {
            return response.json();
        }).catch(error => {
            return error;
        });
    }
    
    static getOrgList() {     
        const request = new Request('/rest/filters/orgs', {
            method: 'GET'      
        });
        
        return fetch(request).then(response => {
            return response.json();
        }).catch(error => {
            return error;
        });
    }
    
    static getSettings() {     
        const request = new Request('/rest/amp/settings', {
            method: 'GET'      
        });
        
        return fetch(request).then(response => {
            return response.json();
        }).catch(error => {
            return error;
        });
    }
    
}

export default CommonListsApi;