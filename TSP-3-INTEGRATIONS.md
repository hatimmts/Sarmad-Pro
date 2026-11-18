# TSP-3 integration audit

## Implemented safe path

The Android shell now exposes the HTML image inputs with `capture="environment"`. On Android WebView this offers camera capture where the device supports it, while the same controls accept an imported image. The existing filename guard still requires `specimen`, `sample`, `archive`, or `non-negotiable` in an imported filename.

The native `Print specimen over Wi‑Fi…` button uses Android's public `PrintManager` and system print picker. A user can select a configured Wi‑Fi printer; the printable page keeps the permanent bilingual safety watermark. This is a generic Android printing path, not a printer-specific integration, and it must only be used for synthetic/non-negotiable specimen sheets.

## Sarmad wallet, ads, and subscription audit

No wallet SDK/API, advertising SDK configuration, billing product, backend endpoint, protocol-node integration contract, or OAuth client configuration was present in this checkout. The existing `bootstrap.json`, static pages, downloaded binaries, and coordinator archive do not document a supported integration surface. No endpoints, credentials, or invented SDK calls were added.

To implement wallet sign-in safely, the owner must provide the official SDK/API documentation and version, Android application/package registration, OAuth redirect/deep-link details, scopes, token validation rules, and a test tenant or sandbox. Never put wallet secrets in the app.

To implement ads, the owner must provide the approved ad network and SDK version, Android app/ad unit IDs, consent policy, test-device configuration, and confirmation that ads are allowed in an archival/educational tool.

To implement “3 Sarmad / 30 days” ad removal, the owner must provide an official billing API/SDK and product identifier, whether Sarmad is a supported payment rail, server-side purchase verification endpoint and signing keys, refund/restore rules, and the exact currency/legal terms. A client-only entitlement would be spoofable, so it is intentionally not implemented.

## Native versus web

Native Android now provides camera/import affordances through HTML capture hints, the offline WebView shell, and system Wi‑Fi printing. The web layer continues to provide the local specimen project, two sides, bounded editing, archival notes, local storage, safe asset guard, and immutable bilingual watermark.
