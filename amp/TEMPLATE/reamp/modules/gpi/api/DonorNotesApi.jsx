class DonorNotesApi {    
    static save(data) { 
        const url = Array.isArray(data) ? '/rest/gpi/donor-notes/save-all' : '/rest/gpi/donor-notes';
        const request = new Request(url, {
            method: 'POST',
            headers: {'Content-Type':'application/json'},
            body: JSON.stringify(data)
        });
        
        return fetch(request).then(response => {
            return response.json();
        }).catch(error => {
            return error;
        });
    }
    
   static deleteDonorNotes(donorNotes) {      
        const request = new Request('/rest/gpi/donor-notes/' + donorNotes.id, {
            method: 'DELETE',
            headers: {'Content-Type':'application/json'}
        });
        return fetch(request).then(response => {
            return response.json();
        }).catch(error => {
            return error;
        })
        
    }
    
}

export default DonorNotesApi;