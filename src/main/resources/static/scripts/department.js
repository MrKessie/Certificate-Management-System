function validateForm() {
    var departmentId = document.getElementById("departmentId").value;
    var departmentName = document.getElementById("departmentName").value;
    var facultyName = document.getElementById("facultyName").value;
    //var selectedIndex = facultyName.selectedIndex;

    if (isNaN(departmentId) || departmentName === "" || facultyName === 0) {
        alert("Please fill in all the fields.");
        return false; // Prevent form submission
    }

    var form = document.getElementById("department-form");
    form.action = "/department/add";
    form.method = "post";
    form.submit();
    alert("Department added successfully")
    return true; // Allow form submission
}


// // Submit form data to the backend for importing students
async function importDepartments(event) {
    event.preventDefault();

    var departmentFile = document.getElementById("importFile").files[0];

    if (!departmentFile) {
        alert("Please select an Excel file.");
        return false; // Prevent form submission
    }

    // Create FormData object
    var formData = new FormData();
    formData.append("departmentFile", departmentFile);

    // Submit form data to the backend for importing departments
    try{
        let response = await fetch("department/import-departments", {
            method: "POST",
            body: formData
        });
        // Check if the response is OK
        if (response.ok) {
            alert("Department imported successfully"); // Show success message
            window.location.reload(); // Reload the page
        } else {
            let errorMessage = await response.text();
            alert('Error: ' + errorMessage); // Show error message
        }
    }
    catch(error) {
        alert("Failed to import departments: " + error.message);
    };

    return true;
}

// Fetches a list of faculties to fill input control on the form
document.addEventListener("DOMContentLoaded", function() {
    fetch('/faculty/all/faculties')
        .then(response => response.json())
        .then(data => {
            let select = document.getElementById('facultyName');
            data.forEach(faculty => {
                let option = document.createElement('option');
                option.value = faculty.id;
                option.textContent = faculty.facultyName;
                select.appendChild(option);
            });
        })
        .catch(error => console.error('Error fetching faculties:', error));
});
