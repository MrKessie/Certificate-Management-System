function toggleSidebar() {
    var sidebar = document.getElementById("sidebar");
    var mainContent = document.getElementById("main-content");
    sidebar.classList.toggle("collapsed");
    mainContent.classList.toggle("collapsed");
}

function toggleSubmenu(element) {
    var submenu = element.querySelector(".submenu");
    submenu.style.display = submenu.style.display === "block" ? "none" : "block";
}



document.addEventListener("DOMContentLoaded", function () {
    const dropdownBtns = document.querySelectorAll(".dropdown-btn");

    dropdownBtns.forEach(btn => {
        btn.addEventListener("click", function () {
            this.classList.toggle("active");
            const submenu = this.nextElementSibling;
            if (submenu.style.display === "block") {
                submenu.style.display = "none";
            } else {
                submenu.style.display = "block";
            }
        });
    });
});


