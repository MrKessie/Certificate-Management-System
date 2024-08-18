console.log("Department script loaded")

// Initialize DataTable
$(document).ready(function() {
    $('#departmentTable').DataTable();
});

// Toggle between forms
document.getElementById('showImportForm').addEventListener('click', function() {
    document.getElementById('addForm').style.display = 'none';
    document.getElementById('importForm').style.display = 'block';
});

document.getElementById('cancelImport').addEventListener('click', function() {
    document.getElementById('importForm').style.display = 'none';
    document.getElementById('addForm').style.display = 'block';
});


function validateForm() {
    const departmentId = document.getElementById('departmentId').value;
    const departmentName = document.getElementById('departmentName').value;
    const faculty = document.getElementById('faculty').value;
    const form = document.getElementById('departmentForm');

    if (!departmentId || !departmentName || !faculty) {
        alert("All fields are required.");
        return false; // Prevent form submission
    }

    if (isNaN(departmentId)) {
        alert("Department ID must be a number.");
        return false;
    }

    if (!/^[a-zA-Z\s]+$/.test(departmentName)) {
        alert("Department name can only contain letters and spaces.");
        return false;
    }

    // form.action = "/department/add"
    // form.method = "POST"
    // form.submit();
    alert("Department added successfully");
    return true;
}


//FUNCTION IMPORT FACULTIES
function importDepartments() {
    console.log('Importing');
    const departmentFile = document.getElementById('file');
    const form = document.getElementById('departmentForm')

    if (departmentFile.files.length === 0) {
        alert("Select a file");
        return false;
    }

    departmentFile.addEventListener('change', function(event) {
        const file = event.target.files[0];

        if (file) {
            const validMimeTypes = [
                'application/vnd.ms-excel',
                'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
            ];

            const fileName = file.name;
            const fileExtension = fileName.split('.').pop().toLowerCase();

            if (!validMimeTypes.includes(file.type) && !['xls', 'xlsx'].includes(fileExtension)) {
                alert('Please upload a valid Excel file.');
                // Clear the file input value to prevent form submission with invalid file
                event.target.value = '';
            }
        }

    });
    form.actiion = "/department/import-departments"
    form.method = 'POST'
    form.submit()
    alert('Department imported successfully');
    return true;
}

function loadTableData() {
    fetch('/department/all')
        .then(response => response.json())
        .then(data => {
            if (!Array.isArray(data)) {
                console.error("Expected an array but got:", data);
                return;
            }
            const tableBody = document.querySelector('#departmentTable tbody');
            tableBody.innerHTML = '';
            data.forEach(department => {
                const row = document.createElement('tr');
                row.innerHTML = `<td>${department.departmentId}</td> 
                             <td>${department.departmentName}</td>
                             <td>${department.faculty}</td>
                             <td>${department.dateAdded}</td>
                             <td>${department.dateEdited}</td>
                             <td>
                                <button class="btn btn-sm btn-info" onclick="editFaculty(1)">Edit</button>
                                <button class="btn btn-sm btn-danger" th:onclick="'deleteDepartment(' + ${department.departmentId} + ')'">Delete</button>
                             </td>`;
                tableBody.appendChild(row);
            });
        })
        .catch(error => console.error('Error loading table data:', error));

}

// function loadTableData() {
//     console.log("Loading table data");
//     fetch('/department/all') // Adjust the endpoint to match your actual data source
//         .then(response => response.json())
//         .then(data => {
//
//             console.log("fetch data")
//             // Check if data is an array
//             if (!Array.isArray(data)) {
//                 console.error("Expected an array but got:", data);
//                 return;
//             }
//             const tableBody = document.querySelector('#departmentTable tbody');
//             tableBody.innerHTML = ''; // Clear existing table rows
//
//             console.log("for each data")
//             data.forEach(department => {
//                 const row = document.createElement('tr');
//                 // row.setAttribute('data-academic-year-id', department.id);
//                 row.innerHTML = `<td>${department.id}</td>
//                                  <td>${department.departmentId}</td>
//                                  <td>${department.departmentName}</td>
//                                  <td>${department.facultyId}</td>
//                                  <td>${department.dateAdded}</td>
//                                  <td>${department.dateEdited}</td>
//                                  <td>
//                                    <button class="btn btn-sm btn-info" th:onclick="'editDepartment(' + ${department.id} + ')'">Edit</button>
//                                    <button class="btn btn-sm btn-danger" th:onclick="'deleteDepartment(' + ${department.id} + ')'">Delete</button>
//                                  </td>`;
//                 tableBody.appendChild(row);
//             });
//         })
//         .catch(error => console.error('Error loading table data:', error));
// }

function deleteDepartment(id) {
    if (confirm('Are you sure you want to delete this Department?')) {
        fetch(`/department/delete/${id}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    alert("Department deleted")
                    loadTableData(); // Reload table data
                } else {
                    alert('Failed to delete Department');
                }
            });
    }
}

function loadFaculties() {
    console.log("Loading")
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


// Initial call to load faculties when the DOM is fully loaded
document.addEventListener("DOMContentLoaded", function() {
    loadFaculties();
});
