document.addEventListener("DOMContentLoaded", function() {
    const editButton = document.querySelectorAll(".icon-btn")
    editButton.forEach(btn => {
        btn.addEventListener("click", function() {
            const id = this.getAttribute("data-id");
            fetch(`/academic-year/${id}?academicYearId=${id}`)
                .then(response => response.json())
                .then(data => {
                    // Populate the edit page controls with the retrieved data
                    document.getElementById("academicYearId").value = data.id;
                    document.getElementById("academicYear").value = data.academicYear;
                    // Show the edit page
                    fetch(`/academic-year/academic-year-edit`)
                        .then(response => response.json())
                        .then(data => {
                            // Redirect to the edit page
                            window.location.href = `/academic-year/academic-year-edit`;
                        });
                });
        });
    });
});