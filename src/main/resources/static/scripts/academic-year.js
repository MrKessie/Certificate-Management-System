function validateForm() {
    // e.preventDefault();
    var academicYear = document.getElementById("academicYear").value;

    if (academicYear === "") {
        alert("Please fill in all the fields.");
        return false; // Prevent form submission
    }
    // Submit form data to the backend for saving faculty details
    var form = document.getElementById("academicYear-form");
    // form.action = "/academic-year/add";
    // form.method = "post";
    // form.submit();
    alert("Academic Year added successfully")
    return true; // Allow form submission
}

function importAcademicYears() {
    var academicYearFile = document.getElementById("importFile").files[0];

    if (!academicYearFile) {
        alert("Please select an Excel file.");
        return;
    }

    // Submit form data to the backend for importing students
    var form = document.getElementById("academicYear-form");
    form.action = "academic-year/import-academic-years";
    form.method = "post";
    form.submit();
    alert("Academic year imported successfully")
}

// Initial call to load faculties when the DOM is fully loaded
document.addEventListener("DOMContentLoaded", function() {
    loadFaculties();
    loadTableData('id');
});

function deleteAcademicYear(button) {
    const row = button.parentElement.parentElement;
    const yearId = row.querySelector('.year-id').textContent.trim();

    if (confirm('Are you sure you want to delete this academic year?')) {
        fetch(`/academic-year/delete/${yearId}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (response.ok) {
                    // Remove the row from the table
                    row.remove();

                    alert('Academic year deleted successfully.');
                } else {
                    alert('Failed to delete academic year.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('An error occurred while deleting the academic year.');
            });
    }
}


function sortTable() {
    const sortBy = document.getElementById('sort-by').value;
    loadTableData(sortBy);
}

function loadTableData(sortBy) {
    fetch(`academic-year/sort-by?sortBy=${sortBy}`)
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById('academicYearTable').getElementsByTagName('tbody')[0];
            tableBody.innerHTML = '';

            data.forEach(item => {
                const row = tableBody.insertRow();
                row.insertCell(0).textContent = item.yearId;
                row.insertCell(1).textContent = item.year;
                row.insertCell(2).textContent = item.dateAdded;
                row.insertCell(3).textContent = item.dateEdited;
            });
        });
}

