function validateForm() {
    var studentId = document.getElementById("studentId").value;
    var studentName = document.getElementById("studentName").value;
    var facultyName = document.getElementById("facultyName").value;
    var departmentName = document.getElementById("departmentName").value;
    var academicYear = document.getElementById("academicYear").value;
    var programmeName = document.getElementById("programmeName").value;

    if (studentId === "" || studentName === "" || programmeName === 0 || facultyName === 0 || departmentName === 0 || academicYear === 0) {
        alert("Please fill in all the fields.");
        return false; // Prevent form submission
    }

    // Submit form data to the backend for saving faculty details
    var form = document.getElementById("student-form");
    form.action = "/student/add";
    form.method = "POST";
    form.submit();
    alert("Student added successfully")
    return true; // Allow form submission
}

// // Submit form data to the backend for importing students
async function importStudents(event) {
    event.preventDefault();

    var studentFile = document.getElementById("importFile").files[0];

    if (!studentFile) {
        alert("Please select an Excel file.");
        return false; // Prevent form submission
    }

    // Create FormData object
    var formData = new FormData();
    formData.append("studentFile", studentFile);

    // Submit form data to the backend for importing departments
    try{
        let response = await fetch("student/import-students", {
            method: "POST",
            body: formData
        });
        // Check if the response is OK
        if (response.ok) {
            alert("Students imported successfully"); // Show success message
            window.location.reload(); // Reload the page
        } else {
            let errorMessage = await response.text();
            alert('Error: ' + errorMessage); // Show error message
        }
    }
    catch(error) {
        alert("Failed to import students: " + error.message);
    };

    return true;
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
            const departmentSelect = document.getElementById('departmentName');
            //facultySelect.innerHTML = ''; // Clear existing options
            data.forEach(department => {
                let option = document.createElement('option');
                option.value = department.id;
                option.textContent = department.departmentName;
                departmentSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading departments:', error));
}

function loadProgrammes() {
    fetch('/programme/all/programmes')
        .then(response => response.json())
        .then(data => {
            const programmeSelect = document.getElementById('programmeName');
            //facultySelect.innerHTML = ''; // Clear existing options
            data.forEach(programme => {
                let option = document.createElement('option');
                option.value = programme.id;
                option.textContent = programme.programmeName;
                programmeSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading programmes:', error));
}

function loadAcademicYears() {
    fetch('/academic-year/all/academic-years')
        .then(response => response.json())
        .then(data => {
            const academicYearSelect = document.getElementById('academicYear');
            //facultySelect.innerHTML = ''; // Clear existing options
            data.forEach(academicYear => {
                let option = document.createElement('option');
                option.value = academicYear.id;
                option.textContent = academicYear.year;
                academicYearSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading academic years:', error));
}


// Initial call to load faculties when the DOM is fully loaded
document.addEventListener("DOMContentLoaded", function() {
    loadFaculties();
    loadDepartments();
    loadProgrammes();
    loadAcademicYears();
});

// // Fetches a list of faculties to fill faculty input control on the form
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
//
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
//
// // Fetches a list of programmes to fill programme input control on the form
// document.addEventListener("DOMContentLoaded", function() {
//     fetch('/programme/all/programmes')
//         .then(response => response.json())
//         .then(data => {
//             let select = document.getElementById('programmeName');
//             data.forEach(programme => {
//                 let option = document.createElement('option');
//                 option.value = programme.id;
//                 option.textContent = programme.programmeName;
//                 select.appendChild(option);
//             });
//         })
//         .catch(error => console.error('Error fetching faculties:', error));
// });

// Fetches a list of academic years to fill academic year input control on the form
// document.addEventListener("DOMContentLoaded", function() {
//     fetch('/academic-year/all/academic-years')
//         .then(response => response.json())
//         .then(data => {
//             let select = document.getElementById('academicYear');
//             data.forEach(academicYear => {
//                 let option = document.createElement('option');
//                 option.value = academicYear.id;
//                 option.textContent = academicYear.year;
//                 select.appendChild(option);
//             });
//         })
//         .catch(error => console.error('Error fetching academic years:', error));
// });

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
