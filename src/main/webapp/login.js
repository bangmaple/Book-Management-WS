function isLoggedIn() {
    let token = window.localStorage.getItem("tokenId");
    console.log(token);
    if (token !== null) {
        loadAuthorizedAdminPage();
        return true;
    }
    return false;
}

function loadLoginPage() {
    if (!isLoggedIn()) {
        document.getElementById("app").innerHTML = `
        <h1>Login page</h1>
        <hr>
        <h3><font color="blue">Please login before using our services!</font></h3>
        <h5 id="loginStatus"></h5>
        Username: <input id="username"/><br/>
        Password: <input type="password" id="password"/><br/>
        <button id="login" onclick="login()">Login</button>`;
    }
}

function parseUserDataToJSON(username, password) {
    return `{
        "username": "${username}",
        "password": "${password}"
    }`;
}

async function login() {
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    if (username.length > 0) {
        if (password.length > 0) {
            await fetch("api/v1/users/login", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: parseUserDataToJSON(username, password)
            }).then(res => {
                window.localStorage.setItem("tokenId", res.headers.get("Authorization"));
                res.json()
            })
                .then(loadAuthorizedAdminPage)
                .catch((res) => {
                    if (res.message === "failed") {
                        document.getElementById("loginStatus").innerHTML = `<font color="red">Invalid username or password.</font>`;
                    }
                })
        }
    }
}