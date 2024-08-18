console.log("Student script loaded");

$(document).ready(function() {
    $('#studentTable').DataTable();
});

// Toggle between forms
document.getElementById('showImportForm').addEventListener('click', function() {
    document.getElementById('addStudentForm').style.display = 'none';
    document.getElementById('importForm').style.display = 'block';
});

document.getElementById('cancelImport').addEventListener('click', function() {
    document.getElementById('importForm').style.display = 'none';
    document.getElementById('addStudentForm').style.display = 'block';
});


function validateForm() {
    const studentId = document.getElementById('studentId').value;
    const surname = document.getElementById('surname').value;
    const otherNames = document.getElementById('otherNames').value;
    const faculty = document.getElementById('faculty').value;
    const department = document.getElementById('department').value;
    const programme = document.getElementById('programme').value;
    const academicYear = document.getElementById('academicYear').value;

    if (studentId === "" || surname === "" || otherNames === "" || faculty === "" || department === "" || programme === "" || academicYear === "") {
        alert("All fields are required.");
        return false; // Prevent form submission
    }

    alert("Student added successfully");
    return true;
}


function loadFaculties() {
    console.log("Loading faculties...")
    fetch('/faculty/all/faculties')
        .then(response => response.json())
        .then(data => {
            const facultySelect = document.getElementById('faculty');
            //facultySelect.innerHTML = ''; // Clear existing options
            data.forEach(faculty => {
                let option = document.createElement('option');
                option.value = faculty.facultyId;
                option.textContent = faculty.facultyName;
                facultySelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading faculties:', error));
}

function loadDepartments() {
    console.log("Loading departments...")
    fetch('/department/all/departments')
        .then(response => response.json())
        .then(data => {
            const departmentSelect = document.getElementById('department');
            //facultySelect.innerHTML = ''; // Clear existing options
            data.forEach(department => {
                let option = document.createElement('option');
                option.value = department.departmentId;
                option.textContent = department.departmentName;
                departmentSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading faculties:', error));
}

function loadProgrammes() {
    console.log("Loading prog...")
    fetch('/programme/all/programmes')
        .then(response => response.json())
        .then(data => {
            const departmentSelect = document.getElementById('programme');
            //facultySelect.innerHTML = ''; // Clear existing options
            data.forEach(programme => {
                let option = document.createElement('option');
                option.value = programme.programmeId;
                option.textContent = programme.programmeName;
                departmentSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading programmes:', error));
}

function loadAcademicYears() {
    console.log("Loading academic...")
    fetch('/academic-year/all/academic-years')
        .then(response => response.json())
        .then(data => {
            const departmentSelect = document.getElementById('academicYear');
            //facultySelect.innerHTML = ''; // Clear existing options
            data.forEach(academicYear => {
                let option = document.createElement('option');
                option.value = academicYear.id;
                option.textContent = academicYear.academicYear;
                departmentSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading faculties:', error));
}

function deleteStudent(id) {
    if (confirm('Are you sure you want to delete this Student?')) {
        fetch(`/student/delete/${id}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    const row = document.querySelector(`tr[data-faculty-faculty-id="${id}"]`);
                    // if (row) {
                    row.remove();
                    // }
                    alert("Student deleted")
                    location.reload();
                    // loadTableData(); // Reload table data
                } else {
                    alert('Failed to delete Student');
                }
            });
    }
}


// Initial call to load faculties when the DOM is fully loaded
document.addEventListener("DOMContentLoaded", function() {
    console.log("Student script loaded")
    console.log("DOM fully loaded");
    loadFaculties()
    loadDepartments()
    loadProgrammes();
    loadAcademicYears();
});