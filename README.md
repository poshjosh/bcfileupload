### Spring Boot based application for uploading files ###

### Usage ###

- Extend the following controller class:

`src/test/java/com/bc/fileupload/FileUploadAndDownloadController.java`

- Add the `@RestController` annotation to the subclass.

- Add the following resources to `src/main/resources/static/`

    - `src/test/java/com/bc/fileupload/readme/main.css`

    - `src/test/java/com/bc/fileupload/readme/main.js`

    - `src/test/java/com/bc/fileupload/readme/uploadfile.html`

    - `src/test/java/com/bc/fileupload/readme/uploadfiles.html`

- Build the application

- Browse to `http://localhost:<server-port>/uploadfile.html` to upload a single file

- Browse to `http://localhost:<server-port>/uploadfiles.html` to upload multiple files

