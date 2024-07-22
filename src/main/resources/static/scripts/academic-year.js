function validateForm(e) {
    e.preventDefault(); // Prevent form submission
    e.stopPropagation();
    var academicYear = document.getElementById("academicYear").value;
    // var facultyName = document.getElementById("facultyName").value;

    if (academicYear === "") {
        alert("Please fill in all the fields.");
        return false; // Prevent form submission
    }
    alert("Hu")
    // Submit form data to the backend for saving faculty details
    var form = document.getElementById("academicYear-form");
    form.action = "/academic-year/add";
    form.method = "post";
    form.submit();
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


// Initial call to load faculties when the DOM is fully loaded
document.addEventListener("DOMContentLoaded", function() {
    loadFaculties();
});

// // Get all delete buttons
// const deleteButtons = document.querySelectorAll('#deleteButton');
//
// // Add event listener to each delete button
// deleteButtons.forEach(button => {
//     button.addEventListener('click', () => {
//         const yearId = button.dataset.yearId;
//
//         // Check if yearId is not undefined or null
//         if (yearId !== undefined && yearId !== null) {
//             fetch(`/academic-year/delete?yearId=${yearId}`, {
//                 method: 'DELETE'
//             })
//             // ... (rest of the code)
//         } else {
//             console.error('Invalid yearId value:', yearId);
//             alert('Failed to delete academic year due to an invalid ID');
//         }
//     });
// });

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
