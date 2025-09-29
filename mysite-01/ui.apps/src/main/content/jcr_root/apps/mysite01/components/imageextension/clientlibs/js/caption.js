(function (document, $) {
  "use strict";

  const captionSelector = 'input[name="./caption"]';
  const checkboxSelector = 'coral-checkbox[name="./damCaption"]';
  const fileRefSelector = 'input[name="./fileReference"]';
  const inheritSelector = 'coral-checkbox[name="./imageFromPageImage"]'; // Adjust if your inherit checkbox name differs

  /**
   * Fetch caption from DAM metadata
   */
  function fetchCaptionFromDAM(fileReference, $caption) {
    const metadataUrl = fileReference + "/jcr:content/metadata.json";

    $.getJSON(metadataUrl)
      .done(function (data) {
        if (data && data["dc:description"]) {
          $caption.val(data["dc:description"]).trigger("change");
        } else {
          console.warn("dc:description not found in DAM metadata");
        }
      })
      .fail(function () {
        console.error("Failed to fetch DAM metadata");
      });
  }

  /**
   * Enable/Disable caption field and fetch DAM caption if needed
   */
  function toggleCaptionFieldState() {
    const $caption = $(captionSelector);
    const $checkbox = $(checkboxSelector);
    const $fileRef = $(fileRefSelector);

    if (!$caption.length || !$checkbox.length || !$fileRef.length) return;

    const isChecked = $checkbox[0].checked;

    if (isChecked) {
      $caption.prop("disabled", true);

      // Wait briefly to ensure fileReference updates
      setTimeout(() => {
        const fileReference = $fileRef.val();
        if (!fileReference) {
          console.warn("File reference not found");
          return;
        }
        fetchCaptionFromDAM(fileReference, $caption);
      }, 300);
    } else {
      $caption.prop("disabled", false);
    }
  }

  /**
   * Observe changes to fileReference using MutationObserver (for inheritance updates)
   */
  function observeFileReference() {
    const fileRefInput = document.querySelector(fileRefSelector);
    if (!fileRefInput) return;

    // Fallback to polling if MutationObserver not available
    if (typeof MutationObserver === "undefined") {
      let lastVal = fileRefInput.value;
      setInterval(() => {
        if (fileRefInput.value !== lastVal) {
          lastVal = fileRefInput.value;
          toggleCaptionFieldState();
        }
      }, 300);
      return;
    }

    const observer = new MutationObserver(() => {
      toggleCaptionFieldState();
    });

    observer.observe(fileRefInput, {
      attributes: true,
      childList: true,
      subtree: true,
      characterData: true,
    });
  }

  /**
   * Initialize on dialog load
   */
  $(document).on("foundation-contentloaded", function () {
    toggleCaptionFieldState();
    observeFileReference();

    // When "Get caption from DAM" checkbox changes
    $(document).on("change", checkboxSelector, function () {
      toggleCaptionFieldState();
    });

    // When fileReference manually changed (DAM selection)
    $(document).on("change", fileRefSelector, function () {
      toggleCaptionFieldState();
    });

    // When "Inherit featured image from page" checkbox toggled
    $(document).on("change", inheritSelector, function () {
      setTimeout(toggleCaptionFieldState, 300);
    });

    // Ensure caption is enabled before submit
    $(document).on("click", ".cq-dialog-submit", function () {
      $(captionSelector).prop("disabled", false);
    });
  });

})(document, Granite.$);