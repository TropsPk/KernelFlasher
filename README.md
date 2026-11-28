<a id="readme-top"></a>

<div align="center">

<h1>⚡ Tropsi Flasher<br /><sub><i>Android Kernel Flasher &nbsp;·&nbsp; Flash. Manage. Control.</i></sub></h1>

**Tropsi Flasher** is a root-based **Android kernel flasher** app for flashing compatible **AnyKernel3 (AK3) kernel ZIPs**, recovery ZIPs, and partition images such as `boot.img`, `recovery.img`, and `vbmeta.img`.

![Platform: Android](https://img.shields.io/badge/Platform-Android_10+-E11D48?style=for-the-badge&labelColor=0B0B0F&logo=android&logoColor=white)
![Root required](https://img.shields.io/badge/Root-Required-E11D48?style=for-the-badge&labelColor=0B0B0F)
![Developer: TropsPk](https://img.shields.io/badge/Developer-TropsPk-E11D48?style=for-the-badge&labelColor=0B0B0F)
[![License: see LICENSE](https://img.shields.io/badge/License-See_LICENSE-2563EB?style=for-the-badge&labelColor=0B0B0F)](LICENSE)

[**About**](#-about-tropsi-flasher) &nbsp;·&nbsp;
[**Supported ZIPs & Images**](#-supported-zips-and-images) &nbsp;·&nbsp;
[**Features**](#-features) &nbsp;·&nbsp;
[**Safety**](#-safety) &nbsp;·&nbsp;
[**Project Info**](#-project-information) &nbsp;·&nbsp;
[**License**](#-credits-and-license)

</div>

## ✨ About Tropsi Flasher

**Tropsi Flasher** is an Android utility for flashing compatible kernel and recovery ZIP packages, along with supported Android partition images.

It brings common flashing and kernel-maintenance tasks into one application.

## 📦 Supported ZIPs and images

| Type | Supported |
| :-- | :-- |
| 🧩 **Kernel ZIPs** | Compatible kernel ZIP packages, including AnyKernel3 (AK3) |
| 🔧 **Recovery ZIPs** | Compatible recovery ZIP packages |
| 💽 **Partition images** | Android partition images, such as `boot.img`, `recovery.img`, and `vbmeta.img` |
| 💾 **Backup & restore** | Kernel-related partition backup and restore |

> [!NOTE]
> Image and package compatibility depends on the device, partition layout, and file format.

## 🚀 Features

<table>
  <tr>
    <td align="center" width="33%">
      <h3>🔍</h3>
      <b>Slot information</b><br />
      View device slot information
    </td>
    <td align="center" width="33%">
      <h3>⚡</h3>
      <b>Flash ZIPs</b><br />
      Flash compatible ZIP packages
    </td>
    <td align="center" width="33%">
      <h3>💽</h3>
      <b>Flash images</b><br />
      Flash supported partition image files
    </td>
  </tr>
  <tr>
    <td align="center" width="33%">
      <h3>💾</h3>
      <b>Backup &amp; restore</b><br />
      Back up and restore kernel-related partitions
    </td>
    <td align="center" width="33%">
      <h3>🔀</h3>
      <b>vendor_dlkm</b><br />
      Toggle the mount and map status of <code>vendor_dlkm</code>
    </td>
    <td align="center" width="33%">
      <h3>📝</h3>
      <b>Logs</b><br />
      Save <code>dmesg</code> and <code>logcat</code> logs
    </td>
  </tr>
</table>

## 🚨 Safety

> [!WARNING]
> Flashing an incompatible package or image, or selecting the wrong partition, can prevent a device from booting. Verify compatibility and maintain appropriate backups before flashing.

## 📋 Project information

| Property | Value |
| :-- | :-- |
| **Application** | Tropsi Flasher |
| **Developer** | TropsPk |
| **Platform** | Android |
| **Requirements** | Root access · Android 10+ (API 29) |
| **Repository** | `TropsPk/KernelFlasher` |

## 📜 Credits and license

This project is based on the **Kernel Flasher** codebase. Preserve original copyright notices and follow the license included in this repository.

See [`LICENSE`](LICENSE) for the applicable terms.

---

<div align="center">

**Tropsi Flasher** · Part of the TropsPk development ecosystem.

*⚡ Flash. Manage. Control.*

<a href="#readme-top">⬆ Back to top</a>

</div>
