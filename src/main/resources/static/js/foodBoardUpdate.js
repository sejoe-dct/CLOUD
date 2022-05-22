function send_put_request() {
    // This method retrive data from form fields and send it to API server.
    // use PUT method to edit the data.
    const xhr = new XMLHttpRequest();
    xhr.open('PUT', '/myPage/foods/{id}');
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = () => {
        if (xhr.status === 200) {
            const data = JSON.parse(xhr.responseText);
            console.log(data);
        }
    };

    // Get data from document's form input fields.
    const data = {
         // 왼쪽 dto 필드 , 우측 태그 안의 id 값
         id : document.getElementById('id').value,
         locationType : document.getElementById('locationType').value,
         foodType : document.getElementById('foodType').value,
         title : document.getElementById('title').value,
         preview : document.getElementById('preview').value,
         address : document.getElementById('address').value,
         info : document.getElementById('info').value,
         rate : document.getElementById('rate').value,
         attachedFiles : document.getElementById('attachedFiles').value
    };
    // Send data to API server.
    xhr.send(JSON.stringify(data));
}


function send_put_file_request() {
    // Using FormData because there is image to send on our page
    // PUT request to server's api, and redirect to the same page
    // get the form element and wrap it to FormData
    var form = document.getElementById('form');
    var formData = new FormData(form);
    // send the request
    var xhr = new XMLHttpRequest();
    xhr.open('PUT', 'http://localhost:3000/api/products/' + document.getElementById('id').value);
    xhr.send(formData);
    xhr.onload = function() {
        if (xhr.status === 200) {
            window.location.href = 'http://localhost:3000/';
        }
    };

}