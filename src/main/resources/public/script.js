function post(){
    let url = "/logs";
    fetch(url, {
        method: "POST",
        body: document.getElementById("word").value,
        headers: {
            "Content-type": "text/html; charset=utf-8"
        }
    });
}

function get(){
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        document.getElementById("logs").innerHTML = this.responseText;
    }
    xhttp.open("GET", "/logs");
    xhttp.send();
}
