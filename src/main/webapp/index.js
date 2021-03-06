function loadAuthorizedAdminPage() {
    document.getElementById("app").innerHTML = `
        <h1>Hello Administrator!</h1>
        <h2><font color="blue">Welcome to my bookstore</font></h2><br/>
        <h6>ToDO Logout</h6>
        <h3>Search book by ISBN!</h3>
        ISBN: <input type="text" id="isbn"/>
        <button id="getBookThenParseBtn">Get Book</button>
        <div id="book"></div>
        <hr/>
        <h3>Search book by like name!</h3>
        Name: <input type="text" id="name"/>
        <button id="getBookByLikeNameBtn">Get Book</button><br/><br/>
        <div id="searchStatus"></div>
        <hr/>
        <h2>Add a new book</h2>
        <table border="1">
            <tbody>
                <tr><td><b>ISBN</b></td><td><input type="text" id="inputISBN"/></td>
                </tr>
                <tr><td><b>Title</b></td><td><input type="text" id="inputTitle"/></td>
                </tr>
                <tr><td><b>Author</b></td><td><input type="text" id="inputAuthor"/></td>
                </tr>
                <tr><td><b>Edition</b></td><td><input type="number" min="1" max="1000" id="inputEdition"/></td>
                </tr>
                <tr><td><b>Publish Year</b></td><td><input type="number" min="1000" max="2020" id="inputPubYear"/></td>
                </tr>
            </tbody>
        </table><br/>
        <input type="button" value="Clear" id="clearInput"/>
        <input type="button" value="Create book" id="createBook"/>
        <br/><div id="createStatus"></div>
    `;
    //Add onclick event listener
    document.getElementById("clearInput").addEventListener('click', clearInput);
    document.getElementById("createBook").addEventListener('click', createBook);
    document.getElementById("getBookThenParseBtn").addEventListener('click', getBookThenParse);
    document.getElementById("getBookByLikeNameBtn").addEventListener('click', getBookByLikeName);
}

window.addEventListener('load', loadLoginPage)