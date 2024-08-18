document.getElementById('togglePassword').addEventListener('click', function () {
    const passwordField = document.getElementById('password');
    const type = passwordField.getAttribute('type') === 'password' ? 'text' : 'password';
    passwordField.setAttribute('type', type);
    this.classList.toggle('fa-eye-slash');
});

async function changePassword() {
    const userId = document.getElementById('userId').value;
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (!userId || !newPassword || !confirmPassword) {
        alert('Please fill in all fields');
        return false;
    }

    if (newPassword !== confirmPassword) {
        alert('Passwords do not match');
        return false;
    }

    alert('Password change successful');
    return true;

}