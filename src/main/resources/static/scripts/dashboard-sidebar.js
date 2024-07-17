document.addEventListener("DOMContentLoaded", function () {
    const dropdownBtns = document.querySelectorAll(".dropdown-btn");
    const sidebarToggle = document.getElementById("sidebarToggle");
    const sidebar = document.querySelector(".sidebar");

    dropdownBtns.forEach(btn => {
        btn.addEventListener("click", function () {
            this.classList.toggle("active");
            const dropdownContent = this.nextElementSibling;
            if (dropdownContent.style.display === "block") {
                dropdownContent.style.display = "none";
            } else {
                dropdownContent.style.display = "block";
            }
        });
    });

    sidebarToggle.addEventListener("click", function () {
        sidebar.classList.toggle("collapsed");
    });
});


