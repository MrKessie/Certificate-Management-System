document.addEventListener("DOMContentLoaded", function () {
    const profileDropdownBtn = document.getElementById("profileDropdownBtn");
    const profileDropdownMenu = document.getElementById("profileDropdownMenu");

    profileDropdownBtn.addEventListener("click", function () {
        profileDropdownMenu.style.display = profileDropdownMenu.style.display === "block" ? "none" : "block";
    });

    // Hide dropdown menu when clicking outside
    document.addEventListener("click", function (event) {
        if (!profileDropdownBtn.contains(event.target)) {
            profileDropdownMenu.style.display = "none";
        }
    });
});