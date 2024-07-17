function validateForm() {
    var academicYear = document.getElementById("academicYear").value;
    // var facultyName = document.getElementById("facultyName").value;

    if (academicYear === "") {
        alert("Please fill in all the fields.");
        return false; // Prevent form submission
    }

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