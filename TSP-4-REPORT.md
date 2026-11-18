# TSP-4 implementation report

## Files and implemented pieces

- `app/src/main/java/pro/sarmad/specimen/MainActivity.kt`: Kotlin 17 Activity with Storage Access Framework `ACTION_OPEN_DOCUMENT` for local PDF/image/document selection. It opens the selected stream locally for validation and keeps only an in-memory URI; there is no upload or persistence.
- `app/src/main/java/pro/sarmad/specimen/MainActivity.kt`: native `PrintManager` integration using a custom `PrintDocumentAdapter`. `onLayout`, `onWrite`, `PageRanges`, and `CancellationSignal` are handled. The UI offers Simplex, Duplex Long Edge, and Duplex Short Edge as requested attributes.
- `specimen.html` and `specimen.css`: existing safe workflow remains the editable two-side specimen UI; camera capture/import uses Android's `capture="environment"` hint and printing retains the permanent bilingual watermark.
- `app/build.gradle` and root `build.gradle`: compileSdk 34, minSdk 24, targetSdk 34, Kotlin JVM target 17. The launcher is Kotlin; the former Java shell is retained only as a non-launcher compatibility source.
- `TSP-4-REPORT.md`: this report and the integration blockers.

## Android Print Framework limitation

The public framework lets an app submit requested `PrintAttributes` and the system print picker/printer service decides whether a device supports duplex. The app cannot enumerate or guarantee printer capabilities before that picker. The selected attributes are not treated as proof of a printer's physical capability; if a selected/returned configuration has no duplex mode, the system path remains simplex and the app makes no duplex claim. The printable output always carries the specimen watermark.

## Sarmad reference and blockers

No `r350` branch, tag, or remote ref exists in the checked-out repository or `origin` (`git ls-remote` returned no matching ref). No authoritative Sarmad SDK artifacts or docs were found in this checkout. Therefore no `sarmad_pay_sdk`, `sarmad_ads_sdk`, Sign in with Sarmad, wallet, ads, or subscription code was invented or added.

Production inputs still required: authoritative r350 SDK/docs and checksums for `sarmad_pay_sdk` and `sarmad_ads_sdk` v1.1.0; final Android package name and signing SHA-256; wallet service ID, scopes, nonce/session and one-time server verification contract; ads app ID/ad unit IDs and consent policy; backend URL; server-only developer key (never APK); billing product ID, server entitlement endpoint/signing verification, restore/refund rules, and exact 3-Sarmad/30-day legal terms. No email/password login is planned.

## Environment verification

Passed: `git diff --check`, `node --check specimen.js`, and Android manifest/theme XML parsing. APK compilation was not attempted or claimed: this environment has neither Android SDK nor Gradle, and no Gradle Wrapper exists in the repository. The wrapper must be supplied by the build owner before a real APK build.

## Native versus web

Native: Kotlin Activity, SAF picker/local URI validation, print mode selection, `PrintManager`, and `PrintDocumentAdapter`. Web: specimen image workflow, local browser storage, safe marked-asset guard, bounded edits, OCR notes, and immutable bilingual watermark.
