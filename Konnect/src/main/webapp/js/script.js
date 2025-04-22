document.addEventListener("DOMContentLoaded", () => {
  // Alert dismissal
  const alertElements = document.querySelectorAll(".alert")
  alertElements.forEach((alert) => {
    const closeButton = alert.querySelector(".alert-close")
    if (closeButton) {
      closeButton.addEventListener("click", () => {
        alert.style.opacity = "0"
        setTimeout(() => {
          alert.style.display = "none"
        }, 300)
      })
    }
    // Auto-dismiss success/info messages after a few seconds
    if (alert.classList.contains("alert-success") || alert.classList.contains("alert-info")) {
      setTimeout(() => {
        if (alert.style.display !== "none") {
          alert.style.opacity = "0"
          setTimeout(() => (alert.style.display = "none"), 300)
        }
      }, 5000)
    }
  })

  // Form validation
  const formsToValidate = document.querySelectorAll("form.needs-validation")
  formsToValidate.forEach((form) => {
    form.addEventListener("submit", (event) => {
      let isValid = true
      const requiredInputs = form.querySelectorAll("[required]")

      requiredInputs.forEach((input) => {
        input.setCustomValidity("")
        input.classList.remove("is-invalid")

        if (!input.value.trim()) {
          isValid = false
          input.setCustomValidity("This field is required.")
          input.classList.add("is-invalid")
        }

        if (input.type === "email" && input.value.trim() && !validateEmail(input.value.trim())) {
          isValid = false
          input.setCustomValidity("Please enter a valid email address.")
          input.classList.add("is-invalid")
        }

        if (input.id === "confirmPassword") {
          const passwordInput = form.querySelector("#password")
          if (passwordInput && input.value !== passwordInput.value) {
            isValid = false
            input.setCustomValidity("Passwords do not match.")
            input.classList.add("is-invalid")
          }
        }
      })

      if (!isValid) {
        event.preventDefault()
        event.stopPropagation()

        const firstInvalid = form.querySelector(".is-invalid")
        if (firstInvalid) {
          firstInvalid.focus()
          firstInvalid.scrollIntoView({ behavior: "smooth", block: "center" })
        }

        let errorSummary = form.querySelector(".form-error-summary")
        if (!errorSummary) {
          errorSummary = document.createElement("div")
          errorSummary.className = "alert alert-danger form-error-summary"
          errorSummary.setAttribute("role", "alert")
          form.insertBefore(errorSummary, form.firstChild)
        }
        errorSummary.textContent = "Please correct the errors highlighted below."
      } else {
        const errorSummary = form.querySelector(".form-error-summary")
        if (errorSummary) {
          errorSummary.remove()
        }

        const submitButton = form.querySelector('button[type="submit"]')
        if (submitButton) {
          submitButton.disabled = true
          submitButton.innerHTML = '<span class="spinner"></span> Processing...'
        }
      }
    })
  })

  // Email validation function
  function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    return re.test(String(email).toLowerCase())
  }

  // Confirm Deletions
  const deleteButtons = document.querySelectorAll(".confirm-delete")
  deleteButtons.forEach((button) => {
    button.addEventListener("click", (event) => {
      const message = button.getAttribute("data-confirm-message") || "Are you sure you want to delete this item?"
      if (!confirm(message)) {
        event.preventDefault()
      }
    })
  })

  // Chat Auto Scroll to Bottom
  const chatMessagesContainer = document.querySelector(".chat-messages")
  if (chatMessagesContainer) {
    chatMessagesContainer.scrollTop = chatMessagesContainer.scrollHeight
  }

  // Chat Textarea Auto-Resize
  const chatTextarea = document.querySelector(".chat-input textarea")
  if (chatTextarea) {
    const initialHeight = chatTextarea.scrollHeight
    chatTextarea.style.height = initialHeight + "px"

    chatTextarea.addEventListener("input", function () {
      this.style.height = "auto"
      this.style.height = this.scrollHeight + "px"
    })

    // Focus the textarea when chat is opened
    chatTextarea.focus()
  }

  // Add smooth transitions for hover effects
  document.querySelectorAll(".btn, .card, .stat-card").forEach((element) => {
    element.style.transition = "all 0.2s ease"
  })
})
