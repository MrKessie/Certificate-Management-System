console.log("Script is loaded successfully");
// function validateForm() {
//
//     console.log("Script is loaded successfully");
//     // event.preventDefault();
//
//     //Get form fields value
//     const userId = document.getElementById("userId").value;
//     const userName = document.getElementById("fullName").value;
//     const password = document.getElementById("password").value;
//     const confirmPassword = document.getElementById("confirmPassword").value;
//     const gender = document.getElementById("gender").value;
//     const role = document.getElementById("role").value;
//
//     //Check if any field empty
//     if (!userId || !userName || !password || !confirmPassword || !gender || !role) {
//         console.log("C1")
//         alert("Please fill all the fields");
//         return false;
//     }
//
//     //Check if ID field is not a number
//     else if (isNaN(userId)) {
//         console.log("C2")
//         alert("User ID must be a number");
//         return false;
//     }
//
//
//     else if (!/^[a-zA-Z\s]+$/.test(userName.value)) {
//         console.log("C3")
//         alert("Enter a valid username");
//         return false;
//     }
//
//     //Check if passwords mismatch
//
//     else if (password !== confirmPassword) {
//         console.log("C4")
//         alert("Passwords do not match");
//         return false;
//     }
//     else {
//         alert("User added successfully");
//         return true;
//     }
//
//     //Check if user exists
//     // checkUserIdExists(userId);
//     //
//     // Function to check if userId exists in the database using AJAX
// }

document.getElementById('registerForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission

    var userId = document.getElementById('userId').value;

    // Check if the user ID exists
    fetch('/user/exists?userId=' + userId)
        .then(response => response.json())
        .then(data => {
            if (data) {
                alert('User ID already exists. Please enter a new one.');
            } else {
                // If the user ID does not exist, submit the form
                var formData = new FormData(document.getElementById('registerForm'));

                fetch('/user/add', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: new URLSearchParams(formData)
                })
                    .then(response => response.text())
                    .then(message => {
                        alert(message);
                        if (message.includes("successfully")) {
                            document.getElementById('registerForm').reset();
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('An error occurred while adding the user.');
                    });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while checking the user ID.');
        });
});
function checkUserIdExists(userId) {
    let xhr = new XMLHttpRequest();
    xhr.open("GET", "/user/checkUserId?userId=" + userId, true);
    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            if (response.exists()) {
                alert("User ID already exists");
            } else {
                document.getElementById("registerForm").submit();
                alert("User added successfully");
            }
        }
    };
    xhr.send();

    alert("User added successfully");
    return true;
}


// document.addEventListener("DOMContentLoaded", function () {
//     const submitButton = document.getElementById("submitButton");
//     if (submitButton) {
//         submitButton.addEventListener("click", validateForm);
//         const form = document.getElementById("registerForm");
//         form.action = "/user/add";
//         form.method = "POST";
//         form.submit();
//     }
//     else {
//         console.log("Submit button not found");
//     }
// })

