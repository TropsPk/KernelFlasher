#!/usr/bin/env bash
# Signs the release APK with the keystore stored in repository secrets.
#
# Replaces the third-party sign-android-release actions, which run on an end-of-life
# Node runtime and still use the removed `set-output` command.
# Same steps as before: check zip alignment, sign with apksigner, verify the signature.
#
# Environment:
#   RELEASE_DIR          folder that contains the unsigned release APK
#   SIGNING_KEY_BASE64   keystore file, base64 encoded (secret KEYSTORE)
#   KEY_ALIAS            key alias inside the keystore
#   KEYSTORE_PASSWORD    keystore password
#   KEY_PASSWORD         key password (optional, defaults to the keystore password)
#   BUILD_TOOLS_VERSION  Android build-tools version, for example 35.0.0
#   ANDROID_HOME         Android SDK location (set on GitHub runners)
# Output:
#   signedReleaseFile=<absolute path of the signed APK> in $GITHUB_OUTPUT
set -euo pipefail

fail() { echo "::error::$*" >&2; exit 1; }

[ -n "${RELEASE_DIR:-}" ]         || fail "RELEASE_DIR is not set"
[ -n "${SIGNING_KEY_BASE64:-}" ]  || fail "The KEYSTORE secret is empty"
[ -n "${KEY_ALIAS:-}" ]           || fail "The KEY_ALIAS secret is empty"
[ -n "${KEYSTORE_PASSWORD:-}" ]   || fail "The KEYSTORE_PASSWORD secret is empty"
[ -n "${BUILD_TOOLS_VERSION:-}" ] || fail "BUILD_TOOLS_VERSION is not set"
[ -n "${ANDROID_HOME:-}" ]        || fail "ANDROID_HOME is not set"

build_tools="$ANDROID_HOME/build-tools/$BUILD_TOOLS_VERSION"
[ -x "$build_tools/apksigner" ] || fail "apksigner not found in $build_tools"
[ -x "$build_tools/zipalign" ]  || fail "zipalign not found in $build_tools"

apk="$(find "$RELEASE_DIR" -maxdepth 1 -type f -name '*.apk' ! -name '*-signed.apk' | sort | head -n 1)"
[ -n "$apk" ] || fail "No APK found in $RELEASE_DIR"
echo "Signing $apk"

keystore="$(mktemp "${RUNNER_TEMP:-/tmp}/signing-key.XXXXXX")"
trap 'rm -f "$keystore"' EXIT
if ! printf '%s' "$SIGNING_KEY_BASE64" | base64 -d > "$keystore" 2>/dev/null || [ ! -s "$keystore" ]; then
  fail "The KEYSTORE secret is not valid base64"
fi

# The Gradle build already aligns the APK. Signing must not change that, so only check it.
"$build_tools/zipalign" -c 4 "$apk" || fail "The APK is not zip aligned"

signed="${apk%.apk}-signed.apk"
args=(sign --ks "$keystore" --ks-key-alias "$KEY_ALIAS" --ks-pass env:KEYSTORE_PASSWORD --out "$signed")
if [ -n "${KEY_PASSWORD:-}" ]; then
  args+=(--key-pass env:KEY_PASSWORD)
fi
"$build_tools/apksigner" "${args[@]}" "$apk"

"$build_tools/apksigner" verify --print-certs "$signed"

signed_path="$(cd "$(dirname "$signed")" && pwd)/$(basename "$signed")"
echo "Signed APK: $signed_path"
echo "signedReleaseFile=$signed_path" >> "${GITHUB_OUTPUT:-/dev/stdout}"
