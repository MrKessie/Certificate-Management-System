function validateForm() {
    var programmeId = document.getElementById("departmentId").value;
    var programmeName = document.getElementById("departmentName").value;
    var facultyName = document.getElementById("facultyName").value;
    var departmentName = document.getElementById("departmentName").value;

    if (isNaN(programmeId) || programmeName === "" || facultyName === "" || departmentName === "") {
        alert("Please fill in all the fields.");
        return false; // Prevent form submission
    }

    // Submit form data to the backend for saving faculty details
    var form = document.getElementById("programme-form");
    form.action = "/programme/add";
    form.method = "POST";
    form.submit();
    alert("Programme added successfully")
    return true; // Allow form submission
}

// // Submit form data to the backend for importing students
async function importProgrammes(event) {
    event.preventDefault();

    var programmeFile = document.getElementById("importFile").files[0];

    if (!programmeFile) {
        alert("Please select an Excel file.");
        return false; // Prevent form submission
    }

    // Create FormData object
    var formData = new FormData();
    formData.append("programmeFile", programmeFile);

    // Submit form data to the backend for importing departments
    try{
        let response = await fetch("programme/import-programmes", {
            method: "POST",
            body: formData
        });
        // Check if the response is OK
        if (response.ok) {
            alert("Programme imported successfully"); // Show success message
            window.location.reload(); // Reload the page
        } else {
            let errorMessage = await response.text();
            alert('Error: ' + errorMessage); // Show error message
        }
    }
    catch(error) {
        alert("Failed to import programmes: " + error.message);
    };

    return true;
}

// Fetches a list of faculties to fill faculty input control on the form
// document.addEventListener("DOMContentLoaded", function() {
//     fetch('/faculty/all/faculties')
//         .then(response => response.json())
//         .then(data => {
//             let select = document.getElementById('facultyName');
//             data.forEach(faculty => {
//                 let option = document.createElement('option');
//                 option.value = faculty.id;
//                 option.textContent = faculty.facultyName;
//                 select.appendChild(option);
//             });
//         })
//         .catch(error => console.error('Error fetching faculties:', error));
// });

// // Fetches a list of departments to fill department input control on the form
// document.addEventListener("DOMContentLoaded", function() {
//     fetch('/department/all/departments')
//         .then(response => response.json())
//         .then(data => {
//             let select = document.getElementById('departmentName');
//             data.forEach(department => {
//                 let option = document.createElement('option');
//                 option.value = department.id;
//                 option.textContent = department.departmentName;
//                 select.appendChild(option);
//             });
//         })
//         .catch(error => console.error('Error fetching faculties:', error));
// });

function loadPage(page, callback) {
    fetch(page)
        .then(response => response.text())
        .then(data => {
            document.getElementById('mainContent').innerHTML = data;
            if (callback) {
                // callback()
                setTimeout(callback, 0) // Call the callback function if provided
            }
        })
        .catch(error => console.error('Error loading page:', error));
}

function loadFaculties() {
    fetch('/faculty/all/faculties')
        .then(response => response.json())
        .then(data => {
            const facultySelect = document.getElementById('facultyName');
            //facultySelect.innerHTML = ''; // Clear existing options
            data.forEach(faculty => {
                let option = document.createElement('option');
                option.value = faculty.id;
                option.textContent = faculty.facultyName;
                facultySelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading faculties:', error));
}

function loadDepartments() {
    fetch('/department/all/departments')
        .then(response => response.json())
        .then(data => {
            const facultySelect = document.getElementById('departmentName');
            //facultySelect.innerHTML = ''; // Clear existing options
            data.forEach(department => {
                let option = document.createElement('option');
                option.value = department.id;
                option.textContent = department.departmentName;
                facultySelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading departments:', error));
}


// Initial call to load faculties when the DOM is fully loaded
document.addEventListener("DOMContentLoaded", function() {
    loadFaculties();
    loadDepartments();
});

function deleteFaculty(facultyId) {
    if (confirm('Are you sure you want to delete this faculty?')) {
        fetch(`/faculty/delete/${facultyId}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (response.ok) {
                    alert('Faculty deleted successfully.');
                    location.reload(); // Reload the page to reflect changes
                } else {
                    alert('Failed to delete faculty.');
                }
            })
            .catch(error => {
                console.error('Error deleting faculty:', error);
                alert('An error occurred while deleting the faculty.');
            });
    }
}
