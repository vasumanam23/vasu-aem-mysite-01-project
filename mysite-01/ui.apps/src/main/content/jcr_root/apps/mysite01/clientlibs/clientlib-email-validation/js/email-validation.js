(function (document, Granite, $) {
  "use strict";

  if (location.pathname.indexOf("/libs/granite/security/content/v2/usereditor.html/") !== 0) {
    return;
  }

  var EMAIL_SELECTOR = "input[name='./profile/email'], input[name='profile/email'], input.coral-Form-field.email";

  // --- Show inline error ---
  function showEmailError($email, message) {
    if ($email.length === 0) return;

    var el = $email[0];
    el.setCustomValidity(message || "Invalid email address format.");

    $email.attr("aria-invalid", "true");

    var $existing = $email.siblings(".server-validation-msg");
    if ($existing.length === 0) {
      $existing = $('<div class="server-validation-msg coral-Form-fieldinfo coral-Form-fieldinfo--error" role="alert"></div>');
      $email.after($existing);
    }
    $existing.text(message);

    $email.closest(".coral-Textfield, .coral-Form-field, .coral-Textfield-wrapper").addClass("is-invalid");

    if (typeof el.reportValidity === "function") {
      el.reportValidity();
    }
  }

  // --- Clear error once user fixes input ---
  function clearEmailError($email) {
    if ($email.length === 0) return;

    var el = $email[0];
    el.setCustomValidity("");

    $email.removeAttr("aria-invalid");
    $email.siblings(".server-validation-msg").remove();
    $email.closest(".coral-Textfield, .coral-Form-field, .coral-Textfield-wrapper").removeClass("is-invalid");
  }

  // --- Listen for user typing to reset error dynamically ---
  function bindLiveValidation() {
    $(document).on("input change", EMAIL_SELECTOR, function () {
      var $email = $(this);
      var value = $email.val();

      // simple regex for email format
      var isValid = /^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(value);

      if (isValid) {
        clearEmailError($email);
      } else {
        // do not set custom validity here — let server decide unless empty
        if (value && !isValid) {
          showEmailError($email, "Please enter a valid email address (e.g., name@domain.tld)");
        }
      }
    });
  }

  // --- Handle backend validation failure (400 from server) ---
  function handleFormAjaxFail(e, xhr, formArg) {
    var $form = formArg ? $(formArg) : $("#propertiesform");
    var $email = $form.find(EMAIL_SELECTOR);

    var msg = "Invalid email address format.";
    try {
      var hdr = xhr && (xhr.getResponseHeader("X-Reason") || xhr.getResponseHeader("X-Request-Message"));
      if (hdr) msg = hdr.trim();
    } catch (ignore) {}

    if ($email.length) {
      showEmailError($email, msg);
      e.preventDefault();
      e.stopImmediatePropagation();
      return false;
    }
  }

  // --- Bind AJAX fail events ---
  $(document).on("foundation-form-ajax-fail foundation-form-ajax-error", handleFormAjaxFail);
  document.addEventListener("foundation-form-ajax-fail", function (evt) {
    handleFormAjaxFail(evt, evt.detail && evt.detail.xhr, evt.target);
  }, true);

  $(document).ajaxError(function (_evt, jqxhr, settings) {
    if (settings && /\.rw\.userprops\.html/.test(settings.url)) {
      handleFormAjaxFail(_evt, jqxhr, $("#propertiesform"));
    }
  });

  // --- Activate live validation binding ---
  bindLiveValidation();

})(document, Granite, Granite.$);
