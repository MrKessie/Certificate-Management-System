console.log("Academic year Script is loaded successfully");


//FUNCTION VALIDATE FORM CONTROLS
function validateForm() {
    console.log("Validate form");
    const academicYear = document.getElementById('academicYearInput').value;

    if (!academicYear) {
        alert("Academic year field is required.");
        return false; // Prevent form submission
    }

    // sendForm();
    alert("Academic year added successfully");
    return true;
}


//FUNCTION TO LOAD TABLE DATA AFTER DELETION
function loadTableData() {
    console.log("Loading table data");
    fetch('/academic-year/all') // Adjust the endpoint to match your actual data source
        .then(response => response.json())
        .then(data => {

            console.log("fetch data")
            // Check if data is an array
            if (!Array.isArray(data)) {
                console.error("Expected an array but got:", data);
                return;
            }
            const tableBody = document.querySelector('#academicYearTable tbody');
            tableBody.innerHTML = ''; // Clear existing table rows

            data.forEach(academicYear => {
                const row = document.createElement('tr');
                row.setAttribute('data-academic-year-id', academicYear.id);

                row.innerHTML = `
                    <td>${academicYear.id}</td>
                    <td>${academicYear.academicYear}</td>
                    <td>${academicYear.dateAdded}</td>
                    <td>${academicYear.dateEdited}</td>
                    <td>
                  <button class="btn btn-sm btn-info">Edit</button>
                  <button class="btn btn-sm btn-danger" th:onclick="'deleteAcademicYear(' + ${academicYear.id} + ')'">Delete</button>
                </td>
                `;
                tableBody.appendChild(row);
            });
        })
        .catch(error => console.error('Error loading table data:', error));
}


//FUNCTION TO DELETE ACADEMIC YEAR
function deleteAcademicYear(id) {
    if (confirm('Are you sure you want to delete this academic year?')) {
        // console.log("Confirmation");
        fetch(`/academic-year/delete/${id}`, {
            method: 'DELETE'
        })
            .then(response => {
                console.log("response");
                if (response.ok) {
                    alert("Academic Year deleted");
                    loadTableData();
                } else {
                    alert('Failed to delete Academic Year');
                }
            });
    }
}


//FUNCTION TO IMPORT ACADEMIC YEARS
function importAcademicYears() {
    const academicYearFile = document.getElementById('file');

    academicYearFile.addEventListener('change', (e) => {
        // Get the selected file
        const file = e.target.files[0];

        // Validate the file type and size
        if (file.type !== 'application/vnd.malformations-office document.spreadsheet.sheet') {
            alert('Please select an Excel file (.xlsx)');
            return false;
        }

        if (file.size > 10 * 1024 * 1024) {
            alert('File size exceeds 10MB');
            return false;
        }

        // Submit the form
        alert('Academic year imported successfully');
        return true;
        // document.getElementById('academicYearForm').submit();
    });
}

// $(document).ready(function() {
//     console.log("Validate form");
//
//
//     $('#submitBtn').click(function() {
//         const academicYear = $('#academicYear').val();
//
//         if (!academicYear) {
//             alert("Academic year field is required.");
//             return false; // Prevent form submission
//         }
//         const form = $('#academicYearForm');
//         form.attr('action', '/academic-year/add');
//         form.attr('method', 'POST');
//         form.submit();
//         alert("Academic Year added successfully")
//     });
//
//         $('#importBtn').click(function() {
//             console.log("Import button clicked");
//
//             const fileInput = $('#importFile')[0];
//             const file = fileInput.files[0];
//
//             if (fileInput.files.length === 0) {
//                 alert('Please select an Excel file to import.');
//                 return false;
//             }
//             if (file.type !== 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
//                 alert('Please select an Excel file (.xlsx)');
//                 return false;
//             }
//             if (file.size > 10 * 1024 * 1024) {
//                 alert('File size exceeds 10MB');
//                 return false;
//             }
//
//             console.log("All conditions passes");
//             const form = $('#academicYearForm')[0];
//             const formData = new FormData(form);
//
//             console.log("Form data prepared");
//
//             $.ajax({
//                 url: '/academic-year/import-academic-years',
//                 type: 'POST',
//                 data: formData,
//                 processData: false,
//                 contentType: false,
//                 success: function(response) {
//                     alert('Academic Year imported successfully');
//                     console.log("Import successful:", response);
//                 },
//
//                 error: function(xhr, status, error) {
//                     alert('An error occurred while importing academic year: ' + error);
//                     console.log("Error details:", xhr, status, error);
//                 }
//             });
//             console.log("Process finished successfully");
//         });
//
// });

function populateEditForm(id, year) {
    // Set the values of the form fields
    console.log("Populate edit form");
    document.getElementById('editAcademicYearId').value = id;
    document.getElementById('editAcademicYear').value = year;

    // Open the modal (optional if using data-toggle)
    $('#editModal').modal('show');
}



document.addEventListener('DOMContentLoaded', (event) => {
    $('#editModal').on('show.bs.modal', function (event) {
        const button = $(event.relatedTarget); // Button that triggered the modal
        const academicYearId = button.data('id'); // Extract info from data-* attributes
        const academicYear = button.data('year');

        // Update the modal's content.
        const modal = $(this);
        modal.find('.modal-body #editAcademicYearId').val(academicYearId);
        modal.find('.modal-body #editAcademicYear').val(academicYear);
    });
});

