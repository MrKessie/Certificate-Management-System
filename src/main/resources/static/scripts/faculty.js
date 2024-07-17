function validateForm() {
    var facultyId = document.getElementById("facultyId").value;
    var facultyName = document.getElementById("facultyName").value;

    if (facultyId === null || facultyName === "") {
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
