// document.getElementById('togglePassword').addEventListener('click', function () {
//     const passwordField = document.getElementById('password');
//     const type = passwordField.getAttribute('type') === 'password' ? 'text' : 'password';
//     passwordField.setAttribute('type', type);
//     this.classList.toggle('fa-eye-slash');
// });

// async function changePassword() {
//     const userId = document.getElementById('userId').value;
//     const newPassword = document.getElementById('newPassword').value;
//     const confirmPassword = document.getElementById('confirmPassword').value;
//
//     if (!userId || !newPassword || !confirmPassword) {
//         alert('Please fill in all fields');
//         return false;
//     }
//
//     if (newPassword !== confirmPassword) {
//         alert('Passwords do not match');
//         return false;
//     }
//
//     alert('Password change successful');
//     return true;
//
// }

document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const showAlert = urlParams.get('showAlert');
    const alertMessage = urlParams.get('alertMessage');

    if (showAlert === 'true') {
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: alertMessage || 'An error occurred'
        });
    }
});

document.getElementById('togglePassword').addEventListener('click', function() {
    const passwordInput = document.getElementById('password');
    const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
    passwordInput.setAttribute('type', type);

    // Toggle eye icon
    this.classList.toggle('fa-eye');
    this.classList.toggle('fa-eye-slash');
});