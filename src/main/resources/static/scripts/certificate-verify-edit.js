$(document).ready(function() {
    $('#certificateVerifyTable').DataTable();
});

document.addEventListener('DOMContentLoaded', async () => {
    try {
        const response = await fetch('/certificate-verify/all');
        if (response.ok) {
            const certificatesVerified = await response.json();

            const tableBody = document.getElementById('certificateVerifyTableBody');
            tableBody.innerHTML = ''; // Clear any existing rows

            certificatesVerified.forEach(certificateVerified => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${certificateVerified.verificationId}</td>
                    <td>${certificateVerified.student.studentName}</td>
                    <td>${certificateVerified.userId.fullName}</td>
                    <td>${certificateVerified.employer}</td>
                    <td>${certificateVerified.organization}</td>
                    <td>${certificateVerified.dateVerified}</td>
                    <td>${certificateVerified.dateEdited}</td>
                    <td>
                    <button class="btn btn-sm btn-info"><i class="la la-pencil"></i></button>
                    <button class="btn btn-sm btn-danger" data-certificateVerified-id="${certificatesVerified.verificationId}"><i class="la la-trash-o"></button>
                    </td>
                `;
                tableBody.appendChild(row);
            });

            // Attach delete event listeners after populating the table
            attachDeleteListeners();
        } else {
            throw new Error('Failed to fetch Certificate Verification data');
        }
    } catch (error) {
        await Swal.fire({
            icon: 'error',
            title: 'Data Fetch Error',
            text: 'There was an error fetching Certificate Verification data. Please try again.'
        });
    }
});


function attachDeleteListeners() {
    document.querySelectorAll('.btn-danger').forEach(button => {
        button.addEventListener('click', async (event) => {
            const verificationId = event.target.dataset.verificationId; // Get the ID from the data attribute

            if (!verificationId || isNaN(verificationId)) {
                await Swal.fire({
                    icon: 'error',
                    title: 'Invalid ID',
                    text: 'Invalid Verification ID. Please try again.'
                });
                return;
            }

            const result = await Swal.fire({
                title: 'Are you sure?',
                text: 'Are you sure you want to delete this Verification details?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes, delete it!',
                cancelButtonText: 'No, cancel!',
                reverseButtons: true
            });

            if (result.isConfirmed) {
                try {
                    const response = await fetch(`/certificate-verify/delete/${verificationId}`, {
                        method: 'DELETE',
                    });

                    if (response.ok) {
                        await Swal.fire({
                            icon: 'success',
                            title: 'Deleted',
                            text: 'Certificate Verification details has been deleted successfully!'
                        });

                        // Remove the row from the table
                        event.target.closest('tr').remove();
                    } else {
                        const errorText = await response.text();
                        await Swal.fire({
                            icon: 'error',
                            title: 'Deletion Error',
                            text: errorText || 'There was an error deleting the Certificate Verification details. Please try again.'
                        });
                    }
                } catch (error) {
                    await Swal.fire({
                        icon: 'error',
                        title: 'Submission Error',
                        text: 'There was an error with the submission. Please try again.'
                    });
                }
            } else {
                // User canceled deletion
                await Swal.fire({
                    icon: 'info',
                    title: 'Cancelled',
                    text: 'Certificate Verification details deletion was cancelled.'
                });
            }
        });
    });
}