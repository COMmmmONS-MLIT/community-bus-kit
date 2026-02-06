
    document.addEventListener("DOMContentLoaded", function () {
        const uploadButton = document.getElementById("btnUploadAjax");
        if (!uploadButton) return;

        uploadButton.addEventListener("click", function () {
            const fileInput = document.getElementById("fileInput");
            const errorBox = document.getElementById("uploadErrorBox");
            const errorList = document.getElementById("uploadErrorList");

            function showErr(messages) {
                if (errorList) {
                    errorList.innerHTML = "";
                    (messages || []).forEach(function (msg) {
                        const li = document.createElement("li");
                        li.textContent = msg;
                        errorList.appendChild(li);
                    });
                    if (errorBox) errorBox.style.display = "";
                } else {
                    alert((messages || []).join("\n"));
                }
                try {
                    window.scrollTo({ top: 0, behavior: "smooth" });
                } catch (e) {}
            }

            if (!(fileInput && fileInput.files && fileInput.files.length)) {
                showErr(["ファイルを選択してください。"]);
                return;
            }

            const file = fileInput.files[0];
            const selectedFileName = document.getElementById("selectedFileName");
            const uploadFileName = document.getElementById("uploadFileName");

            if (selectedFileName) selectedFileName.value = file.name;
            if (uploadFileName) uploadFileName.value = file.name;

            const formData = new FormData();
            formData.append("excel_file", file, file.name);
            formData.append("uploadFileName", file.name);

            const token = (document.querySelector('meta[name="_csrf"]') || {}).content ||
                (document.querySelector('input[name="_csrf"]') || {}).value;
            const header = (document.querySelector('meta[name="_csrf_header"]') || {}).content || "X-CSRF-TOKEN";

            uploadButton.disabled = true;

            $.ajax({
                url: "/operation/lineChargesInfoReport/uploadExcel",
                type: "POST",
                data: formData,
                processData: false,
                contentType: false,
                dataType: "json",
                timeout: 120000,
                beforeSend: function (xhr) {
                    if (token) {
                        xhr.setRequestHeader(header, token);
                    }
                },
                success: function (response) {
                    if (response && response.success) {
                        if (errorBox) errorBox.style.display = "none";
                        if (errorList) errorList.innerHTML = "";
                        alert(response.message || "アップロードが完了しました。");
                        if (response.redirect) {
                            location.reload();
                            return;
                        }
                    } else {
                        const errs = (response && response.errors);
                        showErr(errs);
                    }
                },
                error: function (xhr) {
                    const errs = (xhr && xhr.responseJSON && xhr.responseJSON.errors);
                    showErr(errs);
                },
                complete: function () {
                    uploadButton.disabled = false;
                }
            });
        });
    });
