// CampusGig - Main JavaScript

document.addEventListener("DOMContentLoaded", function () {

    console.log("CampusGig JavaScript loaded");

    // POST GIG FORM
    const postGigForm = document.getElementById("postGigForm");

    if (postGigForm) {
        postGigForm.addEventListener("submit", function () {
            console.log("Post Gig form submitted");
        });
    }

    // REGISTER FORM
    const registerForm = document.getElementById("registerForm");

    if (registerForm) {

        registerForm.addEventListener("submit", function (event) {

            event.preventDefault();

            const name =
                document.getElementById("registerName").value.trim();

            const email =
                document.getElementById("registerEmail").value.trim();

            const password =
                document.getElementById("registerPassword").value;

            const confirmPassword =
                document.getElementById("confirmPassword").value;

            if (!name || !email || !password) {
                alert("Please fill all fields.");
                return;
            }

            if (password !== confirmPassword) {
                alert("Passwords do not match.");
                return;
            }

            const data = new URLSearchParams();

            data.append("name", name);
            data.append("email", email);
            data.append("password", password);

            fetch("/CampusGig/register", {
                method: "POST",
                headers: {
                    "Content-Type":
                        "application/x-www-form-urlencoded"
                },
                body: data.toString()
            })
            .then(response => response.text())
            .then(html => {

                document.open();
                document.write(html);
                document.close();

            })
            .catch(error => {

                console.error(error);

                alert("Registration failed.");
            });
        });
    }
});