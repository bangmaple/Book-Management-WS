/* 
 * BangMapleProject. 2020
 */

const END_POINT = 'api/v1/books/';

var isbn, title, author, edition, pubYear;
var updateISBN, updateTitle, updateAuthor, updateEdition, updatePubYear;


//Initialize variables
const initializeVariables = () => {
    isbn = document.getElementById("inputISBN");
    title = document.getElementById("inputTitle");
    author = document.getElementById("inputAuthor");
    edition = document.getElementById("inputEdition");
    pubYear = document.getElementById("inputPubYear");
}

const initializeInputUpdateVariables = () => {
    updateISBN = document.getElementById("inputUpdateISBN");
    updateTitle = document.getElementById("inputUpdateTitle");
    updateAuthor = document.getElementById("inputUpdateAuthor");
    updateEdition = document.getElementById("inputUpdateEdition");
    updatePubYear = document.getElementById("inputUpdatePubYear");
}

window.addEventListener('load', initializeVariables);


function clearInput() {
    isbn.value = "";
    title.value = "";
    author.value = "";
    edition.value = "";
    pubYear.value = "";
}

function parseBookToJSONUpdate() {
    return `{
        "isbn": "${updateISBN.value}",
        "title": "${updateTitle.value}",
        "author": "${updateAuthor.value}",
        "edition": "${updateEdition.value}",
        "publishYear": "${updatePubYear.value}"
    }`;
}

function parseBookToJSONInsert() {
    return `{
        "isbn": "${document.getElementById("inputISBN").value}",
        "title": "${document.getElementById("inputTitle").value}",
        "author": "${document.getElementById("inputAuthor").value}",
        "edition": "${document.getElementById("inputEdition").value}",
        "publishYear": "${document.getElementById("inputPubYear").value}"
    }`;
}

function confirmUpDel() {
    return document.getElementById("chkConfirm").checked ? true : () => {
        alert("Please tick the checkbox before doing this action!");
        return false;
    };
}

async function createBook() {
    const response = await fetch(END_POINT, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': window.localStorage.getItem("tokenId")
        },
        body: parseBookToJSONInsert()
    });
    if (response.status === 401) {
        window.localStorage.removeItem("tokenId");
        loadLoginPage()
    } else if (response.status === 200) {
        clearInput();
        response.json().then((res) => {
            window.localStorage.setItem("tokenId", response.headers.get("Authorization"));
            document.getElementById("createStatus").innerHTML = `<h3><font color="green">${res.status}</font></h3>`;
        });
    } else {
        response.json().then((res) => {
            window.localStorage.setItem("tokenId", response.headers.get("Authorization"));
            document.getElementById("createStatus").innerHTML = `<h3><font color="red">${res.status}</font></h3>`;
        });
    }
}

async function updateBook() {
    if (confirmUpDel() && updateISBN.value !== undefined && updateISBN.value.length > 0) {
        const updateBookUrl = END_POINT + updateISBN.value;
        const response = await fetch(updateBookUrl, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': window.localStorage.getItem("tokenId")
            },
            body: parseBookToJSONUpdate()
        });
        if (response.status === 401) {
            window.localStorage.removeItem("tokenId");
            loadLoginPage()
        } else if (response.status === 200) {
            window.localStorage.setItem("tokenId", response.headers.get("Authorization"));
            response.json().then((res) => {
                document.getElementById("book").innerHTML = `<h3><font color="green">${res.status}</font></h3>`;
            });
        } else {
            window.localStorage.setItem("tokenId", response.headers.get("Authorization"));
            response.json().then((res) => {
                document.getElementById("status").innerHTML = `<h3><font color="red">${res.status}</font></h3>`
            });
        }
    }
}

async function deleteBook() {
    if (confirmUpDel()) {
        if (updateISBN.value !== undefined && updateISBN.value.length > 0) {
            const deleteUrl = END_POINT + updateISBN.value;
            const response = await fetch(deleteUrl, {
                headers: {
                    'Authorization': window.localStorage.getItem("tokenId")
                }, method: 'DELETE'
            });
            switch (response.status) {
                case 401:
                    window.localStorage.removeItem("tokenId");
                    loadLoginPage()
                    break;
                case 200:
                    window.localStorage.setItem("tokenId", response.headers.get("Authorization"));
                    response.json().then((res) => {
                        document.getElementById("book").innerHTML = `<h3><font color="green">${res.status}</font></h3>`;
                    });
                    break;
                default:
                    window.localStorage.setItem("tokenId", response.headers.get("Authorization"));
                    response.json().then((res) => {
                        document.getElementById("status").innerHTML = `<h3><font color="red">${res.status}</font></h3>`;
                    });
                    break;
            }
        }
    }
}

