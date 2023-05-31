const URI = "http://localhost:3000/users"

const UserApi = {

    getUsers: (setUsersList) => {

        // fetch -> promise based library within JS that helps you make API calls

        // fetch(URI) -> retrieve data at this uri (assume a GET request unless stated otherwise)
        fetch( URI )
            .then( (result) => {      // go here if request successful (200 response)

                console.log("RESULT")
                console.log(result)

                return result.json() // data in next section
            } )
            .then( (data) => {

                console.log("DATA:")
                console.log(data)

                setUsersList(data)

            } )
            .catch( (error) => { console.log(error) } ); // if fetch fails, go here (400/500 responses)
        
    },

    getUserById: (setUserList, userId) => {
        fetch(URI + `/${userId}`)
        .then((result) => {
            console.log("RESULT");
            console.log(result);

            return result.json();
        })
        .then((data) => {
            console.log("DATA: ");
            console.log(data);

            setUserList(data);
        })
        .catch((error) => {console.log(error)} );
    }
}


// allows you to use this object outside of this file
export default UserApi;