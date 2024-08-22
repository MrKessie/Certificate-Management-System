$('#showVerifyForm').on('click', function() {
    $('#getCertificateFormContainer').hide();
    $('#verifyCertificateFormContainer').show();
});

$('#showGetForm').on('click', function() {
    $('#verifyCertificateFormContainer').hide();
    $('#getCertificateFormContainer').show();
});



document.getElementById("getCertificateForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    const studentId = document.getElementById("verifyStudentId").value ;
    const employer = document.getElementById("employer").value;
    const organization = document.getElementById("organization").value
    const resultsContainer = document.getElementById("resultsGet");
    const errorContainer = document.getElementById("errorGet");
    const getBtn = document.getElementById("getBtn")

    if (!studentId || !employer || !organization) {
        await Swal.fire({
            icon: 'warning',
            title: 'Required Fields',
            text: 'All fields are required.'
        });
        return;
    }

    if (isNaN(studentId)) {
        await Swal.fire({
            icon: 'warning',
            title: 'Invalid Student ID',
            text: 'Enter a valid Student ID.'
        });
        return;
    }

    if (!/^[a-zA-Z\s]+$/.test(employer)) {
        await Swal.fire({
            icon: 'warning',
            title: 'Invalid Employer Name',
            text: 'Enter a valid Employer Name'
        });
        return;
    }

    if (!/^[a-zA-Z\s]+$/.test(organization)) {
        await Swal.fire({
            icon: 'warning',
            title: 'Invalid Organization Name',
            text: 'Enter a valid Organization Name'
        });
        return;
    }

    const formData = new FormData();
    formData.append('studentId', studentId);
    formData.append('employer', employer);
    formData.append('organization', organization);

    Swal.fire({
        title: 'Processing...',
        text: 'Please wait while we verify the certificate.',
        allowOutsideClick: false,
        allowEscapeKey: false,
        showConfirmButton: false,
        willOpen: () => {
            Swal.showLoading();
        }
    });

    fetch(`/certificate/verify/student/${studentId}?employer=${employer}&organization=${organization}`, {method: 'GET'})
        .then(response => response.json())
        .then(data => {
            if (data.exists) {
                Swal.close();
                // Populate the results if the certificate exists
                document.getElementById("statusGet").textContent = "Certificate Found";
                document.getElementById("studentIdGet").textContent = studentId;
                document.getElementById("nameGet").textContent = data.name;
                document.getElementById("programmeGet").textContent = data.programme.programmeName;
                document.getElementById("departmentGet").textContent = data.department.departmentName;
                document.getElementById("academicYearGet").textContent = data.academicYear.academicYear;
                document.getElementById("classHonoursGet").textContent = data.classHonours;
                document.getElementById("viewCertificateGet").setAttribute("href", data.viewLink);
                // console.log("View Link: " + data.viewLink);
                resultsContainer.classList.remove("hidden");
                errorContainer.textContent = "";
            } else {
                Swal.close();
                // Display "N/A" if the certificate does not exist
                document.getElementById("statusGet").textContent = "N/A";
                document.getElementById("studentIdGet").textContent = "N/A";
                document.getElementById("nameGet").textContent = "N/A";
                document.getElementById("programmeGet").textContent = "N/A";
                document.getElementById("departmentGet").textContent = "N/A";
                document.getElementById("academicYearGet").textContent = "N/A";
                document.getElementById("classHonoursGet").textContent = "N/A";
                document.getElementById("viewCertificateGet").removeAttribute("href");
                resultsContainer.classList.remove("hidden");
                errorContainer.textContent = "Certificate not found.";
            }
        })
        .catch(error => {
            console.error('Error:', error);
            errorContainer.textContent = "An error occurred while fetching the certificate.";
        });
});