function raiseGetBooksByLikeNameError() {
    document.getElementById("searchStatus").innerHTML = `<h3><font color="red">Failed to get books! Try again later.</font></h3>`;
}

function parseBooksToTable(books) {
    if (books.length === 0) {
        document.getElementById("searchStatus").innerHTML = `<h3><font color="red">No record found!</font></h3>`;
        return;
    }
    let counter = 0;
    let htmlRes = `
        <table border="1">
        <thead>
            <tr>
                <td>No</td>
                <td>ISBN</td>
                <td>Title</td>
                <td>Author</td>
                <td>Edition</td>
                <td>Publish Year</td>
            </tr>
        </thead>
            <tbody>`;
    books.forEach(book => {
        htmlRes += `
                <tr>
                    <td>${(++counter)}</td>
                    <td>${book.isbn}</td>
                    <td>${book.title}</td>
                    <td>${book.author}</td>
                    <td>${book.edition}</td>
                    <td>${book.publishYear}</td>
                </tr>
        `;
    });
    htmlRes += `
            </tbody>
        </table>`;
    document.getElementById("searchStatus").innerHTML = htmlRes;
}

async function getBookByLikeName() {
    let title = document.getElementById("name").value;
    const getBookByLikeNameUrl = END_POINT + "likeName?title=" + title;
    const response = await fetch(getBookByLikeNameUrl, {
        headers: {
            'Authorization': window.localStorage.getItem("tokenId")
        }
    })

    if (response.status === 401) {
        window.localStorage.removeItem("tokenId");
        loadLoginPage();
    } else if (response.status === 200) {
        response.json().then((res) => parseBooksToTable(res));
    } else {
        raiseGetBooksByLikeNameError();
    }
}

async function getBookThenParse() {
    let searchISBN = document.getElementById("isbn").value;
    if (searchISBN !== undefined && searchISBN.length > 0) {
        const searchISBNUrl = END_POINT + searchISBN;
        const response = await fetch(searchISBNUrl, {
            headers: {
                'Authorization': window.localStorage.getItem("tokenId")
            },
            method: "GET"
        });
        if (response.status === 401) {
            window.localStorage.removeItem("tokenId");
            loadLoginPage();
        } else if (response.status === 200) {
            response.json().then((res) => parseBook(res));
        } else {
            raiseGetBooksByLikeNameError()
        }

    } else {
        raiseRequireISBNError();
    }
}

function raiseRequireISBNError() {
    document.getElementById("book").innerHTML = `<h3><font color="red">Please input the ISBN before getting the book.</font></h3>`;
}

function raiseGetBookError() {
    document.getElementById("book").innerHTML = `<h3><font color="red">Failed to retrieve book. (wrong ISBN?)</font></h3>`;
}

function parseBook(book) {
    document.getElementById("book").innerHTML = `<br/>
        <table border="1">
            <tbody>
                <tr>
                    <td><b>ISBN</b></td>
                    <td><input value="${book.isbn}" id="inputUpdateISBN" readonly/></td>
                </tr>
                <tr>
                    <td><b>Title</b></td>
                    <td><input value="${book.title}" id="inputUpdateTitle"/></td>
                </tr>
                <tr>
                    <td><b>Author</b></td>
                    <td><input value="${book.author}" id="inputUpdateAuthor"/></td>
                </tr>
                <tr>
                    <td><b>Edition</b></td>
                    <td><input type="number" min="1" max="1000" value="${book.edition}" id="inputUpdateEdition"></td>
                </tr>
                <tr>
                    <td><b>Publish Year</b></td>
                    <td><input type="number" min="1000" max="2020" value="${book.publishYear}" id="inputUpdatePubYear"/></td>
                </tr>
            </tbody>
        </table><br/>
        <button id="updateBookBtn">Update this book</button>
        <button id="deleteBookBtn">Delete this book</button><br/><br/>
        <input type="checkbox" id="chkConfirm"/> I confirm that I want to update/delete this book.<br/>
        <div id="status"></div>`;
    initializeInputUpdateVariables();
    //Add onclick event listener
    document.getElementById("updateBookBtn").addEventListener('click', updateBook);
    document.getElementById("deleteBookBtn").addEventListener('click', deleteBook);
}


