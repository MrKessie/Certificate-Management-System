$(document).ready(function() {
    $('#certificateIssueTable').DataTable();
});

document.addEventListener('DOMContentLoaded', async () => {
    try {
        const response = await fetch('/certificate-issue/all');
        if (response.ok) {
            const certificatesIssued = await response.json();

            const tableBody = document.getElementById('certificateIssueTableBody');
            tableBody.innerHTML = ''; // Clear any existing rows

            certificatesIssued.forEach(certificateIssued => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${certificateIssued.issueId}</td>
                    <td>${certificateIssued.studentId.studentName}</td>
                    <td>${certificateIssued.userId.fullName}</td>
                    <td>${certificateIssued.collectorName}</td>
                    <td>${certificateIssued.dateIssued}</td>
                    <td>${certificateIssued.dateEdited}</td>
                    <td>
                    <button class="btn btn-sm btn-info"><i class="la la-pencil"></i></button>
                    <button class="btn btn-sm btn-danger" data-certificateIssue-id="${certificateIssued.issueId}"><i class="la la-trash-o"></button>
                    </td>
                `;
                tableBody.appendChild(row);
            });

            // Attach delete event listeners after populating the table
            attachDeleteListeners();
        } else {
            throw new Error('Failed to fetch Certificate Issue data');
        }
    } catch (error) {
        await Swal.fire({
            icon: 'error',
            title: 'Data Fetch Error',
            text: 'There was an error fetching Certificate Issue data. Please try again.'
        });
    }
});


function attachDeleteListeners() {
    document.querySelectorAll('.btn-danger').forEach(button => {
        button.addEventListener('click', async (event) => {
            const issueId = event.target.dataset.issueId; // Get the ID from the data attribute

            if (!issueId || isNaN(issueId)) {
                await Swal.fire({
                    icon: 'error',
                    title: 'Invalid ID',
                    text: 'Invalid Issued ID. Please try again.'
                });
                return;
            }

            const result = await Swal.fire({
                title: 'Are you sure?',
                text: 'Are you sure you want to delete this Issued details?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes, delete it!',
                cancelButtonText: 'No, cancel!',
                reverseButtons: true
            });

            if (result.isConfirmed) {
                try {
                    const response = await fetch(`/certificate-issue/delete/${issueId}`, {
                        method: 'DELETE',
                    });

                    if (response.ok) {
                        await Swal.fire({
                            icon: 'success',
                            title: 'Deleted',
                            text: 'Certificate Issued details has been deleted successfully!'
                        });

                        // Remove the row from the table
                        event.target.closest('tr').remove();
                    } else {
                        const errorText = await response.text();
                        await Swal.fire({
                            icon: 'error',
                            title: 'Deletion Error',
                            text: errorText || 'There was an error deleting the Certificate Issued details. Please try again.'
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
                    text: 'Certificate Issued details deletion was cancelled.'
                });
            }
        });
    });
}