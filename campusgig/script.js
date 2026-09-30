console.log("CampusGig loaded successfully!");
function showApplyMessage() {
    alert("Please log in or create a CampusGig account to apply for this gig.");
}
// Post Gig form
const postGigForm = document.getElementById("postGigForm");

if (postGigForm) {

    postGigForm.addEventListener("submit", function(event) {

        event.preventDefault();

        const title = document.getElementById("gigTitle").value.trim();
        const category = document.getElementById("gigCategory").value;
        const budget = document.getElementById("gigBudget").value;
        const deadline = document.getElementById("gigDeadline").value;
        const description = document.getElementById("gigDescription").value.trim();

        if (!title || !category || !budget || !deadline || !description) {

            alert("Please fill in all required fields.");

            return;
        }

        if (budget <= 0) {

            alert("Budget must be greater than ₹0.");

            return;
        }

        alert("Gig form validated successfully!");

    });

}
// Login form
const loginForm = document.getElementById("loginForm");

if (loginForm) {

    loginForm.addEventListener("submit", function(event) {

        event.preventDefault();

        const email = document.getElementById("loginEmail").value.trim();
        const password = document.getElementById("loginPassword").value;

        if (!email || !password) {
            alert("Please enter your email and password.");
            return;
        }

        alert("Login form validated successfully!");

    });

}
// Register form
const registerForm = document.getElementById("registerForm");

if (registerForm) {

    registerForm.addEventListener("submit", function(event) {

        event.preventDefault();

        const name = document.getElementById("registerName").value.trim();
        const email = document.getElementById("registerEmail").value.trim();
        const password = document.getElementById("registerPassword").value;
        const confirmPassword = document.getElementById("confirmPassword").value;

        if (!name || !email || !password || !confirmPassword) {
            alert("Please fill in all fields.");
            return;
        }

        if (password.length < 6) {
            alert("Password must contain at least 6 characters.");
            return;
        }

        if (password !== confirmPassword) {
            alert("Passwords do not match.");
            return;
        }

        alert("Registration form validated successfully!");

    });

}