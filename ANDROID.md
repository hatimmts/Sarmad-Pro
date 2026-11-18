# Sarmad Specimen Studio (Android)

The `app/` module is a native Android shell around the audited `specimen.html` workflow. Gradle copies the root workflow files into the APK's private assets at build time; the app runs offline in a `WebView` and declares no network permission. This keeps the safety behavior in one source of truth while producing an Android package.

## Build

With Android SDK, JDK 17, and Gradle available:

```sh
gradle :app:assembleDebug
```

The current repository environment has no Android SDK or Gradle installation, so APK compilation cannot be run here. Static validation still checks the Java/XML/Gradle source and the existing web workflow checks remain applicable.

The native wrapper ships exactly the safe workflow: generated synthetic samples, marked archival image guard, two local specimen sides, bounded crop/perspective/brightness controls, descriptive OCR notes, and the non-dismissible `SPECIMEN / NOT LEGAL TENDER / NOT FOR CIRCULATION` banner. It does not implement currency reproduction, alignment, authenticity enhancement, denomination, or serial-number editing.
