

// Toggle between forms
document.getElementById('showImportForm').addEventListener('click', function() {
    document.getElementById('addForm').style.display = 'none';
    document.getElementById('importForm').style.display = 'block';
});

document.getElementById('cancelImport').addEventListener('click', function() {
    document.getElementById('importForm').style.display = 'none';
    document.getElementById('addForm').style.display = 'block';
});

$(document).ready(function() {
    $('#programmeTable').DataTable();
});


function validateForm() {
    const programmeId = document.getElementById('programmeId').value;
    const programmeName = document.getElementById('programmeName').value;
    const faculty = document.getElementById('faculty').value;
    const department = document.getElementById('department').value;
    const form = document.getElementById('addProgrammeForm')

    if (!programmeId || !programmeName || !faculty || !department) {
        alert("All fields are required.");
        return false; // Prevent form submission
    }

    if (isNaN(programmeId)) {
        alert("Programme ID must be a number")
        return false;
    }

    if (!/^[a-zA-Z\s]+$/.test(programmeName)) {
        alert("Programme name should only contain alphabets and spaces.");
        return false;
    }

        // form.action = "/programme/add"
        // form.method = "POST"
        // form.submit();
        alert("Programme added successfully");
        return true;

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

function loadTableData() {
    console.log("Loading table data");
    fetch('/programme/all') // Adjust the endpoint to match your actual data source
        .then(response => response.json())
        .then(data => {

            console.log("fetch data")
            // Check if data is an array
            if (!Array.isArray(data)) {
                console.error("Expected an array but got:", data);
                return;
            }
            const tableBody = document.querySelector('#programmeTable tbody');
            tableBody.innerHTML = ''; // Clear existing table rows

            data.forEach(programme => {
                const row = document.createElement('tr');
                // row.setAttribute('data-academic-year-id', department.id);
                row.innerHTML = `<td>${programme.programmeId}</td>
                    <td>${programme.programmeName}</td>
                    <td>${programme.faculty}</td>
                    <td>${programme.department}</td>
                    <td>${programme.dateAdded}</td>
                    <td>${programme.dateEdited}</td>
                    <td>
                        <button class="btn btn-sm btn-info" onclick="editFaculty(1)">Edit</button>
                        <button class="btn btn-sm btn-danger" th:onclick="'deleteProgramme(' + ${programme.programmeId} + ')'">Delete</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        })
        .catch(error => console.error('Error loading table data:', error));
}

function deleteProgramme(id) {
    if (confirm('Are you sure you want to delete this programme?')) {
        fetch(`/programme/delete/${id}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    alert("Programme deleted successfully")
                    loadTableData()
                    // location.reload();
                    // loadTableData(); // Reload table data
                } else {
                    alert('Failed to delete Programme');
                }
            });
    }
}


// Initial call to load faculties when the DOM is fully loaded
document.addEventListener("DOMContentLoaded", function() {
    loadFaculties();
    loadDepartments()
});