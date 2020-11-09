/* 
 * BangMapleProject. 2020
 */

const END_POINT = 'api/v1/books/';

function clearInput() {
    document.getElementById("inputISBN").value = "";
    document.getElementById("inputTitle").value = "";
    document.getElementById("inputAuthor").value = "";
    document.getElementById("inputEdition").value = "";
    document.getElementById("inputPubYear").value = "";
}

function parseBookToJSONUpdate() {
    let isbn = document.getElementById("inputUpdateISBN").value;
    let title = document.getElementById("inputUpdateTitle").value;
    let author = document.getElementById("inputUpdateAuthor").value;
    let edition = document.getElementById("inputUpdateEdition").value;
    let publishYear = document.getElementById("inputUpdatePubYear").value;
    return `{
        "isbn": "` + isbn + `",
        "title": "` + title + `",
        "author": "` + author + `",
        "edition": "` + edition + `",
        "publishYear": "` + publishYear + `"
    }`;
}

function parseBookToJSONInsert() {
    let isbn = document.getElementById("inputISBN").value;
    let title = document.getElementById("inputTitle").value;
    let author = document.getElementById("inputAuthor").value;
    let edition = document.getElementById("inputEdition").value;
    let publishYear = document.getElementById("inputPubYear").value;
    return `{
        "isbn": "` + isbn + `",
        "title": "` + title + `",
        "author": "` + author + `",
        "edition": "` + edition + `",
        "publishYear": "` + publishYear + `"
    }`;
}

function confirmUpDel() {
    let val = document.getElementById("chkConfirm").checked;
    if (val === false) {
        alert("Please tick the checkbox before doing this action!");
        return false;
    }
    return true;
}

function createBook() {
    fetch(END_POINT, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: parseBookToJSONInsert()
    })
            .then(res => res.json())
            .then(res => {
                clearInput();
                document.getElementById("createStatus").innerHTML = `<h3><font color="green">` + res.status + `</font></h3>`;
            })
            .catch(err => {
                document.getElementById("createStatus").innerHTML = `<h3><font color="red">` + err.status + `</font></h3>`;
            });
}

function updateBook() {
    if (confirmUpDel()) {
        let isbn = document.getElementById("inputUpdateISBN").value;
        if (isbn !== undefined && isbn.length > 0) {
            fetch(String(END_POINT + isbn), {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: parseBookToJSONUpdate()
            })
                    .then(res => res.json())
                    .then(res => document.getElementById("book").innerHTML = `<h3><font color="green">` + res.status + `</font></h3>`)
                    .catch(err => document.getElementById("status").innerHTML = `<h3><font color="red">` + err.status + `</font></h3>`);
        }
    }
}

function deleteBook() {
    if (confirmUpDel()) {
        let isbn = document.getElementById("inputUpdateISBN").value;
        if (isbn !== undefined && isbn.length > 0) {
            fetch(String(END_POINT + isbn), {
                method: 'DELETE'
            })
                    .then(res => res.json())
                    .then(res => document.getElementById("book").innerHTML = `<h3><font color="green">` + res.status + `</font></h3>`)
                    .catch(err => document.getElementById("status").innerHTML = `<h3><font color="red">` + err.status + `</font></h3>`);
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
    var counter = 0;
    var htmlRes = `
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
                    <td>` + (++counter) + `</td>
                    <td>` + book.isbn + `</td>
                    <td>` + book.title + `</td>
                    <td>` + book.author + `</td>
                    <td>` + book.edition + `</td>
                    <td>` + book.publishYear + `</td>
                </tr>
        `;
    });
    htmlRes += `
            </tbody>
        </table>`;
    document.getElementById("searchStatus").innerHTML = htmlRes;
}

function getBookByLikeName() {
    let title = document.getElementById("name").value;

    fetch(String(END_POINT + "likeName?title=" + title))
            .then(res => res.json())
            .then(res => parseBooksToTable(res))
            .catch(err => raiseGetBooksByLikeNameError());
}

function getBookThenParse() {
    let isbn = document.getElementById("isbn").value;
    if (isbn !== undefined && isbn.length > 0) {
        fetch(String(END_POINT + isbn), {
            method: 'GET'
        })
                .then(res => res.json())
                .then(res => parseBook(res))
                .catch(err => raiseGetBookError());
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
                    <td><input value="` + book.isbn + `" id="inputUpdateISBN" readonly/></td>
                </tr>
                <tr>
                    <td><b>Title</b></td>
                    <td><input value="` + book.title + `" id="inputUpdateTitle"/></td>
                </tr>
                <tr>
                    <td><b>Author</b></td>
                    <td><input value="` + book.author + `" id="inputUpdateAuthor"/></td>
                </tr>
                <tr>
                    <td><b>Edition</b></td>
                    <td><input type="number" min="1" max="1000" value="` + book.edition + `" id="inputUpdateEdition"></td>
                </tr>
                <tr>
                    <td><b>Publish Year</b></td>
                    <td><input type="number" min="1000" max="2020" value="` + book.publishYear + `" id="inputUpdatePubYear"/></td>
                </tr>
            </tbody>
        </table><br/>
        <input type="button" onclick="updateBook()" value="Update this book"/>
        <input type="button" onclick="deleteBook()" value="Delete this book"/><br/><br/>
        <input type="checkbox" id="chkConfirm"/> I confirm that I want to update/delete this book.<br/>
        <div id="status"></div>`;
}

