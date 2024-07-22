function validateForm() {
    var facultyId = document.getElementById("facultyId").value;
    var facultyName = document.getElementById("facultyName").value;

    if (isNaN(facultyId) === null || facultyName === "") {
        alert("Please fill in all the fields.");
        return false; // Prevent form submission
    }

    // Submit form data to the backend for saving faculty details
    var form = document.getElementById("faculty-form");
    form.action = "/faculty/add";
    form.method = "post";
    form.submit();
    alert("Faculty added successfully")
    return true; // Allow form submission
}

function importFaculties() {
    var facultyFile = document.getElementById("importFile").files[0];

    if (!facultyFile) {
        alert("Please select an Excel file.");
        return;
    }

    // Submit form data to the backend for importing students
    var form = document.getElementById("faculty-form");
    form.action = "faculty/import-faculties";
    form.method = "post";
    form.submit();
    alert("Faculty imported successfully")
}

// function loadPage(page, callback) {
//     fetch(page)
//         .then(response => response.text())
//         .then(data => {
//             document.getElementById('mainContent').innerHTML = data;
//             if (callback) {
//                  callback()
//                 setTimeout(callback, 0) // Call the callback function if provided
//             }
//         })
//         .catch(error => console.error('Error loading page:', error));
// }


// Initial call to load faculties when the DOM is fully loaded
// document.addEventListener("DOMContentLoaded", function() {
//     loadFaculties();
// });

function deleteFaculty(button) {
    const row = button.parentElement.parentElement;
    const facultyId = row.querySelector('.faculty-Id').textContent.trim();

    if (confirm('Are you sure you want to delete this faculty?')) {
        fetch(`/faculty/delete/${facultyId}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (response.ok) {
                    // Remove the row from the table
                    row.remove();

                    alert('Faculty year deleted successfully.');
                } else {
                    alert('Failed to delete faculty.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('An error occurred while deleting the faculty.');
            });
    }
}
