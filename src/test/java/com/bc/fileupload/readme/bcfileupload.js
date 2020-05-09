'use strict';

var bfuSingleUploadForm = document.querySelector('#singleUploadForm');
var bfuSingleFileUploadInput = document.querySelector('#singleFileUploadInput');
var bfuSingleFileUploadError = document.querySelector('#singleFileUploadError');
var bfuSingleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');

var bfuMultipleUploadForm = document.querySelector('#multipleUploadForm');
var bfuMultipleFileUploadInput = document.querySelector('#multipleFileUploadInput');
var bfuMultipleFileUploadError = document.querySelector('#multipleFileUploadError');
var bfuMultipleFileUploadSuccess = document.querySelector('#multipleFileUploadSuccess');

var bcfileupload = {
    
    addLoadEventForSingleFileUpload: function() {
        bcfileupload.addLoadEvent(bcfileupload.addSingleUploadEventListener());
    },
    
    addLoadEventForMultipleFileUpload: function() {
        bcfileupload.addLoadEvent(bcfileupload.addMultipleUploadEventListener());
    },
    
    addLoadEvent: function (func) {
//window.alert("#addLoadEvent. Function: "+func);    
        var oldonload = window.onload;
//window.alert("#addLoadEvent. OLD Function: "+oldonload);    
        if (typeof window.onload != 'function') {
            window.onload = func;
        }else{
            window.onload = function() {
                if (oldonload) {
                    oldonload();
                }
                func();
            }
        }
    },
    
    addMultipleUploadEventListener: function() {
        bfuMultipleUploadForm.addEventListener('submit', function(event){
            var files = bfuMultipleFileUploadInput.files;
            if(files.length === 0) {
                bfuMultipleFileUploadError.innerHTML = "Please select at least one file";
                bfuMultipleFileUploadError.style.display = "block";
            }
            bcfileupload.uploadMultipleFiles(files);
            event.preventDefault();
        }, true);
    },

    addSingleUploadEventListener: function() {
        bfuSingleUploadForm.addEventListener('submit', function(event){
            var files = bfuSingleFileUploadInput.files;
            if(files.length === 0) {
                bfuSingleFileUploadError.innerHTML = "Please select a file";
                bfuSingleFileUploadError.style.display = "block";
            }
            bcfileupload.uploadSingleFile(files[0]);
            event.preventDefault();
        }, true);
    },

    uploadSingleFile: function(file) {
        var formData = new FormData();
        formData.append("file", file);

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/uploadFile");

        xhr.onload = function() {
            console.log(xhr.responseText);
            var response = JSON.parse(xhr.responseText);
            if(xhr.status === 200) {
                bfuSingleFileUploadError.style.display = "none";
                bfuSingleFileUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
                bfuSingleFileUploadSuccess.style.display = "block";
            } else {
                bfuSingleFileUploadSuccess.style.display = "none";
                bfuSingleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
            }
        };

        xhr.send(formData);
    },

    uploadMultipleFiles: function(files) {
        var formData = new FormData();
        for(var index = 0; index < files.length; index++) {
            formData.append("files", files[index]);
        }

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/uploadFiles");

        xhr.onload = function() {
            console.log(xhr.responseText);
            var response = JSON.parse(xhr.responseText);
            if(xhr.status === 200) {
                bfuMultipleFileUploadError.style.display = "none";
                var content = "<p>All Files Uploaded Successfully</p>";
                for(var i = 0; i < response.length; i++) {
                    content += "<p>DownloadUrl : <a href='" + response[i].fileDownloadUri + "' target='_blank'>" + response[i].fileDownloadUri + "</a></p>";
                }
                bfuMultipleFileUploadSuccess.innerHTML = content;
                bfuMultipleFileUploadSuccess.style.display = "block";
            } else {
                bfuMultipleFileUploadSuccess.style.display = "none";
                bfuMultipleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
            }
        };

        xhr.send(formData);
    }
};
