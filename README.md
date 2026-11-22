# Tropsi Flasher

Flash, back up and restore kernels on Android. Built for the Tropsi X kernel, works with any AnyKernel3 zip.

## Usage

Open a slot, then flash an AK3 zip, back up the kernel partitions, or restore a backup.
You can also map or mount `vendor_dlkm`, and save `dmesg` and `logcat`.

## Build

```
./gradlew assembleRelease
```

## Credits

Fork of [fatalcoder524/KernelFlasher](https://github.com/fatalcoder524/KernelFlasher), itself a fork of [capntrips/KernelFlasher](https://github.com/capntrips/KernelFlasher).
Licensed under the original license (see `LICENSE`).
