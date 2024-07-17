function validateForm() {
    var departmentId = document.getElementById("departmentId").value;
    var departmentName = document.getElementById("departmentName").value;
    var facultyName = document.getElementById("facultyName").value;
    var selectedIndex = facultyName.selectedIndex;

    if (departmentId === null || departmentName === "") {
        alert("Please fill in all the fields.");
        return false; // Prevent form submission
    }

    else if (selectedIndex === 0) {
        alert("Please select valid Faculty")
        return false
    }

    // Submit form data to the backend for saving faculty details
    var form = document.getElementById("department-form");
    form.action = "/department/add";
    form.method = "POST";
    form.submit();
    alert("Department added successfully")
    return true; // Allow form submission
}

function importDepartments() {
    var departmentFile = document.getElementById("importFile").files[0];

    if (!departmentFile) {
        alert("Please select an Excel file.");
        return;
    }

    // Submit form data to the backend for importing students
    var form = document.getElementById("department-form");
    form.action = "department/import-faculties";
    form.method = "POST";
    form.submit();
    alert("Department imported successfully")
}

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